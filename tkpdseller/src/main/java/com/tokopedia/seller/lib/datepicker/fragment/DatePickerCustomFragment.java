package com.tokopedia.seller.lib.datepicker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.DatePickerUtils;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.lib.datepicker.widget.DatePickerLabelView;

import java.util.Calendar;

/**
 * Created by Nathaniel on 1/16/2017.
 */

public class DatePickerCustomFragment extends TkpdFragment {

    public interface Callback {

        void onDateSubmitted(long startDate, long endDate);

    }

    private DatePickerLabelView startDatePickerLabelView;
    private DatePickerLabelView endDatePickerLabelView;
    private Button submitButton;
    private long startDate;
    private long endDate;
    private long minStartDate;
    private long maxEndDate;
    private int maxDateRange;
    private Callback callback;

    public static DatePickerCustomFragment newInstance(long sDate, long eDate, long minStartDate, long maxEndDate, int maxDateRange) {
        DatePickerCustomFragment fragment = new DatePickerCustomFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(DatePickerConstant.EXTRA_START_DATE, sDate);
        bundle.putLong(DatePickerConstant.EXTRA_END_DATE, eDate);
        bundle.putLong(DatePickerConstant.EXTRA_MIN_START_DATE, minStartDate);
        bundle.putLong(DatePickerConstant.EXTRA_MAX_END_DATE, maxEndDate);
        bundle.putInt(DatePickerConstant.EXTRA_MAX_DATE_RANGE, maxDateRange);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker_custom, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            startDate = bundle.getLong(DatePickerConstant.EXTRA_START_DATE, -1);
            endDate = bundle.getLong(DatePickerConstant.EXTRA_END_DATE, -1);
            minStartDate = bundle.getLong(DatePickerConstant.EXTRA_MIN_START_DATE, -1);
            maxEndDate = bundle.getLong(DatePickerConstant.EXTRA_MAX_END_DATE, -1);
            maxDateRange = bundle.getInt(DatePickerConstant.EXTRA_MAX_DATE_RANGE, -1);
        }
        startDatePickerLabelView = (DatePickerLabelView) view.findViewById(R.id.date_picker_start_date);
        endDatePickerLabelView = (DatePickerLabelView) view.findViewById(R.id.date_picker_end_date);
        startDatePickerLabelView.setOnContentClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDatePicker();
            }
        });
        endDatePickerLabelView.setOnContentClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndDatePicker();
            }
        });
        submitButton = (Button) view.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDate();
            }
        });
        updateDateView();
    }

    private void openStartDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        DatePickerUtil datePicker = new DatePickerUtil(getActivity(),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR)
        );
        datePicker.setMinDate(minStartDate);
        datePicker.setMaxDate(maxEndDate);
        datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear - 1, dayOfMonth);
                updateStartDate(calendar.getTimeInMillis());
            }

            private void updateStartDate(long selectedDate) {
                startDate = selectedDate;
                Calendar endDateCalendar = Calendar.getInstance();
                endDateCalendar.setTimeInMillis(selectedDate);
                endDateCalendar.add(Calendar.DATE, maxDateRange);
                if (endDate > endDateCalendar.getTimeInMillis()) {
                    endDate = endDateCalendar.getTimeInMillis();
                }
                if (endDate > maxEndDate) {
                    endDate = maxEndDate;
                }
                if (endDate < startDate) {
                    endDate = startDate;
                }
                updateDateView();
            }
        });
    }

    private void openEndDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endDate);
        DatePickerUtil datePicker = new DatePickerUtil(getActivity(),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR)
        );
        datePicker.setMinDate(minStartDate);
        datePicker.setMaxDate(maxEndDate);
        datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear - 1, dayOfMonth);
                updateStartDate(calendar.getTimeInMillis());
            }

            private void updateStartDate(long selectedDate) {
                endDate = selectedDate;
                Calendar startDateCalendar = Calendar.getInstance();
                startDateCalendar.setTimeInMillis(selectedDate);
                startDateCalendar.add(Calendar.DATE, -maxDateRange);
                if (startDate < startDateCalendar.getTimeInMillis()) {
                    startDate = startDateCalendar.getTimeInMillis();
                }
                if (startDate < minStartDate) {
                    startDate = minStartDate;
                }
                if (startDate > endDate) {
                    startDate = endDate;
                }
                updateDateView();
            }
        });
    }

    private void updateDateView() {
        startDatePickerLabelView.setContent(DatePickerUtils.getReadableDate(getActivity(), startDate));
        endDatePickerLabelView.setContent(DatePickerUtils.getReadableDate(getActivity(), endDate));
    }

    public void saveDate() {
        if (callback != null) {
            callback.onDateSubmitted(startDate, endDate);
        } else {
            getActivity().finish();
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}