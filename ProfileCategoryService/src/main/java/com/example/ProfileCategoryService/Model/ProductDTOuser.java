package com.example.ProfileCategoryService.Model;

import com.example.ProfileCategoryService.Entity.Product;
import com.example.ProfileCategoryService.Entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTOuser {
    private Product product;
    private ProductImage primaryImage;
}
