package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsDetailNewGroupDI;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailGroupActivity;
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
        nameEditText.setFocusable(false);
        nameInputLayout.setHint(getString(R.string.label_top_ads_group_name));
        detailAd = new TopAdsDetailGroupViewModel();

        view.findViewById(R.id.linear_partial_top_ads_edit_ad).setVisibility(
                TextUtils.isEmpty(adId) ? View.VISIBLE: View.GONE);
    }

    @Override
    protected void loadAdDetail() {
        // no need to load detail for group, and for existing group
        // presenter.getDetailAd(adId);
    }

    @Override
    protected void saveAd() {
        if (TextUtils.isEmpty(adId)) { // saveNew
            super.saveAd();
            presenter.saveAdNew(name, (TopAdsDetailGroupViewModel) detailAd, topAdsProductList);
        }
        else { // save New with existing group Id
            presenter.saveAdExisting(adId, topAdsProductList);
        }
    }

    @Override
    protected void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, !TextUtils.isEmpty(adId));
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, false);
        intent.putParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, topAdsProductList);
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
    }

    @Override
    public void showLoading(boolean isShown) {
        if (isShown) {
            super.showLoading();
        } else {
            super.hideLoading();
        }
    }

    @Override
    public void goToGroupDetail(String groupId) {
        Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        startActivity(intent);
    }
}