package com.jcleon.franchiseapi.dto;

public class TopProductResponse {

    private String branchId;
    private String branchName;
    private ProductResponse product;

    public TopProductResponse() {
    }

    public TopProductResponse(String branchId, String branchName, ProductResponse product) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.product = product;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }
}
