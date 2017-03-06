package com.tokopedia.seller.opportunity.viewmodel;

import java.util.List;

/**
 * Created by nisie on 3/6/17.
 */

public class CategoryViewModel {
    int categoryId;
    String categoryName;
    int treeLevel;
    List<CategoryViewModel> listChild;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    public List<CategoryViewModel> getListChild() {
        return listChild;
    }

    public void setListChild(List<CategoryViewModel> listChild) {
        this.listChild = listChild;
    }
}
