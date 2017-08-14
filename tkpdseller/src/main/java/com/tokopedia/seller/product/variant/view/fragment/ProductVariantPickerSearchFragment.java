package com.tokopedia.seller.product.variant.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.tokopedia.seller.base.view.activity.BasePickerMultipleItemActivity;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.base.view.listener.BasePickerItemSearchList;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerSearchListAdapter;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerSearchFragment extends BaseSearchListFragment<BlankPresenter, ProductVariantViewModel>
        implements BasePickerItemSearchList, BaseMultipleCheckListAdapter.CheckedCallback<ProductVariantViewModel> {

    private BasePickerMultipleItem<ProductVariantViewModel> pickerMultipleItem;

    private List<ProductVariantViewModel> itemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BasePickerMultipleItem) {
            pickerMultipleItem = (BasePickerMultipleItem<ProductVariantViewModel>) getActivity();
        }
        itemList = getActivity().getIntent().getParcelableArrayListExtra(BasePickerMultipleItemActivity.EXTRA_INTENT_PICKER_ITEM_LIST);
    }

    @Override
    protected BaseListAdapter<ProductVariantViewModel> getNewAdapter() {
        return new ProductVariantPickerSearchListAdapter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onSearchLoaded(itemList, itemList.size());
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        ((BaseMultipleCheckListAdapter<ProductVariantViewModel>) adapter).setCheckedCallback(this);
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(ProductVariantViewModel productVariantViewModel) {

    }

    @Override
    public void onItemChecked(ProductVariantViewModel productVariantViewModel, boolean checked) {
        if (checked) {
            pickerMultipleItem.addItemFromSearch(productVariantViewModel);
        } else {
            pickerMultipleItem.removeItemFromSearch(productVariantViewModel);
        }
    }

    @Override
    public void deselectItem(Object o) {

    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {

    }
}