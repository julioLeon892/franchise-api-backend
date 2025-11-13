package com.jcleon.franchiseapi.service;

import com.jcleon.franchiseapi.dto.BranchResponse;
import com.jcleon.franchiseapi.dto.CreateBranchRequest;
import com.jcleon.franchiseapi.dto.CreateFranchiseRequest;
import com.jcleon.franchiseapi.dto.CreateProductRequest;
import com.jcleon.franchiseapi.dto.FranchiseResponse;
import com.jcleon.franchiseapi.dto.ProductResponse;
import com.jcleon.franchiseapi.dto.TopProductResponse;
import com.jcleon.franchiseapi.dto.UpdateNameRequest;
import com.jcleon.franchiseapi.dto.UpdateStockRequest;
import com.jcleon.franchiseapi.exception.NotFoundException;
import com.jcleon.franchiseapi.model.Branch;
import com.jcleon.franchiseapi.model.Franchise;
import com.jcleon.franchiseapi.model.Product;
import com.jcleon.franchiseapi.repository.FranchiseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FranchiseService {

    private final FranchiseRepository franchiseRepository;

    public FranchiseService(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public FranchiseResponse createFranchise(CreateFranchiseRequest request) {
        Franchise franchise = new Franchise(request.getName());
        Franchise saved = franchiseRepository.save(franchise);
        return toFranchiseResponse(saved);
    }

    public BranchResponse addBranch(String franchiseId, CreateBranchRequest request) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        Branch branch = new Branch(UUID.randomUUID().toString(), request.getName());
        ensureBranches(franchise).add(branch);
        Franchise saved = franchiseRepository.save(franchise);
        return toBranchResponse(findBranchOrThrow(saved, branch.getId()));
    }

    public ProductResponse addProduct(String franchiseId, String branchId, CreateProductRequest request) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        Branch branch = findBranchOrThrow(franchise, branchId);
        Product product = new Product(UUID.randomUUID().toString(), request.getName(), request.getStock());
        ensureProducts(branch).add(product);
        Franchise saved = franchiseRepository.save(franchise);
        Branch updatedBranch = findBranchOrThrow(saved, branchId);
        Product updatedProduct = findProductOrThrow(updatedBranch, product.getId());
        return toProductResponse(updatedProduct);
    }

    public void removeProduct(String franchiseId, String branchId, String productId) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        Branch branch = findBranchOrThrow(franchise, branchId);
        boolean removed = ensureProducts(branch).removeIf(product -> product.getId().equals(productId));
        if (!removed) {
            throw new NotFoundException("Product not found with id: " + productId);
        }
        franchiseRepository.save(franchise);
    }

    public ProductResponse updateProductStock(String franchiseId, String branchId, String productId, UpdateStockRequest request) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        Branch branch = findBranchOrThrow(franchise, branchId);
        Product product = findProductOrThrow(branch, productId);
        product.setStock(request.getStock());
        franchiseRepository.save(franchise);
        return toProductResponse(product);
    }

    public FranchiseResponse updateFranchiseName(String franchiseId, UpdateNameRequest request) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        franchise.setName(request.getName());
        Franchise saved = franchiseRepository.save(franchise);
        return toFranchiseResponse(saved);
    }

    public BranchResponse updateBranchName(String franchiseId, String branchId, UpdateNameRequest request) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        Branch branch = findBranchOrThrow(franchise, branchId);
        branch.setName(request.getName());
        franchiseRepository.save(franchise);
        return toBranchResponse(branch);
    }

    public ProductResponse updateProductName(String franchiseId, String branchId, String productId, UpdateNameRequest request) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        Branch branch = findBranchOrThrow(franchise, branchId);
        Product product = findProductOrThrow(branch, productId);
        product.setName(request.getName());
        franchiseRepository.save(franchise);
        return toProductResponse(product);
    }

    public List<TopProductResponse> getTopProductsByBranch(String franchiseId) {
        Franchise franchise = getFranchiseOrThrow(franchiseId);
        return ensureBranches(franchise).stream()
                .map(branch -> ensureProducts(branch).stream()
                        .max(Comparator.comparingInt(Product::getStock))
                        .map(product -> new TopProductResponse(branch.getId(), branch.getName(), toProductResponse(product))))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Franchise getFranchiseOrThrow(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new NotFoundException("Franchise not found with id: " + franchiseId));
    }

    private List<Branch> ensureBranches(Franchise franchise) {
        if (franchise.getBranches() == null) {
            franchise.setBranches(new ArrayList<>());
        }
        return franchise.getBranches();
    }

    private Branch findBranchOrThrow(Franchise franchise, String branchId) {
        return ensureBranches(franchise).stream()
                .filter(branch -> branch.getId().equals(branchId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Branch not found with id: " + branchId));
    }

    private List<Product> ensureProducts(Branch branch) {
        if (branch.getProducts() == null) {
            branch.setProducts(new ArrayList<>());
        }
        return branch.getProducts();
    }

    private Product findProductOrThrow(Branch branch, String productId) {
        return ensureProducts(branch).stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
    }

    private FranchiseResponse toFranchiseResponse(Franchise franchise) {
        List<BranchResponse> branches = franchise.getBranches().stream()
                .map(this::toBranchResponse)
                .collect(Collectors.toList());
        return new FranchiseResponse(franchise.getId(), franchise.getName(), branches);
    }

    private BranchResponse toBranchResponse(Branch branch) {
        List<ProductResponse> products = branch.getProducts().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
        return new BranchResponse(branch.getId(), branch.getName(), products);
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getStock());
    }
}
