package com.tokopedia.seller.product.category.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.common.category.domain.interactor.FetchCategoryWithParentChildUseCase;
import com.tokopedia.core.common.category.view.model.CategoryLevelViewModel;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 10/9/17.
 */

public class CategoryPickerDynamicFragment extends CategoryPickerFragment {

    public static final String ADDITIONAL_OPTION = "additional_option";

    private ArrayList<CategoryViewModel> categoryViewModels;

    public static CategoryPickerDynamicFragment createInstance(long currentSelected, ArrayList<CategoryViewModel> categoryViewModels) {
        CategoryPickerDynamicFragment categoryPickerDynamicFragment = new CategoryPickerDynamicFragment();
        Bundle args = new Bundle();
        args.putLong(INIT_SELECTED, currentSelected);
        args.putParcelableArrayList(ADDITIONAL_OPTION, categoryViewModels);
        categoryPickerDynamicFragment.setArguments(args);
        return categoryPickerDynamicFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        categoryViewModels = getArguments().getParcelableArrayList(ADDITIONAL_OPTION);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void renderCategory(List<CategoryViewModel> listCategory, long categoryId) {
        if(categoryId == FetchCategoryWithParentChildUseCase.UNSELECTED){
            List<CategoryViewModel> categoryViewModelList = new ArrayList<>();
            if(categoryViewModels != null) {
                categoryViewModelList.addAll(categoryViewModels);
            }
            categoryViewModelList.addAll(listCategory);
            super.renderCategory(categoryViewModelList, categoryId);
        }else{
            super.renderCategory(listCategory, categoryId);
        }
    }

    @Override
    public void renderCategoryFromSelected(List<CategoryLevelViewModel> categoryLevelDomainModels) {
        for(CategoryLevelViewModel categoryLevelViewModel : categoryLevelDomainModels){
            if(categoryLevelViewModel.getLevel() == 0){
                List<CategoryViewModel> categoryViewModels = new ArrayList<>();
                categoryViewModels.addAll(this.categoryViewModels);
                categoryViewModels.addAll(categoryLevelViewModel.getViewModels());
                categoryLevelViewModel.setViewModels(categoryViewModels);
                super.renderCategoryFromSelected(categoryLevelDomainModels);
            }
        }
    }
}
