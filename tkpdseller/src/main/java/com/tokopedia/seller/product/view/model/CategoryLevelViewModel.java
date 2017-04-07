package com.tokopedia.seller.product.view.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class CategoryLevelViewModel {
    public static final int UNSELECTED = -1;

    private final List<CategoryViewModel> viewModels;
    private final int level;
    private int selected;

    public CategoryLevelViewModel(List<CategoryViewModel> map, int level) {
        viewModels = map;
        selected = UNSELECTED;
        this.level = level;
    }

    public int getLevel() {
        return level;
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

    public int getSelectedPosition() {
        int selectedPositionFromIndex = UNSELECTED;
        for (int i = 0; i < viewModels.size(); i ++){
            if (viewModels.get(i).getId() == selected){
                selectedPositionFromIndex = i;
                break;
            }
        }
        return selectedPositionFromIndex;
    }

    public CategoryViewModel getSelectedModel() {
        for (CategoryViewModel viewModel : viewModels){
            if (viewModel.getId() == selected){
                return viewModel;
            }
        }
        throw new RuntimeException("Selected item not found");
    }


}
