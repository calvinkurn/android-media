package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.presenter.TopAdsDetailProductPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsDetailProductPresenterImpl;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelSwitch;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsDetailProductFragment extends TopAdsDetailFragment<TopAdsDetailProductPresenter> {

    private ProductAd productAd;

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsDetailProductPresenterImpl(getActivity(), this, new TopAdsProductAdInteractorImpl(new TopAdsManagementService(),
                new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(getActivity())));
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        productAd = bundle.getParcelable(TopAdsExtraConstant.EXTRA_DETAIL_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_product_detail;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if(checked){
            presenter.turnOnAds(productAd, SessionHandler.getShopID(getActivity()));
        }else{
            presenter.turnOffAds(productAd, SessionHandler.getShopID(getActivity()));
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if(productAd != null) {
            loadAdDetail(productAd);
        }
    }

    public static Fragment createInstance(ProductAd productAd) {
        Fragment fragment = new TopAdsDetailProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_DETAIL_DATA, productAd);
        fragment.setArguments(bundle);
        return fragment;
    }
}
