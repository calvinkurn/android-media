package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tkpd.library.utils.Logger;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.presenter.TopAdsProductFragmentPresenterImpl;
import com.tokopedia.seller.topads.view.listener.TopAdsProductFragmentListener;

import java.util.Calendar;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsProductFragment extends BasePresenterFragment<TopAdsProductFragmentPresenterImpl> implements TopAdsProductFragmentListener {

    private static String TAG = TopAdsProductFragment.class.getSimpleName();

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
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_YEAR, 3);
        presenter.populateSummary(startCalendar.getTime(), endCalendar.getTime());
    }

    @Override
    public void onSummaryLoaded(@NonNull Summary summary) {
        Logger.i(TAG, "Cost Summary: " + summary.getCostSum());
    }

    @Override
    public void onLoadSummaryError(@NonNull Throwable throwable) {

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
}