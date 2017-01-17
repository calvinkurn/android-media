package com.tokopedia.transaction.purchase.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.purchase.model.AllTxFilter;
import com.tokopedia.transaction.purchase.model.TxFilterItem;
import com.tokopedia.transaction.purchase.utils.FilterUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Kulomady on 6/27/16.
 */
class TxBottomSheetFilterDialog implements AdapterView.OnItemSelectedListener,
        DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    private final Activity activity;
    private final BottomSheetDialog dialog;
    private final Unbinder unbinder;
    private AllTxFilter allTxFilter;
    private List<TxFilterItem> txFilterItemList = new ArrayList<>();

    @BindView(R2.id.filter)
    Spinner spnFilter;
    @BindView(R2.id.search)
    EditText searchField;
    @BindView(R2.id.start_date)
    TextView tvStartDate;
    @BindView(R2.id.end_date)
    TextView tvEndDate;
    @BindView(R2.id.search_button)
    TextView tvSearchSubmit;


    interface OnFilterListener {
        void onFilterSearchButtonClicked(AllTxFilter allTxFilter);
    }

    private OnFilterListener onFilterListener;

    @SuppressLint("InflateParams")
    TxBottomSheetFilterDialog(
            Activity activity,
            AllTxFilter allTxFilter,
            OnFilterListener onFilterListener) {
        this.activity = activity;
        this.allTxFilter = allTxFilter;
        this.onFilterListener = onFilterListener;
        this.dialog = new BottomSheetDialog(activity);
        this.dialog.setContentView(R.layout.dialog_transaction_filter_tx_module);
        unbinder = ButterKnife.bind(this, this.dialog);
        initViewContent(allTxFilter);
        this.dialog.setOnDismissListener(this);
        this.dialog.setOnCancelListener(this);
    }

    private void initViewContent(AllTxFilter allTxFilter) {
        spnFilter.setOnItemSelectedListener(this);
        tvStartDate.setText(allTxFilter.getDateStart());
        tvEndDate.setText(allTxFilter.getDateEnd());
        txFilterItemList = FilterUtils.filterTxAllItems(activity);
        ArrayAdapter<TxFilterItem> adapter
                = new ArrayAdapter<>(activity, R.layout.simple_spinner_tv_res, txFilterItemList);
        spnFilter.setAdapter(adapter);
        setStateFilterSelection(allTxFilter.getFilter());
        searchField.clearFocus();
        KeyboardHandler.DropKeyboard(activity, activity.getCurrentFocus());
    }

    @OnClick(R2.id.start_date)
    void btnStartDateClicked() {
        DatePickerUtil datePicker = new DatePickerUtil(this.activity);
        datePicker.SetDate(
                allTxFilter.getDayStart(),
                allTxFilter.getMonthStart(),
                allTxFilter.getYearStart()
        );
        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int dayOfMonth) {
                allTxFilter.setYearStart(year);
                allTxFilter.setDayStart(dayOfMonth);
                allTxFilter.setMonthStart(month);
                tvStartDate.setText(allTxFilter.getDateStart());
            }
        });
    }

    @OnClick(R2.id.end_date)
    void btnEndDateClicked() {
        DatePickerUtil datePicker = new DatePickerUtil(this.activity);
        datePicker.SetDate(allTxFilter.getDayEnd(), allTxFilter.getMonthEnd(),
                allTxFilter.getYearEnd());
        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {

            @Override
            public void onDateSelected(int year, int month, int dayOfMonth) {
                allTxFilter.setYearEnd(year);
                allTxFilter.setDayEnd(dayOfMonth);
                allTxFilter.setMonthEnd(month);
                tvEndDate.setText(allTxFilter.getDateEnd());
            }
        });
    }

    @OnClick(R2.id.search_button)
    void btnSearchClicked() {
        allTxFilter.setQuery(searchField.getText().toString());
        onFilterListener.onFilterSearchButtonClicked(allTxFilter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedFilter = ((TxFilterItem) parent.getAdapter().getItem(position)).getId();
        allTxFilter.setFilter(selectedFilter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        View view = activity.getCurrentFocus();
        KeyboardHandler.DropKeyboard(activity, view);
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        setStateFilterSelection(allTxFilter.getFilter());
        spnFilter.clearFocus();
        View view = activity.getCurrentFocus();
        KeyboardHandler.DropKeyboard(activity, view);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    void setStateFilterSelection(String txFilterID) {
        int index = 0;
        for (int i = 0; i < txFilterItemList.size(); i++) {
            if (txFilterItemList.get(i).getId().equalsIgnoreCase(txFilterID)) index = i;
        }
        spnFilter.setSelection(index);
    }

    void unbindDialog() {
        unbinder.unbind();
    }
}
