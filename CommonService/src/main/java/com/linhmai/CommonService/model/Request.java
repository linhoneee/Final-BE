package com.linhmai.CommonService.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private Long id;
    private Long userId;
    private List<Item> items;
    private Shipping selectedShipping;
    private DistanceData distanceData;
    private Double total;
}
