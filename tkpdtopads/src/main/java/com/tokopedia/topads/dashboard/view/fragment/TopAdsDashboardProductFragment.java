package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.R;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordListActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupAdListActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsProductAdListActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsStatisticProductActivity;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardProductFragmentListener;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardProductPresenterImpl;

public class TopAdsDashboardProductFragment extends TopAdsDashboardFragment<TopAdsDashboardProductPresenterImpl> implements TopAdsDashboardProductFragmentListener {

    public static final int REQUEST_CODE_AD_STATUS = 2;

    private LabelView groupSummaryLabelView;
    private LabelView itemSummaryLabelView;
    private LabelView keywordLabelView;

    private int totalProductAd;
    private int totalGroupAd;

    public static TopAdsDashboardProductFragment createInstance() {
        TopAdsDashboardProductFragment fragment = new TopAdsDashboardProductFragment();
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDashboardProductPresenterImpl(getActivity());
        presenter.setTopAdsDashboardFragmentListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_product;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        groupSummaryLabelView = (LabelView) view.findViewById(R.id.label_view_group_summary);
        itemSummaryLabelView = (LabelView) view.findViewById(R.id.label_view_item_summary);
        keywordLabelView = (LabelView) view.findViewById(R.id.label_view_keyword);
        groupSummaryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductGroupClicked();
            }
        });
        itemSummaryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductItemClicked();
            }
        });
        keywordLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onKeywordLabelClicked();
            }
        });
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        totalProductAd = Integer.MIN_VALUE;
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
    }

    public void loadData() {
        super.loadData();
        presenter.populateTotalAd();
    }

    @Override
    public void onTotalAdLoaded(@NonNull TotalAd totalAd) {
        groupSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductGroupAd()));
        itemSummaryLabelView.setContent(String.valueOf(totalAd.getTotalProductAd()));
        keywordLabelView.setContent(String.valueOf(totalAd.getTotalKeyword()));
        totalProductAd = totalAd.getTotalProductAd();
        totalGroupAd = totalAd.getTotalProductGroupAd();
        onLoadDataSuccess();
        showShowCase();
    }

    // use for show case in activity
    public View getDepositView() {
        return getView().findViewById(R.id.view_group_deposit);
    }
    public View getCalendarView() {
        return getView().findViewById(R.id.date_label_view);
    }
    public View getStatisticView() {
        return getView().findViewById(R.id.view_group_statistic);
    }
    public ViewGroup getScrollView() {
        return (ViewGroup) getView().findViewById(R.id.scrollView);
    }

    @Override
    public void onLoadTotalAdError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    private void onProductGroupClicked() {
        UnifyTracking.eventTopAdsProductClickGroupDashboard();
        Intent intent = new Intent(getActivity(), TopAdsGroupAdListActivity.class);
        if (totalProductAd >= 0) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_TOTAL_PRODUCT_ADS, totalProductAd);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    private void onProductItemClicked() {
        UnifyTracking.eventTopAdsProductClickProductDashboard();
        Intent intent = new Intent(getActivity(), TopAdsProductAdListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    private void onKeywordLabelClicked() {
        UnifyTracking.eventTopAdsProductClickKeywordDashboard();
        Intent intent = new Intent(getActivity(), TopAdsKeywordListActivity.class);
        if (totalGroupAd >= 0) {
            intent.putExtra(TopAdsExtraConstant.EXTRA_TOTAL_GROUP_ADS, totalGroupAd);
        }
        startActivityForResult(intent, REQUEST_CODE_AD_STATUS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_AD_STATUS && intent != null) {
            if (startDate == null || endDate == null) {
                return;
            }
            boolean adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
            if (adStatusChanged) {
                loadData();
            }
        }
    }

    @Override
    public void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType) {
        super.onDateChoosen(sDate, eDate, lastSelection, selectionType);
        trackingDateTopAds(lastSelection, selectionType);
    }

    private void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsProductMainPageDateCustom();
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsProductMainPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsProductMainPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsProductMainPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsProductMainPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsProductMainPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStatisticAverageClicked() {
        UnifyTracking.eventTopAdsProductStatisticDashboard(AppEventTracking.EventLabel.STATISTIC_OPTION_AVERAGE_CONVERSION);
        super.onStatisticAverageClicked();
    }

    @Override
    protected void onStatisticClickClicked() {
        UnifyTracking.eventTopAdsProductStatisticDashboard(AppEventTracking.EventLabel.STATISTIC_OPTION_CLICK);
        super.onStatisticClickClicked();
    }

    @Override
    protected void onStatisticConversionClicked() {
        UnifyTracking.eventTopAdsProductStatisticDashboard(AppEventTracking.EventLabel.STATISTIC_OPTION_CONVERSION);
        super.onStatisticConversionClicked();
    }

    @Override
    protected void onStatisticCostClicked() {
        UnifyTracking.eventTopAdsProductStatisticDashboard(AppEventTracking.EventLabel.STATISTIC_OPTION_CPC);
        super.onStatisticCostClicked();
    }

    @Override
    protected void onStatisticCtrClicked() {
        UnifyTracking.eventTopAdsProductStatisticDashboard(AppEventTracking.EventLabel.STATISTIC_OPTION_CTR);
        super.onStatisticCtrClicked();
    }

    @Override
    protected void onStatisticImpressionClicked() {
        UnifyTracking.eventTopAdsProductStatisticDashboard(AppEventTracking.EventLabel.STATISTIC_OPTION_IMPRESSION);
        super.onStatisticImpressionClicked();
    }

    @Override
    protected Class<?> getClassIntentStatistic() {
        return TopAdsStatisticProductActivity.class;
    }
}