package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.di.TopAdsDetailNewGroupDI;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;

/**
 * Fragment to Create New Ads in Top Ads Group
 * Pass GroupId if the group has already existed, dont pass (or pass 0) if not exist
 * Pass group name to show the group to be created/edited
 * Created by Hendry on 02.03.2017
 */

public class TopAdsDetailNewGroupFragment extends TopAdsDetailNewFragment<TopAdsDetailNewGroupPresenter>
        implements TopAdsDetailNewGroupView {

    public static Fragment createInstance(String groupName, String groupId, String itemIdToAdd) {
        Fragment fragment = new TopAdsDetailNewGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_NAME, groupName);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        bundle.putString(TopAdsExtraConstant.EXTRA_ITEM_ID, itemIdToAdd);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailNewGroupDI.createPresenter((BaseActivity)getActivity());
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
                TextUtils.isEmpty(adId) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void loadAdDetail() {
        // no need to load detail for group, and for existing group
        // presenter.getDetailAd(adId);
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        if (detailAd ==  null) {
            return;
        }
        if (TextUtils.isEmpty(adId)) { // saveNew
            presenter.saveAdNew(name, (TopAdsDetailGroupViewModel) detailAd, topAdsProductList);
        } else { // save New with existing group Id
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
    public void goToGroupDetail(String groupId) {
        Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, groupId);
        startActivity(intent);
    }

}