package com.jcleon.franchiseapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @Min(value = 0, message = "Stock must be greater or equal to zero")
    private int stock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
