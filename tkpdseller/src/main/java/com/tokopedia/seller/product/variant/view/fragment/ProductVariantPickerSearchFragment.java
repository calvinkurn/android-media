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
import com.tokopedia.seller.product.variant.constant.ExtraConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerSearchListAdapter;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerSearchFragment extends BaseSearchListFragment<BlankPresenter, ProductVariantViewModel>
        implements BasePickerItemSearchList<ProductVariantViewModel>, BaseMultipleCheckListAdapter.CheckedCallback<ProductVariantViewModel> {

    private BasePickerMultipleItem<ProductVariantViewModel> pickerMultipleItem;

    private List<ProductVariantViewModel> itemList;
    private List<ProductVariantUnit> productVariantUnitList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BasePickerMultipleItem) {
            pickerMultipleItem = (BasePickerMultipleItem<ProductVariantViewModel>) getActivity();
        }
        itemList = getActivity().getIntent().getParcelableArrayListExtra(BasePickerMultipleItemActivity.EXTRA_INTENT_PICKER_ITEM_LIST);
        productVariantUnitList = getActivity().getIntent().getParcelableArrayListExtra(ExtraConstant.EXTRA_PRODUCT_VARIANT_UNIT_LIST);
    }

    @Override
    protected BaseMultipleCheckListAdapter<ProductVariantViewModel> getNewAdapter() {
        return new ProductVariantPickerSearchListAdapter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resetPageAndSearch();
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        ((BaseMultipleCheckListAdapter<ProductVariantViewModel>) adapter).setCheckedCallback(this);
    }

    @Override
    protected void searchForPage(int page) {
        onSearchLoaded(itemList, itemList.size());
    }

    @Override
    public void onItemClicked(ProductVariantViewModel productVariantViewModel) {
        // Already handled onItemChecked
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
    public void deselectItem(ProductVariantViewModel productVariantViewModel) {
        ((BaseMultipleCheckListAdapter<ProductVariantViewModel>) adapter).setChecked(productVariantViewModel.getId(), false);
        resetPageAndSearch();
    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {

    }
}