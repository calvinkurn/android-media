package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsDetailNewGroupDI;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewGroupPresenter;

/**
 * Fragment to Create New Ads in Top Ads Group
 * Pass GroupId if the group has already existed, dont pass (or pass 0) if not exist
 * Pass group name to show the group to be created/edited
 * Created by Hendry on 02.03.2017
 */

public class TopAdsDetailNewGroupFragment extends TopAdsDetailNewFragment<TopAdsDetailNewGroupPresenter>
        implements TopAdsDetailNewGroupView {

    public static Fragment createInstance(String groupName, String groupId) {
        Fragment fragment = new TopAdsDetailNewGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_NAME, groupName);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailNewGroupDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_detail_new_group;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nameEditText.setEnabled(false);
        nameInputLayout.setHint(getString(R.string.label_top_ads_group_name));
        detailAd = new TopAdsDetailGroupViewModel();
    }

    @Override
    protected void loadAdDetail() {
        presenter.getDetailAd(adId);
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        presenter.saveAdNew(name, (TopAdsDetailGroupViewModel) detailAd, topAdsProductList);
    }

    @Override
    protected void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, false);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, false);
        intent.putParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, topAdsProductList);
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
    }

    @Override
    public void showErrorGroupEmpty() {
        // TODO show error when group is empty
    }

    @Override
    public void showLoading(boolean isShown) {
        if (isShown) {
            super.showLoading();
        } else {
            super.hideLoading();
        }
    }
}