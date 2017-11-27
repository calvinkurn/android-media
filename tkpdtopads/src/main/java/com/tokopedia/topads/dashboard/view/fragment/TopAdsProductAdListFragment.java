package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsFilterProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyProductAdDataBinder;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsProductAdListPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsProductAdListPresenterImpl;

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
        presenter.searchAd(startDate, endDate, keyword, status, groupId, getCurrentPage());
    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyProductAdDataBinder(adapter);
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
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, ad.getId());
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD, ad);
            intent.putExtra(TopAdsExtraConstant.EXTRA_FORCE_REFRESH, true);
            startActivityForResult(intent, REQUEST_CODE_AD_CHANGE);
        }
    }


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
            resetPageAndSearch();
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
        UnifyTracking.eventTopAdsProductNewPromoProduct();
        Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
        this.startActivityForResult(intent, REQUEST_CODE_AD_ADD);
    }

    @Override
    public void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType) {
        super.onDateChoosen(sDate, eDate, lastSelection, selectionType);
        trackingDateTopAds(lastSelection, selectionType);
    }

    private void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsProductPageProductDateCustom();
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsProductPageProductDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
                    break;
                default:
                    break;
            }
        }
    }
}