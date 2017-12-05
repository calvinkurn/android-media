package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsFilterGroupActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;
import com.tokopedia.topads.dashboard.view.adapter.viewholder.TopAdsEmptyGroupAdDataBinder;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGroupAdListPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGroupAdListPresenterImpl;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupAdListFragment extends TopAdsAdListFragment<TopAdsGroupAdListPresenter, Ad> implements TopAdsEmptyAdDataBinder.Callback {

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
    protected void searchForPage(int page) {
        presenter.searchAd(startDate, endDate, keyword, status, getCurrentPage());
    }

    @Override
    protected TopAdsEmptyAdDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyAdDataBinder emptyGroupAdsDataBinder = new TopAdsEmptyGroupAdDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.top_ads_empty_group_title_promo_text));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.top_ads_empty_group_promo_content_text));
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.menu_top_ads_add_promo_group));
        emptyGroupAdsDataBinder.setCallback(this);
        return emptyGroupAdsDataBinder;
    }

    @Override
    public void onItemClicked(Ad ad) {
        if (ad instanceof GroupAd) {
            Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, ad.getId());
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD, ad);
            startActivityForResult(intent, REQUEST_CODE_AD_CHANGE);
        }
    }

    @Override
    public void goToFilter() {
        Intent intent = new Intent(getActivity(), TopAdsFilterGroupActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
        startActivityForResult(intent, REQUEST_CODE_AD_FILTER);
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEmptyButtonClicked() {
        onCreateAd();
    }

    @Override
    public void onCreateAd() {
        UnifyTracking.eventTopAdsProductNewPromoGroup();
        Intent intent = new Intent(getActivity(), TopAdsGroupNewPromoActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AD_ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_FILTER && intent != null) {
            status = intent.getIntExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_STATUS, status);
            resetPageAndSearch();
        }
    }

    @Override
    public void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType) {
        super.onDateChoosen(sDate, eDate, lastSelection, selectionType);
        trackingDateTopAds(lastSelection, selectionType);
    }

    private void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsProductPageGroupDateCustom();
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsProductPageGroupDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
                    break;
                default:
                    break;
            }
        }
    }

    // use for showcase
    public View getDateView(){
        return getView().findViewById(R.id.date_label_view);
    }

}