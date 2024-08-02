package com.example.OrderService.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEvent {
    private int userId;
    private List<Item> items;
    private SelectedShipping selectedShipping;
    private DistanceData distanceData;
    private double total;
}