package com.tokopedia.seller.goldmerchant.featured.view.fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.featured.di.component.DaggerFeaturedProductComponent;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.FeaturedProductAdapter;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;
import com.tokopedia.seller.goldmerchant.featured.view.listener.FeaturedProductView;
import com.tokopedia.seller.goldmerchant.featured.view.presenter.FeaturedProductPresenterImpl;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductFragment extends BaseListFragment<BlankPresenter, FeaturedProductModel> implements FeaturedProductView {

    @Inject
    FeaturedProductPresenterImpl featuredProductPresenter;

    @Override
    protected void initInjector() {
        DaggerFeaturedProductComponent
                .builder()
                .goldMerchantComponent(getComponent(GoldMerchantComponent.class))
                .build().inject(this);
        featuredProductPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        featuredProductPresenter.detachView();
    }

    @Override
    public void onItemClicked(FeaturedProductModel featuredProductModel) {

    }

    @Override
    protected BaseListAdapter<FeaturedProductModel> getNewAdapter() {
        return new FeaturedProductAdapter();
    }

    @Override
    protected void searchForPage(int page) {
        featuredProductPresenter.loadData();
    }

    @Override
    public void onPostSuccess() {

    }
}
