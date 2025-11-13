package com.jcleon.franchiseapi.repository;

import com.jcleon.franchiseapi.model.Franchise;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FranchiseRepository extends MongoRepository<Franchise, String> {
}
