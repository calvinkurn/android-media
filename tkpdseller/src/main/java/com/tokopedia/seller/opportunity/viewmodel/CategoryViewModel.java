package com.tokopedia.seller.opportunity.viewmodel;

import java.util.List;

/**
 * Created by nisie on 3/6/17.
 */

public class CategoryViewModel {
    int categoryId;
    String categoryName;
    int parent;
    int isHidden;
    int treeLevel;
    String identifier;
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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
