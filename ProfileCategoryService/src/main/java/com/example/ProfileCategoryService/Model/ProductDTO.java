package com.example.ProfileCategoryService.Model;


import com.example.ProfileCategoryService.Entity.Product;
import com.example.ProfileCategoryService.Entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Product product;
    private List<ProductImage> productImages;
}
