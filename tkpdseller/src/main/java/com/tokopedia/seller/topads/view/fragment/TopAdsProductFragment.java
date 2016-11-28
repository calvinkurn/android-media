package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.Logger;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.presenter.TopAdsProductFragmentPresenterImpl;
import com.tokopedia.seller.topads.view.listener.TopAdsProductFragmentListener;

import java.util.Calendar;

import butterknife.Bind;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsProductFragment extends BasePresenterFragment<TopAdsProductFragmentPresenterImpl> implements TopAdsProductFragmentListener {

    private static String TAG = TopAdsProductFragment.class.getSimpleName();

    @Bind(R2.id.image_view_shop_icon)
    ImageView shopIconImageView;
    @Bind(R2.id.text_view_shop_title)
    TextView shopTitleTextView;
    @Bind(R2.id.text_view_deposit_desc)
    TextView depositDescTextView;

    @Bind(R2.id.layout_top_ads_info_text_impression)
    View impressionInfoLayout;
    @Bind(R2.id.layout_top_ads_info_text_click)
    View clickInfoLayout;
    @Bind(R2.id.layout_top_ads_info_text_ctr)
    View ctrInfoLayout;
    @Bind(R2.id.layout_top_ads_info_text_conversion)
    View conversionInfoLayout;
    @Bind(R2.id.layout_top_ads_info_text_average)
    View averageMainInfoLayout;
    @Bind(R2.id.layout_top_ads_info_text_cost)
    View costInfoLayout;

    public static TopAdsProductFragment createInstance() {
        TopAdsProductFragment fragment = new TopAdsProductFragment();
        return fragment;
    }

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
    protected void initialPresenter() {
        presenter = new TopAdsProductFragmentPresenterImpl(getActivity());
        presenter.setTopAdsProductFragmentListener(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_product;
    }

    @Override
    protected void initView(View view) {
        initialLayout();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_YEAR, 3);
        presenter.populateSummary(startCalendar.getTime(), endCalendar.getTime());
        presenter.populateDeposit();
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
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

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

    private void updateInfoText(View layout, int resourceId, String value) {
        ((TextView) layout.findViewById(resourceId)).setText(value);
    }
}