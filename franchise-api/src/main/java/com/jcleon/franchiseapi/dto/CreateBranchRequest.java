package com.jcleon.franchiseapi.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateBranchRequest {

    @NotBlank(message = "Branch name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
