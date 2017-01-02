package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.Logger;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardPresenter;
import com.tokopedia.seller.topads.view.activity.TopAdsAddCreditActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class TopAdsDashboardFragment<T extends TopAdsDashboardPresenter> extends BasePresenterFragment<T> implements TopAdsDashboardFragmentListener {

    private static String TAG = TopAdsDashboardFragment.class.getSimpleName();

    private static final String RANGE_DATE_FORMAT = "dd MMM yyyy";

    @BindView(R2.id.image_view_shop_icon)
    ImageView shopIconImageView;
    @BindView(R2.id.text_view_shop_title)
    TextView shopTitleTextView;
    @BindView(R2.id.text_view_deposit_desc)
    TextView depositDescTextView;

    @BindView(R2.id.text_view_range_date)
    TextView rangeDateDescTextView;

    @BindView(R2.id.layout_top_ads_info_text_impression)
    View impressionInfoLayout;
    @BindView(R2.id.layout_top_ads_info_text_click)
    View clickInfoLayout;
    @BindView(R2.id.layout_top_ads_info_text_ctr)
    View ctrInfoLayout;
    @BindView(R2.id.layout_top_ads_info_text_conversion)
    View conversionInfoLayout;
    @BindView(R2.id.layout_top_ads_info_text_average)
    View averageMainInfoLayout;
    @BindView(R2.id.layout_top_ads_info_text_cost)
    View costInfoLayout;

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
        initialLayout();
    }

    @Override
    public void onSummaryLoaded(@NonNull Summary summary) {
        Logger.i(TAG, "Cost Summary: " + summary.getCostSum());
        updateSummaryLayout(summary);
    }

    @Override
    public void onLoadSummaryError(@NonNull Throwable throwable) {

    }

    @Override
    public void onDepositTopAdsLoaded(@NonNull DataDeposit dataDeposit) {
        depositDescTextView.setText(getString(R.string.label_top_ads_deposit_desc, dataDeposit.getAmountFmt()));
    }

    @Override
    public void onLoadDepositTopAdsError(@NonNull Throwable throwable) {

    }

    @Override
    public void onShopDetailLoaded(@NonNull ShopModel shopModel) {
        ImageHandler.loadImageCircle2(getActivity(), shopIconImageView, shopModel.info.shopAvatar);
        shopTitleTextView.setText(shopModel.info.shopName);
    }

    @Override
    public void onLoadShopDetailError(@NonNull Throwable throwable) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        presenter.resetDate();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void initialLayout() {
        updateInfoText(impressionInfoLayout, R.id.text_view_title, String.valueOf(getString(R.string.label_top_ads_impression)));
        updateInfoText(clickInfoLayout, R.id.text_view_title, String.valueOf(getString(R.string.label_top_ads_click)));
        updateInfoText(ctrInfoLayout, R.id.text_view_title, String.valueOf(getString(R.string.label_top_ads_ctr)));
        updateInfoText(conversionInfoLayout, R.id.text_view_title, String.valueOf(getString(R.string.label_top_ads_conversion)));
        updateInfoText(averageMainInfoLayout, R.id.text_view_title, String.valueOf(getString(R.string.label_top_ads_average)));
        updateInfoText(costInfoLayout, R.id.text_view_title, String.valueOf(getString(R.string.label_top_ads_cost)));
    }

    private void updateSummaryLayout(Summary summary) {
        updateInfoText(impressionInfoLayout, R.id.text_view_content, String.valueOf(summary.getImpressionSum()));
        updateInfoText(clickInfoLayout, R.id.text_view_content, String.valueOf(summary.getClickSum()));
        updateInfoText(ctrInfoLayout, R.id.text_view_content, String.valueOf(summary.getCtrPercentage()));
        updateInfoText(conversionInfoLayout, R.id.text_view_content, String.valueOf(summary.getConversionSum()));
        updateInfoText(averageMainInfoLayout, R.id.text_view_content, String.valueOf(summary.getCostAvg()));
        updateInfoText(costInfoLayout, R.id.text_view_content, String.valueOf(summary.getCostSum()));
    }

    protected void updateInfoText(View layout, int resourceId, String value) {
        ((TextView) layout.findViewById(resourceId)).setText(value);
    }

    protected void loadData() {
        startDate = presenter.getStartDate();
        endDate = presenter.getEndDate();
        updateRangeDate();
        presenter.populateSummary(startDate, endDate);
        presenter.populateDeposit();
        presenter.populateShopInfo();
    }

    protected void updateRangeDate() {
        rangeDateDescTextView.setText(getString(R.string.top_ads_range_date_text,
                new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(startDate),
                new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(endDate)));
    }

    @OnClick(R2.id.image_button_add_deposit)
    void goToAddCredit() {
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivity(intent);
    }
}