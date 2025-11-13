package com.jcleon.franchiseapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "franchises")
public class Franchise {

    @Id
    private String id;
    private String name;
    private List<Branch> branches = new ArrayList<>();

    public Franchise() {
    }

    public Franchise(String name) {
        this.name = name;
    }

    public Franchise(String id, String name, List<Branch> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }
}
