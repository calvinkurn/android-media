package com.tokopedia.seller.product.view.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class CategoryLevelViewModel {
    public static final int UNSELECTED = -1;

    private final List<CategoryViewModel> viewModels;
    private int selected;

    public CategoryLevelViewModel(List<CategoryViewModel> map) {
        viewModels = map;
        selected = UNSELECTED;
    }

    public List<CategoryViewModel> getViewModels() {
        return viewModels;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
