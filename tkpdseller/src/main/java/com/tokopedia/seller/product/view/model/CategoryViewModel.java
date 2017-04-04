package com.tokopedia.seller.product.view.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryViewModel {
    private String name;
    private int id;
    private List<CategoryViewModel> child;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChild(List<CategoryViewModel> child) {
        this.child = child;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<CategoryViewModel> getChild() {
        return child;
    }
}
