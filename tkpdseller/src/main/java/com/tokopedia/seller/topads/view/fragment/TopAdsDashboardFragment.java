package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardPresenter;
import com.tokopedia.seller.topads.view.activity.SetDateActivity;
import com.tokopedia.seller.topads.view.activity.SetDateFragment;
import com.tokopedia.seller.topads.view.activity.TopAdsAddCreditActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;
import com.tokopedia.seller.topads.view.widget.TopAdsStatisticLabelView;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class TopAdsDashboardFragment<T extends TopAdsDashboardPresenter> extends BasePresenterFragment<T> implements TopAdsDashboardFragmentListener {

    private static final int REQUEST_CODE_DATE = 0;


    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R2.id.image_view_shop_icon)
    ImageView shopIconImageView;
    @BindView(R2.id.text_view_shop_title)
    TextView shopTitleTextView;
    @BindView(R2.id.text_view_deposit_desc)
    TextView depositDescTextView;

    @BindView(R2.id.text_view_range_date)
    TextView rangeDateDescTextView;

    @BindView(R2.id.statistic_label_view_impression)
    TopAdsStatisticLabelView impressionStatisticLabelView;
    @BindView(R2.id.statistic_label_view_click)
    TopAdsStatisticLabelView clickStatisticLabelView;
    @BindView(R2.id.statistic_label_view_ctr)
    TopAdsStatisticLabelView ctrStatisticLabelView;
    @BindView(R2.id.statistic_label_view_conversion)
    TopAdsStatisticLabelView conversionStatisticLabelView;
    @BindView(R2.id.statistic_label_view_average)
    TopAdsStatisticLabelView averageStatisticLabelView;
    @BindView(R2.id.statistic_label_view_cost)
    TopAdsStatisticLabelView costStatisticLabelView;

    protected Date startDate;
    protected Date endDate;

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected void initView(View view) {
        depositDescTextView.setText(getString(R.string.label_top_ads_deposit_desc, getString(R.string.top_ads_statistic_info_default_value)));
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        RefreshHandler refresh = new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                loadData();
            }
        });
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter.isDateUpdated(startDate, endDate)) {
            startDate = presenter.getStartDate();
            endDate = presenter.getEndDate();
            loadData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_DATE && intent != null) {
            long startDateTime = intent.getLongExtra(SetDateFragment.START_DATE, -1);
            long endDateTime = intent.getLongExtra(SetDateFragment.END_DATE, -1);
            if (startDateTime > 0 && endDateTime > 0) {
                presenter.saveDate(new Date(startDateTime), new Date(endDateTime));
            }
        }
    }

    protected void loadData() {
        swipeToRefresh.setRefreshing(true);
        rangeDateDescTextView.setText(presenter.getRangeDateFormat(startDate, endDate));
        presenter.populateSummary(startDate, endDate);
        presenter.populateDeposit();
        presenter.populateShopInfo();
    }

    @Override
    public void onSummaryLoaded(@NonNull Summary summary) {
        updateSummaryLayout(summary);
        hideLoading();
    }

    private void updateSummaryLayout(Summary summary) {
        impressionStatisticLabelView.setContent(String.valueOf(summary.getImpressionSumFmt()));
        clickStatisticLabelView.setContent(String.valueOf(summary.getClickSumFmt()));
        ctrStatisticLabelView.setContent(String.valueOf(summary.getCtrPercentageFmt()));
        conversionStatisticLabelView.setContent(String.valueOf(summary.getConversionSumFmt()));
        averageStatisticLabelView.setContent(String.valueOf(summary.getCostAvgFmt()));
        costStatisticLabelView.setContent(String.valueOf(summary.getCostSumFmt()));
    }

    @Override
    public void onLoadSummaryError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    @Override
    public void onDepositTopAdsLoaded(@NonNull DataDeposit dataDeposit) {
        depositDescTextView.setText(getString(R.string.label_top_ads_deposit_desc, dataDeposit.getAmountFmt()));
        hideLoading();
    }

    @Override
    public void onLoadDepositTopAdsError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    @Override
    public void onShopDetailLoaded(@NonNull ShopModel shopModel) {
        ImageHandler.loadImageCircle2(getActivity(), shopIconImageView, shopModel.info.shopAvatar);
        shopTitleTextView.setText(shopModel.info.shopName);
        hideLoading();
    }

    @Override
    public void onLoadShopDetailError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    protected void showNetworkError() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadData();
            }
        }).showRetrySnackbar();
    }

    protected void hideLoading() {
        swipeToRefresh.setRefreshing(false);
    }

    @OnClick(R2.id.layout_date)
    void onDateLayoutClicked() {
        Intent moveToSetDate = new Intent(getActivity(), SetDateActivity.class);
        moveToSetDate.putExtra(SetDateActivity.IS_GOLD_MERCHANT, true);
//        moveToSetDate.putExtra(SetDateActivity.SELECTION_PERIOD, lastSelection);
//        moveToSetDate.putExtra(SetDateActivity.SELECTION_TYPE, selectionType);
        moveToSetDate.putExtra(SetDateActivity.CUSTOM_START_DATE, startDate.getTime());
        moveToSetDate.putExtra(SetDateActivity.CUSTOM_END_DATE, endDate.getTime());
        startActivityForResult(moveToSetDate, REQUEST_CODE_DATE);
    }

    @OnClick(R2.id.statistic_label_view_impression)
    void onStaticImpressionClicked() {
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.statistic_label_view_click)
    void onStatisticClickClicked() {
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.statistic_label_view_ctr)
    void onStatisticImpressionClicked() {
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.statistic_label_view_conversion)
    void onStatisticConversionClicked() {
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.statistic_label_view_average)
    void onStatisticAverageClicked() {
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.statistic_label_view_cost)
    void onStatisticCostClicked() {
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.image_button_add_deposit)
    void goToAddCredit() {
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivity(intent);
    }
}