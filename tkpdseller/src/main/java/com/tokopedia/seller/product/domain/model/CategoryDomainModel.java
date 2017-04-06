package com.tokopedia.seller.product.domain.model;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryDomainModel {

    private int id;

    private String name;

    private String identifier;
    private boolean hasChild;

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

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public boolean isHasChild() {
        return hasChild;
    }
}
