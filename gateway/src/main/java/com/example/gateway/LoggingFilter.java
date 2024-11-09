package com.example.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    // Bản đồ giữa mẫu đường dẫn và danh sách vai trò yêu cầu
    private final Map<String, List<String>> pathRoleMap = new HashMap<>();

    public LoggingFilter() {
        pathRoleMap.put("/addresses/user/**", List.of("USER"));
        pathRoleMap.put("/users", List.of("ADMIN"));
        pathRoleMap.put("/users/{userId}", List.of("ADMIN", "USER")); // Cho phép cả ADMIN và USER
        // Thêm các mẫu đường dẫn và vai trò tương ứng khác nếu cần
    }

    public Mono<Void> checkTokenAndRole(ServerWebExchange exchange) {
        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (authHeaders == null || authHeaders.isEmpty()) {
            logger.warn("Không có token trong yêu cầu.");
            return Mono.error(new CommonException("AUTH01", "thiếu token", HttpStatus.UNAUTHORIZED));
        }

        String path = exchange.getRequest().getPath().value();

        // Xác định danh sách vai trò yêu cầu cho đường dẫn hiện tại
        List<String> requiredRoles = getRequiredRolesForPath(path);

        if (requiredRoles == null || requiredRoles.isEmpty()) {
            // Nếu không có yêu cầu vai trò cụ thể, cho phép tiếp tục mà không kiểm tra quyền
            return Mono.empty();
        }

        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new AccessDeniedException("Không tìm thấy SecurityContext")))
                .flatMap(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    if (authentication == null || !authentication.isAuthenticated()) {
                        logger.warn("Người dùng chưa được xác thực.");
                        return Mono.error(new CommonException("AUTH01", "Người dùng chưa được xác thực", HttpStatus.UNAUTHORIZED));
                    }

                    if (authentication instanceof JwtAuthenticationToken) {
                        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                        Instant expirationTime = jwt.getExpiresAt();

                        // Kiểm tra nếu token đã hết hạn
                        if (expirationTime != null && Instant.now().isAfter(expirationTime)) {
                            logger.warn("Token đã hết hạn.");
                            return Mono.error(new CommonException("AUTH03", "Token đã hết hạn.", HttpStatus.UNAUTHORIZED));
                        }
                    }

                    // Lấy danh sách vai trò của người dùng
                    List<String> userRoles = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());

                    // Kiểm tra xem người dùng có ít nhất một vai trò trong danh sách yêu cầu không
                    boolean hasRequiredRole = userRoles.stream().anyMatch(requiredRoles::contains);
                    if (!hasRequiredRole) {
                        logger.warn("Người dùng không có quyền truy cập vào API.");
                        logger.warn("Vai trò yêu cầu: {}. Vai trò của người dùng: {}.", requiredRoles, userRoles);
                        return Mono.error(new CommonException("AUTH04", "người dùng không có quyền truy cập, vui lòng xem log", HttpStatus.FORBIDDEN));
                    }

                    logger.info("Người dùng có quyền truy cập vào API.");
                    logger.warn("Vai trò yêu cầu: {}. Vai trò của người dùng: {}.", requiredRoles, userRoles);

                    return Mono.empty();
                });
    }

    private List<String> getRequiredRolesForPath(String path) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (Map.Entry<String, List<String>> entry : pathRoleMap.entrySet()) {
            String pattern = entry.getKey();
            if (matcher.match(pattern, path)) {
                return entry.getValue();
            }
        }
        return null; // Không có vai trò yêu cầu cụ thể
    }
}
