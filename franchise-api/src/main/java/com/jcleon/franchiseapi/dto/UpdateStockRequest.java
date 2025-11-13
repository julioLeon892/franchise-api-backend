package com.jcleon.franchiseapi.dto;

import jakarta.validation.constraints.Min;

public class UpdateStockRequest {

    @Min(value = 0, message = "Stock must be greater or equal to zero")
    private int stock;

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
