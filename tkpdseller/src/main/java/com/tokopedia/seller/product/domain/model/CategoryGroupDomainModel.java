package com.tokopedia.seller.product.domain.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryGroupDomainModel {

    private int id;

    private String name;

    private String identifier;

    private int weight;

    private List<CategoryGroupDomainModel> child;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<CategoryGroupDomainModel> getChild() {
        return child;
    }

    public void setChild(List<CategoryGroupDomainModel> child) {
        this.child = child;
    }
}
