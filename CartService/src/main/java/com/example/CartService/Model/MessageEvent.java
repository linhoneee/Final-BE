package com.example.CartService.Model;

import com.example.CartService.Entity.Item;
import com.linhmai.CommonService.model.DistanceData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEvent {
    private Long userId;
    private List<Item> items;
    private SelectedShipping selectedShipping;
    private DistanceData distanceData;
    private double total;
}
