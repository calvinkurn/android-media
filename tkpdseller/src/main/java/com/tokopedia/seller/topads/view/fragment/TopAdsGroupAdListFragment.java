package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.presenter.TopAdsGroupAdListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsGroupAdListPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyGroupAdsDataBinder;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupAdListFragment extends TopAdsAdListFragment<TopAdsGroupAdListPresenter> implements TopAdsEmptyGroupAdsDataBinder.Callback {

    public static Fragment createInstance() {
        TopAdsGroupAdListFragment fragment = new TopAdsGroupAdListFragment();
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsGroupAdListPresenterImpl(context, this);
    }

    @Override
    protected void searchAd() {
        presenter.searchAd(startDate, endDate, keyword, status, page);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        int totalProductAd = getActivity().getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_TOTAL_PRODUCT_ADS, Integer.MIN_VALUE);
        if (totalProductAd >= 0) {
            TopAdsEmptyGroupAdsDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyGroupAdsDataBinder(adapter);
            emptyGroupAdsDataBinder.setEmptyGroupContentText(getString(R.string.top_ads_empty_group_promo_content_not_empty_text));
            emptyGroupAdsDataBinder.setEmptyGroupContentItemText(getString(R.string.top_ads_empty_group_promo_content_item_no_text, String.valueOf(totalProductAd)));
            emptyGroupAdsDataBinder.setCallback(this);
            adapter.setEmptyView(emptyGroupAdsDataBinder);
        }
    }

    @Override
    public void onClicked(Ad ad) {
        if(ad instanceof GroupAd){
            Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD, (GroupAd) ad);
            startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
        }
    }

    @Override
    public void onEmptyGroupContentItemTextClicked() {
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        startActivity(intent);
    }
}