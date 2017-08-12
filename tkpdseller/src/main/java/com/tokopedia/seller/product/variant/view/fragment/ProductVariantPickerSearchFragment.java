package com.tokopedia.seller.product.variant.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.base.view.listener.BasePickerItemSearchList;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerCacheListAdapter;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerSearchListAdapter;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.UUID;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerSearchFragment extends BaseSearchListFragment<BlankPresenter, ProductVariantViewModel> implements BasePickerItemSearchList{

    private BasePickerMultipleItem pickerMultipleItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BasePickerMultipleItem) {
            pickerMultipleItem = (BasePickerMultipleItem) getActivity();
        }
    }

    @Override
    protected BaseListAdapter<ProductVariantViewModel> getNewAdapter() {
        return new ProductVariantPickerSearchListAdapter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onSearchLoaded(pickerMultipleItem.getItemPickerTypeList(), pickerMultipleItem.getItemPickerTypeList().size());
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(ProductVariantViewModel productVariantViewModel) {

    }

    @Override
    public void notifyChange() {

    }

    @Override
    public void onSearchSubmitted(String text) {
        ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();
        productVariantViewModel.setId(1);
        productVariantViewModel.setTitle(UUID.randomUUID().toString());
        productVariantViewModel.setImageUrl("https://image.flaticon.com/teams/slug/freepik.jpg");
        pickerMultipleItem.addItemFromSearch(productVariantViewModel);
    }

    @Override
    public void onSearchTextChanged(String text) {

    }
}