package com.tokopedia.seller.topads.dashboard.view.fragment;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.adapter.viewholder.TopAdsEmptyProductListDataBinder;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsCreatePromoWithoutGroupModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenter;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public class TopAdsNewProductListWithoutGroupFragment extends TopAdsNewProductListFragment<TopAdsCreatePromoWithoutGroupModel, TopAdsGetProductDetailPresenter> {

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
        daggerPresenter.attachView(this);
    }

    @Override
    protected void initiateStepperModel() {
        stepperModel = new TopAdsCreatePromoWithoutGroupModel();
    }

    @Override
    protected void goToNextPage() {
        if(stepperListener != null){
            UnifyTracking.eventTopAdsProductAddPromoWithoutGroupStep1();
            stepperListener.goToNextPage(stepperModel);
            hideLoading();
        }
    }

    @Override
    protected TopAdsEmptyProductListDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyProductListDataBinder topAdsEmptyProductListDataBinder = super.getEmptyViewDefaultBinder();
        topAdsEmptyProductListDataBinder.setEmptyContentText(getString(R.string.top_ads_label_choose_product_desc_empty_without_group));
        return topAdsEmptyProductListDataBinder;
    }
}
