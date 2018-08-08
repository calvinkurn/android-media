package com.tokopedia.seller.product.category.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.common.category.view.model.CategoryLevelViewModel;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseRetryDataBinder;
import com.tokopedia.seller.common.widget.DividerItemDecoration;
import com.tokopedia.seller.product.manage.view.adapter.ProductManageCategoryPickerAdapter;
import com.tokopedia.seller.product.manage.view.adapter.ProductManageSortAdapter;
import com.tokopedia.seller.product.manage.view.model.ProductManageCategoryViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity.CATEGORY_RESULT_ID;
import static com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity.CATEGORY_RESULT_NAME;

/**
 * Created by zulfikarrahman on 10/9/17.
 */

public class CategoryPickerDynamicFragment extends CategoryPickerFragment implements BaseListAdapter.Callback<ProductManageCategoryViewModel> {

    public static final String ADDITIONAL_OPTION = "additional_option";

    private ArrayList<ProductManageCategoryViewModel> categoryViewModels;
    ProductManageCategoryPickerAdapter productManageCategoryPickerAdapter;

    public static CategoryPickerDynamicFragment createInstance(long currentSelected, ArrayList<ProductManageCategoryViewModel> categoryViewModels) {
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
    protected void setupRecyclerView(View view) {
        RecyclerView categoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setMotionEventSplittingEnabled(false);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        productManageCategoryPickerAdapter = new ProductManageCategoryPickerAdapter();
        productManageCategoryPickerAdapter.setCallback(this);
        RetryDataBinder topAdsRetryDataBinder = new BaseRetryDataBinder(productManageCategoryPickerAdapter);
        topAdsRetryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                initVar();
            }
        });
        productManageCategoryPickerAdapter.setRetryView(topAdsRetryDataBinder);
        productManageCategoryPickerAdapter.setIdCategorySelected(selectedCategoryId);
        categoryRecyclerView.setAdapter(productManageCategoryPickerAdapter);
    }

    @Override
    protected void initVar() {
        presenter.fetchCategoryLevelOne();
    }

    @Override
    public void renderCategory(List<CategoryViewModel> listCategory, long categoryId) {
        List<ProductManageCategoryViewModel> categoryViewModelList = new ArrayList<>();
        if (categoryViewModels != null) {
            categoryViewModelList.addAll(categoryViewModels);
        }
        for(CategoryViewModel categoryViewModel : listCategory){
            categoryViewModelList.add(new ProductManageCategoryViewModel(categoryViewModel.getName(),
                    categoryViewModel.getId(), categoryViewModel.isHasChild()));
        }
        productManageCategoryPickerAdapter.addData(categoryViewModelList);
    }

    @Override
    public void renderCategoryFromSelected(List<CategoryLevelViewModel> categoryLevelDomainModels) {
    }

    @Override
    public void showRetryEmpty() {
        productManageCategoryPickerAdapter.showRetryFull(true);
    }

    @Override
    public void hideRetryEmpty() {
        productManageCategoryPickerAdapter.showRetryFull(false);
    }

    @Override
    public void showLoadingDialog() {
        productManageCategoryPickerAdapter.showLoadingFull(true);
    }

    @Override
    public void dismissLoadingDialog() {
        productManageCategoryPickerAdapter.showLoadingFull(false);
    }

    @Override
    public void onItemClicked(ProductManageCategoryViewModel productManageCategoryViewModel) {
        productManageCategoryPickerAdapter.setIdCategorySelected(productManageCategoryViewModel.getId());
        productManageCategoryPickerAdapter.notifyDataSetChanged();
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_RESULT_ID, productManageCategoryViewModel.getId());
        intent.putExtra(CATEGORY_RESULT_NAME, productManageCategoryViewModel.getName());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
