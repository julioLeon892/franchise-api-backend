package com.jcleon.franchiseapi.controller;

import com.jcleon.franchiseapi.dto.BranchResponse;
import com.jcleon.franchiseapi.dto.CreateBranchRequest;
import com.jcleon.franchiseapi.dto.CreateFranchiseRequest;
import com.jcleon.franchiseapi.dto.CreateProductRequest;
import com.jcleon.franchiseapi.dto.FranchiseResponse;
import com.jcleon.franchiseapi.dto.ProductResponse;
import com.jcleon.franchiseapi.dto.TopProductResponse;
import com.jcleon.franchiseapi.dto.UpdateNameRequest;
import com.jcleon.franchiseapi.dto.UpdateStockRequest;
import com.jcleon.franchiseapi.service.FranchiseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final FranchiseService franchiseService;

    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @PostMapping
    public ResponseEntity<FranchiseResponse> createFranchise(@Valid @RequestBody CreateFranchiseRequest request) {
        FranchiseResponse response = franchiseService.createFranchise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{franchiseId}/branches")
    public ResponseEntity<BranchResponse> addBranch(@PathVariable String franchiseId,
                                                    @Valid @RequestBody CreateBranchRequest request) {
        BranchResponse response = franchiseService.addBranch(franchiseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    public ResponseEntity<ProductResponse> addProduct(@PathVariable String franchiseId,
                                                      @PathVariable String branchId,
                                                      @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = franchiseService.addProduct(franchiseId, branchId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{franchiseId}/branches/{branchId}/products/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable String franchiseId,
                                              @PathVariable String branchId,
                                              @PathVariable String productId) {
        franchiseService.removeProduct(franchiseId, branchId, productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
    public ResponseEntity<ProductResponse> updateProductStock(@PathVariable String franchiseId,
                                                              @PathVariable String branchId,
                                                              @PathVariable String productId,
                                                              @Valid @RequestBody UpdateStockRequest request) {
        ProductResponse response = franchiseService.updateProductStock(franchiseId, branchId, productId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{franchiseId}/branches/top-products")
    public ResponseEntity<List<TopProductResponse>> getTopProductsByBranch(@PathVariable String franchiseId) {
        List<TopProductResponse> response = franchiseService.getTopProductsByBranch(franchiseId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{franchiseId}/name")
    public ResponseEntity<FranchiseResponse> updateFranchiseName(@PathVariable String franchiseId,
                                                                 @Valid @RequestBody UpdateNameRequest request) {
        FranchiseResponse response = franchiseService.updateFranchiseName(franchiseId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{franchiseId}/branches/{branchId}/name")
    public ResponseEntity<BranchResponse> updateBranchName(@PathVariable String franchiseId,
                                                           @PathVariable String branchId,
                                                           @Valid @RequestBody UpdateNameRequest request) {
        BranchResponse response = franchiseService.updateBranchName(franchiseId, branchId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/name")
    public ResponseEntity<ProductResponse> updateProductName(@PathVariable String franchiseId,
                                                             @PathVariable String branchId,
                                                             @PathVariable String productId,
                                                             @Valid @RequestBody UpdateNameRequest request) {
        ProductResponse response = franchiseService.updateProductName(franchiseId, branchId, productId, request);
        return ResponseEntity.ok(response);
    }
}
