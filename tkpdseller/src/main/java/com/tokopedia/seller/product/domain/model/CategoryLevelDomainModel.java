package com.tokopedia.seller.product.domain.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class CategoryLevelDomainModel {
    private Integer selected;
    private int parent;
    private List<CategoryDomainModel> categoryModels;

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public int getParent() {
        return parent;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public void setCategoryModels(List<CategoryDomainModel> categoryModels) {
        this.categoryModels = categoryModels;
    }

    public List<CategoryDomainModel> getCategoryModels() {
        return categoryModels;
    }
}
