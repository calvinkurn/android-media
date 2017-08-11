package com.tokopedia.seller.product.variant.view.fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.base.view.fragment.BaseCacheListFragment;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantPickerCacheListAdapter;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantPickerSearchFragment extends BaseSearchListFragment<BlankPresenter, ItemPickerType> {

    @Override
    protected BaseListAdapter getNewAdapter() {
        return new ProductVariantPickerCacheListAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(ItemPickerType itemPickerType) {

    }
}
