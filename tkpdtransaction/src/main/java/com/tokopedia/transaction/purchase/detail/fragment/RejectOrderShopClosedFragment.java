package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.common.data.order.OrderDetailData;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderShopClosedFragment extends TkpdFragment {

    private RejectOrderShopClosedListener listener;

    private String[] months;

    private String selectedEndDate = "";

    private static final String ORDER_DETAIL_DATA_KEY = "ORDER_DETAIL_DATA_KEY";

    public static RejectOrderShopClosedFragment createFragment(OrderDetailData data) {
        RejectOrderShopClosedFragment fragment = new RejectOrderShopClosedFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER_DETAIL_DATA_KEY, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_reject_store_closed, container, false);
        ViewGroup endDateLayout = view.findViewById(R.id.end_date_layout);
        EditText startDateField = view.findViewById(R.id.start_date_field);
        EditText endDateField = view.findViewById(R.id.end_date_field);
        EditText notesField = view.findViewById(R.id.notes_field);
        TextView rejectShipmentConfirmButton = view.findViewById(R.id.reject_shipment_confirm_button);
        Calendar calendar = Calendar.getInstance();
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        months = dateFormatSymbols.getMonths();
        startDateField.setText(
                calendar.get(Calendar.DAY_OF_MONTH)
                        + "/"
                        + months[calendar.get(Calendar.MONTH)].substring(0, 3)
                        + "/" + calendar.get(Calendar.YEAR));
        endDateLayout.setOnClickListener(onEndDateClickedListener(endDateField));
        endDateField.setOnClickListener(onEndDateClickedListener(endDateField));
        rejectShipmentConfirmButton.setOnClickListener(onConfirmButtonClickedListener(
                endDateField,
                notesField
        ));
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private View.OnClickListener onEndDateClickedListener(final EditText editText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(editText);
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RejectOrderShopClosedListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RejectOrderShopClosedListener) context;
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                onDateSetListener(editText),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener(final EditText editText) {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                selectedEndDate = String.valueOf(dayOfMonth)
                        + "/"
                        + monthNumberZeroGenerator(monthOfYear + 1)
                        + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year);
                editText.setText(
                        String.valueOf(dayOfMonth)
                                + "/"
                                + months[monthOfYear].substring(0, 3)
                                + "/" + String.valueOf(year)
                );
            }
        };
    }

    private View.OnClickListener onConfirmButtonClickedListener(
            final EditText endDateField,
            final EditText notesField) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (endDateField.getText().toString().isEmpty()) {
                    NetworkErrorHelper.showSnackbar(
                            getActivity(),
                            "Mohon Pilih Tanggal Akhir Tutup");
                } else {
                    TKPDMapParam<String, String> params = new TKPDMapParam<>();
                    params.put("order_id",
                            ((OrderDetailData) getArguments().getParcelable(ORDER_DETAIL_DATA_KEY))
                                    .getOrderId());
                    params.put("close_end", selectedEndDate);
                    params.put("closed_note", notesField.getText().toString());
                    params.put("reason_code", "4");
                    listener.onClosedDateSelected(params);
                }
            }
        };
    }

    public interface RejectOrderShopClosedListener {

        void onClosedDateSelected(TKPDMapParam<String, String> rejectParam);

    }

    private String monthNumberZeroGenerator(int month) {
        if(month < 10) {
            return "0";
        } else return "";
    }

}
