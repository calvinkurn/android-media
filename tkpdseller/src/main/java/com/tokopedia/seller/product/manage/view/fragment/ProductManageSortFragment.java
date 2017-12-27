package com.tokopedia.seller.product.manage.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.di.DaggerProductManageComponent;
import com.tokopedia.seller.product.manage.di.ProductManageModule;
import com.tokopedia.seller.product.manage.view.adapter.ProductManageSortAdapter;
import com.tokopedia.seller.product.manage.view.listener.ProductManageSortView;
import com.tokopedia.seller.product.manage.view.model.ProductManageSortModel;
import com.tokopedia.seller.product.manage.view.presenter.ProductManageSortPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageSortFragment extends BaseListFragment<ProductManageSortPresenter, ProductManageSortModel> implements ProductManageSortView {

    @Inject
    ProductManageSortPresenter productManageSortPresenter;

    String selectedSortProduct = SortProductOption.POSITION;

    public static ProductManageSortFragment createInstance(String selectedSortProduct) {
        ProductManageSortFragment productManageSortFragment = new ProductManageSortFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProductManageConstant.EXTRA_SORT_SELECTED, selectedSortProduct);
        productManageSortFragment.setupArguments(bundle);
        return productManageSortFragment;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerProductManageComponent.builder()
                .productComponent(getComponent(ProductComponent.class))
                .productManageModule(new ProductManageModule())
                .build()
                .inject(this);
        productManageSortPresenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_manage_sort;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        selectedSortProduct = arguments.getString(ProductManageConstant.EXTRA_SORT_SELECTED, SortProductOption.POSITION);
    }

    @Override
    protected BaseListAdapter<ProductManageSortModel> getNewAdapter() {
        ProductManageSortAdapter productManageSortAdapter = new ProductManageSortAdapter();
        productManageSortAdapter.setSortProductOption(selectedSortProduct);
        return productManageSortAdapter;
    }

    @Override
    protected void searchForPage(int page) {
        productManageSortPresenter.getListSortManageProduct(getResources().getStringArray(R.array.sort_option));
    }

    @Override
    public void onSuccessGetListSort(List<ProductManageSortModel> productManageSortModels) {
        onSearchLoaded(productManageSortModels, productManageSortModels.size());
    }

    @Override
    public void onItemClicked(ProductManageSortModel productManageSortModel) {
        ((ProductManageSortAdapter) adapter).setSortProductOption(productManageSortModel.getId());
        adapter.notifyDataSetChanged();
        Intent intent = new Intent();
        intent.putExtra(ProductManageConstant.EXTRA_SORT_SELECTED, productManageSortModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected boolean hasNextPage() {
        return false;
    }
}
