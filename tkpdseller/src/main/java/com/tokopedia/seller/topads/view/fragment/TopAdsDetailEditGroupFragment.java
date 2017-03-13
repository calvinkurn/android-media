package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsDetailEditGroupDI;
import com.tokopedia.seller.topads.di.TopAdsDetailEditShopDI;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditShopPresenter;

import java.util.ArrayList;

public class TopAdsDetailEditGroupFragment extends TopAdsDetailEditFragment<TopAdsDetailEditGroupPresenter> {

    public static Fragment createInstance(String name, String adId) {
        Fragment fragment = new TopAdsDetailEditGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_NAME, name);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_detail_edit_group;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nameInputLayout.setHint(getString(R.string.label_top_ads_group_name));
        nameEditText.setEnabled(true);
        view.requestFocus();
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailEditGroupDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected void loadAdDetail() {
        super.loadAdDetail();
        presenter.getDetailAd(adId);
    }

    @Override
    protected void loadAd(TopAdsDetailAdViewModel detailAd) {
        super.loadAd(detailAd);
        nameEditText.setText(detailAd.getTitle());
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        if (TextUtils.isEmpty(nameEditText.getText().toString().trim())) {
            onSaveAdError(getString(R.string.label_top_ads_error_empty_group_name));
            return;
        }
        if (detailAd !=  null) {
            presenter.saveAd((TopAdsDetailGroupViewModel) detailAd);
        }
    }
}