package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.BulkAction;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditGroupMainPageActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailGroupPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailGroupViewPresenterImpl;

/**
 * Created by zulfikarrahman on 1/3/17.
 */

public class TopAdsDetailGroupFragment extends TopAdsDetailStatisticFragment<TopAdsDetailGroupPresenter, GroupAd> {

    public static final String GROUP_AD_PARCELABLE = "GROUP_AD_PARCELABLE";
    private LabelView items;
    private OnTopAdsDetailGroupListener listener;

    public static Fragment createInstance(GroupAd groupAd, String adId, boolean forceRefresh) {
        Fragment fragment = new TopAdsDetailGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, groupAd);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        bundle.putBoolean(TopAdsExtraConstant.EXTRA_FORCE_REFRESH, forceRefresh);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        items = (LabelView) view.findViewById(R.id.items);
        initNameAndItemsView();
    }

    private void initNameAndItemsView() {
        name.setTitle(getString(R.string.label_top_ads_groups));
        name.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.font_black_secondary_54));
        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductItemClicked();
            }
        });
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailGroupViewPresenterImpl(getActivity(), this, new TopAdsGroupAdInteractorImpl(getActivity()));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_group_detail;
    }

    @Override
    protected void turnOnAd() {
        super.turnOnAd();
        presenter.turnOnAds(ad.getId());
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        presenter.turnOffAds(ad.getId());
    }

    @Override
    protected void refreshAd() {
        if (ad != null ) {
            presenter.refreshAd(startDate, endDate, ad.getId());
        } else {
            presenter.refreshAd(startDate, endDate, adId);
        }
    }

    @Override
    protected void editAd() {
        Intent intent = TopAdsEditGroupMainPageActivity.createIntent(getActivity(), null, ad.getId(), isForceRefresh);
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    protected void deleteAd() {
        super.deleteAd();
        UnifyTracking.eventTopAdsProductDeleteGrup();
        presenter.deleteAd(ad.getId());
    }

    @Override
    public void onTurnOffAdSuccess(BulkAction dataResponseActionAds) {
        fillToAdObject(dataResponseActionAds);
        super.onTurnOffAdSuccess(dataResponseActionAds);
    }

    private void fillToAdObject(BulkAction dataResponseActionAds) {
        if(dataResponseActionAds != null && dataResponseActionAds instanceof GroupAdBulkAction) {
            Integer status = Integer.valueOf(((GroupAdBulkAction) dataResponseActionAds).getAdList().get(0).getStatus());

            CommonUtils.dumper("status from network -> "+status);
            if(adFromIntent != null)
                adFromIntent.setStatus(status);

            if(ad != null)
                ad.setStatus(status);
        }
    }

    @Override
    public void onTurnOnAdSuccess(BulkAction dataResponseActionAds) {
        fillToAdObject(dataResponseActionAds);
        super.onTurnOnAdSuccess(dataResponseActionAds);
    }

    @Override
    public void onAdLoaded(GroupAd ad) {
        super.onAdLoaded(ad);
        if (listener != null) {
            listener.startShowCase();
        }
    }

    @Override
    protected GroupAd fillFromPrevious(GroupAd current, GroupAd previous) {
        if(previous != null && previous.getDatum() != null) {
            current.setDatum(previous.getDatum());
        }
        return super.fillFromPrevious(current, previous);
    }

    @Override
    protected void updateMainView(GroupAd ad) {
        super.updateMainView(ad);
        items.setContent(getString(R.string.top_ads_label_count_product_group, ad.getTotalItem()));
        if (ad.getTotalItem() > 0) {
            items.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
        }
    }

    @Override
    public void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context instanceof OnTopAdsDetailGroupListener) {
            listener = (OnTopAdsDetailGroupListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    void onProductItemClicked() {
        if (ad != null) {
            Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_GROUP, ad);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            showDeleteConfirmation(getString(R.string.title_delete_group), getString(R.string.top_ads_delete_group_alert));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType) {
        super.onDateChoosen(sDate, eDate, lastSelection, selectionType);
        trackingDateTopAds(lastSelection, selectionType);
    }

    private void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsProductDetailGroupPageDateCustom();
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsProductDetailGroupPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsProductDetailGroupPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsProductDetailGroupPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsProductDetailGroupPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsProductDetailGroupPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
                    break;
                default:
                    break;
            }
        }
    }

    // for show case
    public View getStatusView() {
        return getView().findViewById(R.id.status);
    }

    public View getProductView() {
        return items;
    }

    public interface OnTopAdsDetailGroupListener {
        void startShowCase();
    }
}