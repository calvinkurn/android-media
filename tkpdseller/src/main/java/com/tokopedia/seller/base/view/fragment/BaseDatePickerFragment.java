package com.tokopedia.seller.base.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.DatePicker;
import com.tokopedia.seller.base.view.listener.DatePickerView;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.seller.common.datepicker.utils.DatePickerUtils;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.design.label.DateLabelView;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAdListFragment}
 */

public abstract class BaseDatePickerFragment extends BaseDaggerFragment implements DatePicker, DatePickerView {

    protected DateLabelView dateLabelView;

    @Inject
    public DatePickerPresenter datePickerPresenter;
    private DatePickerViewModel datePickerViewModel;

    protected long getStartDate(){
        return datePickerViewModel.getStartDate();
    }

    protected long getEndDate() {
        return datePickerViewModel.getEndDate();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        datePickerPresenter.attachView(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadDataForDate();
    }

    protected void disableDateLabelView(){
        if (dateLabelView!= null) {
            dateLabelView.setEnabled(false);
        }
    }

    protected void enableDateLabelView(){
        if (dateLabelView!= null) {
            dateLabelView.setEnabled(true);
        }
    }

    @Override
    public void reloadDataForDate() {
        // important to reattach here, or the presenter will attach to the previous fragment when onResume
        datePickerPresenter.attachView(this);
        datePickerPresenter.fetchDatePickerSetting();
    }

    @Override
    public void onSuccessLoadDatePicker(DatePickerViewModel datePickerViewModel) {
        if (! datePickerViewModel.equal(this.datePickerViewModel)) {
            this.datePickerViewModel = datePickerViewModel;
            loadDataByDate(datePickerViewModel);
            setDateLabelView(datePickerViewModel);
        }
    }

    protected void setDateLabelView(DatePickerViewModel datePickerViewModel) {
        dateLabelView.setDate(datePickerViewModel.getStartDate(),
                datePickerViewModel.getEndDate());
    }

    public abstract void loadDataByDate(DatePickerViewModel datePickerViewModel);

    @Override
    public void onErrorLoadDatePicker(Throwable throwable) {
        if (datePickerViewModel == null) {
            datePickerViewModel = getDefaultDateViewModel();
        } else {
            datePickerPresenter.saveDateSetting(datePickerViewModel);
        }
        setDateLabelView(datePickerViewModel);
        loadDataByDate(datePickerViewModel);
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
        if (requestCode == DatePickerConstant.REQUEST_CODE_DATE && intent != null) {
            long startDate = intent.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
            long endDate = intent.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
            if (startDate > 0 && endDate > 0) {
                onDateSelected(intent);
            }
        }
    }

    private void onDateSelected(Intent intent) {
        DatePickerViewModel datePickerViewModel = DatePickerUtils.convertDatePickerFromIntent(intent);
        datePickerViewModel = revisitExtraIntent(intent, datePickerViewModel, this.datePickerViewModel);
        if (datePickerViewModel!= null && !datePickerViewModel.equal(this.datePickerViewModel)) {
            datePickerPresenter.saveDateSetting(datePickerViewModel);
        }
    }

    /**
     * if there are any intent extra to be put on datepickerViewModel, add it here
     */
    public DatePickerViewModel revisitExtraIntent(Intent intent, DatePickerViewModel datePickerViewModel,
                                                  DatePickerViewModel prevDatePickerViewModel){
        return datePickerViewModel;
    }

    @Override
    public void openDatePicker() {
        startActivityForResult(getDatePickerIntent(datePickerViewModel), DatePickerConstant.REQUEST_CODE_DATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        datePickerPresenter.detachView();
    }
}