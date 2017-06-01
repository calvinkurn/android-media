package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.model.Ad;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.seller.topads.view.presenter.TopAdsGroupAdListPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsGroupAdListPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsFilterGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupAdListFragment extends TopAdsAdListFragment<TopAdsGroupAdListPresenter> implements TopAdsEmptyAdDataBinder.Callback {

    public static Fragment createInstance() {
        TopAdsGroupAdListFragment fragment = new TopAdsGroupAdListFragment();
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsGroupAdListPresenterImpl(getActivity(), this);
    }

    @Override
    protected void searchAd() {
        super.searchAd();
        presenter.searchAd(startDate, endDate, keyword, status, page);
    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewBinder() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_empty_group_title_promo_text));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_empty_group_promo_content_text));
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.menu_top_ads_add_promo_group));
        emptyGroupAdsDataBinder.setCallback(this);
        return emptyGroupAdsDataBinder;
    }

    @Override
    public void onClicked(Ad ad) {
        if (ad instanceof GroupAd) {
            Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD, (GroupAd) ad);
            startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
        }
    }

    @Override
    protected void goToFilter() {
        Intent intent = new Intent(getActivity(), TopAdsFilterGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        startActivity(intent);
    }

    public void onEmptyButtonClicked() {
        Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
        this.startActivityForResult(intent, REQUEST_CODE_AD_ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_FILTER && intent != null) {
            status = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
            searchAd();
        }
    }

}