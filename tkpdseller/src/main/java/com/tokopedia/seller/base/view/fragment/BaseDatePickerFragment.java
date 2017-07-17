package com.tokopedia.seller.base.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.base.di.component.DaggerDatePickerComponent;
import com.tokopedia.seller.base.di.component.DatePickerComponent;
import com.tokopedia.seller.base.di.module.DatePickerModule;
import com.tokopedia.seller.base.view.constant.ConstantView;
import com.tokopedia.seller.base.view.listener.DatePickerView;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.lib.widget.DateLabelView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAdListFragment}
 */

public abstract class BaseDatePickerFragment extends BaseDaggerFragment implements DatePickerView {

    public static final String REQUEST_DATE_FORMAT = "yyyy-MM-dd";
    @Inject
    public DatePickerPresenter datePickerPresenter;
    protected DateLabelView dateLabelView;
    protected String startDate;
    protected String endDate;
    protected DatePickerViewModel datePickerViewModel;
    protected DatePickerComponent datePickerComponent;

    protected abstract void loadData();

    protected abstract Intent getDatePickerIntent();

    @Override
    protected void initInjector() {
        datePickerComponent = DaggerDatePickerComponent
                .builder()
                .datePickerModule(new DatePickerModule())
                .appComponent(getComponent(AppComponent.class))
                .build();

        datePickerComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        datePickerPresenter.fetchDatePickerSetting();
    }

    @Override
    public void onSuccessLoadDatePicker(DatePickerViewModel datePickerViewModel) {
        if (TextUtils.isEmpty(datePickerViewModel.getStartDate()) ||
                TextUtils.isEmpty(datePickerViewModel.getEndDate()) ||
                TextUtils.isEmpty(startDate) ||
                TextUtils.isEmpty(endDate) ||
                !startDate.equalsIgnoreCase(datePickerViewModel.getStartDate()) ||
                !endDate.equalsIgnoreCase(datePickerViewModel.getEndDate())) {
            startDate = datePickerViewModel.getStartDate();
            endDate = datePickerViewModel.getEndDate();
            this.datePickerViewModel = datePickerViewModel;
            loadData();
        }
    }

    @Override
    public void onErrorLoadDatePicker(Throwable throwable) {
        loadData();
    }

    @Override
    public void onSuccessSaveDatePicker() {
        // Do nothing
    }

    @Override
    public void onErrorSaveDatePicker(Throwable throwable) {
        // Do nothing
    }

    @Override
    public void onSuccessClearDatePicker() {
        // Do nothing
    }

    @Override
    public void onErrorClearDatePicker(Throwable throwable) {
        // Do nothing
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ConstantView.REQUEST_CODE_DATE && intent != null) {
            long sDate = intent.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
            long eDate = intent.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
            int lastSelection = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1);
            int selectionType = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
            if (sDate > 0 && eDate > 0) {
                onDateSelected(sDate, eDate, lastSelection, selectionType);
            }
        }
    }

    public void onDateSelected(long sDate, long eDate, int lastSelection, int selectionType) {
        datePickerViewModel.setStartDate(new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(new Date(sDate)));
        datePickerViewModel.setEndDate(new SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(new Date(eDate)));
        datePickerViewModel.setDatePickerSelection(lastSelection);
        datePickerViewModel.setDatePickerType(selectionType);
        datePickerPresenter.saveDateSetting(datePickerViewModel);
        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            return;
        }
        loadData();
    }

    protected void openDatePicker() {
        startActivityForResult(getDatePickerIntent(), ConstantView.REQUEST_CODE_DATE);
    }
}