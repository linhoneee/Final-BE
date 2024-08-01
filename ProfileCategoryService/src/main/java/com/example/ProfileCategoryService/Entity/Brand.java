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
@Table("brands")
public class Brand {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    // Getters and Setters
}
