package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.GMStatisticTransactionTableAdapter;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.model.GMStatisticTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTablePresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTableView;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseListFragment;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * @author normansyahputa on 7/13/17.
 */
public class GMStatisticTransactionTableFragment extends TopAdsBaseListFragment<GMStatisticTransactionTablePresenter, GMStatisticTransactionTableModel>
        implements GMStatisticTransactionTableView {
    public static final String TAG = "GMStatisticTransactionT";

    @Inject
    GMStatisticTransactionTablePresenter presenter;

    public static Fragment createInstance(long startDate, long endDate) {
        Bundle bundle = new Bundle();
        bundle.putLong(GMStatisticTransactionTableView.START_DATE, startDate);
        bundle.putLong(GMStatisticTransactionTableView.END_DATE, endDate);
        GMStatisticTransactionTableFragment fragment = new GMStatisticTransactionTableFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void searchData() {
        super.searchData();
        presenter.loadData(startDate, endDate);
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerGMTransactionComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .build().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null && getArguments() != null) {
            if (getArguments().getLong(GMStatisticTransactionTableView.START_DATE, -1) != -1) {
                startDate = getDate(getArguments().getLong(GMStatisticTransactionTableView.START_DATE));
                dumpStartDateLong();
            }
            if (getArguments().getLong(GMStatisticTransactionTableView.END_DATE, -1) != -1) {
                endDate = getDate(getArguments().getLong(GMStatisticTransactionTableView.END_DATE));
                dumpEndDateLong();
            }
        }
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private Date getDate(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar.getTime();
    }

    public void dumpStartDateLong() {
        Log.d(TAG, "dumpStartDateLong : " + startDate.toString());
    }

    public void dumpEndDateLong() {
        Log.d(TAG, "dumpEndDateLong : " + endDate.toString());
    }

    @Override
    public void onItemClicked(GMStatisticTransactionTableModel gmStatisticTransactionTableModel) {

    }

    @Override
    protected BaseListAdapter getNewAdapter() {
        return new GMStatisticTransactionTableAdapter();
    }
}
