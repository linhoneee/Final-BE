package com.example.ProfileCategoryService.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("product_images")
public class ProductImage {

    @Id
    private Long id;

    @Column("product_id")
    private Long productId;

    @Column("url")
    private String url;

    @Column("is_primary")
    private Boolean isPrimary;
}
