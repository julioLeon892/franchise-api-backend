package com.jcleon.franchiseapi.service;

import com.jcleon.franchiseapi.exception.NotFoundException;
import com.jcleon.franchiseapi.model.Branch;
import com.jcleon.franchiseapi.model.Franchise;
import com.jcleon.franchiseapi.model.Product;
import com.jcleon.franchiseapi.repository.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseService franchiseService;

    private Franchise franchise;

    @BeforeEach
    void setUp() {
        franchise = new Franchise();
        franchise.setId("fr-1");
        franchise.setName("Test Franchise");
    }

    @Test
    void getTopProductsByBranchReturnsHighestStockPerBranch() {
        Branch branchA = new Branch("br-1", "Branch A");
        branchA.setProducts(new java.util.ArrayList<>(List.of(
                new Product("pd-1", "Product 1", 10),
                new Product("pd-2", "Product 2", 5)
        )));
        Branch branchB = new Branch("br-2", "Branch B");
        branchB.setProducts(new java.util.ArrayList<>(List.of(
                new Product("pd-3", "Product 3", 4),
                new Product("pd-4", "Product 4", 20)
        )));
        franchise.setBranches(List.of(branchA, branchB));

        when(franchiseRepository.findById("fr-1")).thenReturn(Optional.of(franchise));

        var result = franchiseService.getTopProductsByBranch("fr-1");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getBranchId()).isEqualTo("br-1");
        assertThat(result.get(0).getProduct().getId()).isEqualTo("pd-1");
        assertThat(result.get(1).getBranchId()).isEqualTo("br-2");
        assertThat(result.get(1).getProduct().getId()).isEqualTo("pd-4");
    }

    @Test
    void removeProductThrowsWhenProductDoesNotExist() {
        Branch branch = new Branch("br-1", "Branch");
        branch.setProducts(new java.util.ArrayList<>(List.of(new Product("pd-1", "Product", 5))));
        franchise.setBranches(List.of(branch));

        when(franchiseRepository.findById("fr-1")).thenReturn(Optional.of(franchise));

        assertThatThrownBy(() -> franchiseService.removeProduct("fr-1", "br-1", "pd-2"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Product not found with id: pd-2");
    }
}
