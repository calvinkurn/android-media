package com.tokopedia.seller.product.featured.view.fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.featured.view.adapter.FeaturedProductAdapter;
import com.tokopedia.seller.product.featured.view.adapter.model.FeaturedProductModel;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductFragment extends BaseListFragment<BlankPresenter, FeaturedProductModel> {
    @Override
    public void onItemClicked(FeaturedProductModel featuredProductModel) {

    }

    @Override
    protected BaseListAdapter<FeaturedProductModel> getNewAdapter() {
        return new FeaturedProductAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }
}
