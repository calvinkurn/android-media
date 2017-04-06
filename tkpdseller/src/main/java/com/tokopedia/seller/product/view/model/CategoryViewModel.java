package com.tokopedia.seller.product.view.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryViewModel {
    private String name;
    private int id;
    private boolean hasChild;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public boolean isHasChild() {
        return hasChild;
    }
}
