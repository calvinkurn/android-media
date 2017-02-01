package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.presenter.TopAdsProductAdListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsProductAdListPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsFilterGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsFilterProductActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

/**
 * Created by zulfikarrahman on 12/16/16.
 */

public class TopAdsProductAdListFragment extends TopAdsAdListFragment<TopAdsProductAdListPresenter> {

    private int group;

    public static Fragment createInstance(int group) {
        TopAdsProductAdListFragment fragment = new TopAdsProductAdListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_GROUP, group);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onFirstTimeLaunched() {
        super.onFirstTimeLaunched();
        group = getArguments().getInt(TopAdsExtraConstant.EXTRA_GROUP);
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsProductAdListPresenterImpl(context, this);
    }

    @Override
    protected void searchAd() {
        presenter.searchAd(startDate, endDate, keyword, status, group, page);
    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewBinder() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyAdDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_empty_product_title_promo_text));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_empty_product_promo_content_empty_text));
        return emptyGroupAdsDataBinder;
    }

    @Override
    public void onClicked(Ad ad) {
        if(ad instanceof ProductAd){
            Intent intent = new Intent(getActivity(), TopAdsDetailProductActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD, (ProductAd) ad);
            startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
        }
    }

    @Override
    protected void goToFilter() {
        Intent intent = new Intent(getActivity(), TopAdsFilterProductActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_STATUS_VALUE, status);
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_FILTER && intent != null) {
            status = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_STATUS_VALUE, status);
            searchAd();
        }
    }
}