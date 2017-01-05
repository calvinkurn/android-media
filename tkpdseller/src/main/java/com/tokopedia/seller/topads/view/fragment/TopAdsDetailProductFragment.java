package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.presenter.TopAdsDetailProductPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsDetailProductPresenterImpl;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelSwitch;

import butterknife.BindView;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsDetailProductFragment extends TopAdsDetailFragment<TopAdsDetailProductPresenter> {

    ProductAd productAd;

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsDetailProductPresenterImpl(this, new TopAdsProductAdInteractorImpl(new TopAdsManagementService(),
                new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(getActivity())));
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        productAd = bundle.getParcelable(TopAdsExtraConstant.EXTRA_DETAIL_DATA);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        if(productAd != null) {
            setData(productAd);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_product_detail;
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        status.setListenerValue(new TopAdsLabelSwitch.ListenerSwitchValue() {
            @Override
            public void onValueChange(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    presenter.turnOnAds(productAd, SessionHandler.getShopID(getActivity()));
                }else{
                    presenter.turnOffAds(productAd, SessionHandler.getShopID(getActivity()));
                }
            }
        });
    }

    public static Fragment createInstance(ProductAd productAd) {
        Fragment fragment = new TopAdsDetailProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_DETAIL_DATA, productAd);
        fragment.setArguments(bundle);
        return fragment;
    }
}
