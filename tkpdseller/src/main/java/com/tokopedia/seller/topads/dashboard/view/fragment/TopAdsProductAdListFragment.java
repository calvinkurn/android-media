package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.seller.topads.dashboard.view.model.Ad;
import com.tokopedia.seller.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.seller.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsProductAdListPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsProductAdListPresenterImpl;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsFilterProductActivity;
import com.tokopedia.seller.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

/**
 * Created by zulfikarrahman on 12/16/16.
 */

public class TopAdsProductAdListFragment extends TopAdsAdListFragment<TopAdsProductAdListPresenter, Ad> implements TopAdsEmptyAdDataBinder.Callback {

    private long groupId;
    private GroupAd groupAd;

    public static Fragment createInstance(GroupAd groupAd) {
        TopAdsProductAdListFragment fragment = new TopAdsProductAdListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_GROUP, groupAd);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onFirstTimeLaunched() {
        super.onFirstTimeLaunched();
        groupAd = getArguments().getParcelable(TopAdsExtraConstant.EXTRA_GROUP);
        if (groupAd != null && !TextUtils.isEmpty(groupAd.getId())) {
            groupId = Integer.valueOf(groupAd.getId());
        }
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsProductAdListPresenterImpl(getActivity(), this);
    }

    @Override
    protected void searchForPage(int page) {
        presenter.searchAd(startDate, endDate, keyword, status, groupId, page);
    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_empty_product_title_promo_text));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_empty_product_promo_content_text));
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.menu_top_ads_add_promo_product));
        emptyGroupAdsDataBinder.setCallback(this);
        return emptyGroupAdsDataBinder;
    }

    @Override
    public void onItemClicked(Ad ad) {
        if (ad instanceof ProductAd) {
            Intent intent = new Intent(getActivity(), TopAdsDetailProductActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD, (ProductAd) ad);
            startActivityForResult(intent, REQUEST_CODE_AD_CHANGE);
        }
    }

    @Override
    public void goToFilter() {
        Intent intent = new Intent(getActivity(), TopAdsFilterProductActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, groupId);
        if (groupAd != null) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_ID, groupAd.getId());
            intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_CURRENT_GROUP_NAME, groupAd.getName());
        }
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_FILTER && intent != null) {
            status = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
            groupId = intent.getLongExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_GROUP_ID, groupId);
            setAndSearchForPage(START_PAGE);
        }
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // no op
    }

    @Override
    public void onEmptyButtonClicked() {
        onCreateAd();
    }

    @Override
    public void onCreateAd() {
        Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
        this.startActivityForResult(intent, REQUEST_CODE_AD_ADD);
    }
}