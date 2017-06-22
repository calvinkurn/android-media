package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.constant.ConstantView;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.lib.datepicker.DatePickerResultListener;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsListViewListener;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsRetryDataBinder;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.widget.DividerItemDecoration;

import java.util.Date;
import java.util.List;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.view.fragment.TopAdsAdListFragment}
 */

public abstract class TopAdsBaseListFragment<T, U> extends BaseListFragment<T, U> implements
        DatePickerResultListener.DatePickerResult {

    protected Date startDate;
    protected Date endDate;
    protected DatePickerResultListener datePickerResultListener;

    @Nullable
    protected TopAdsDatePickerPresenter datePickerPresenter;

    protected TopAdsDatePickerPresenter getDatePickerPresenter() {
        return new TopAdsDatePickerPresenterImpl(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    protected void initialPresenter() {
        super.initialPresenter();
        datePickerPresenter = getDatePickerPresenter();
        datePickerResultListener = new DatePickerResultListener(this, ConstantView.REQUEST_CODE_DATE);
    }

    @Override
    protected void setActionVar() {
        // Avoid load date at first time
    }

    @Override
    public void onResume() {
        super.onResume();
        if (datePickerPresenter!= null && datePickerPresenter.isDateUpdated(startDate, endDate)) {
            startDate = datePickerPresenter.getStartDate();
            endDate = datePickerPresenter.getEndDate();
            loadData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (datePickerResultListener != null) {
            datePickerResultListener.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType) {
        datePickerPresenter.saveDate(new Date(sDate), new Date(eDate));
        datePickerPresenter.saveSelectionDatePicker(selectionType, lastSelection);
        if (startDate == null || endDate == null) {
            return;
        }
        loadData();
    }

    protected void openDatePicker() {
        Intent intent = datePickerPresenter.getDatePickerIntent(getActivity(), startDate, endDate);
        startActivityForResult(intent, ConstantView.REQUEST_CODE_DATE);
    }

    @Override
    protected void searchData(int page) {
        this.page = page;
        if (startDate == null || endDate == null) {
            return;
        }
        searchData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        datePickerResultListener = null;
    }
}