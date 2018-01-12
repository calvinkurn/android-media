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
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderShopClosedFragment extends TkpdFragment {

    private RejectOrderShopClosedListener listener;

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
        TextView rejectShipmentConfirmButton = view.findViewById(R.id.reject_shipment_confirm_button);
        startDateField.setText(DateFormat.getDateTimeInstance().format(new Date()));
        endDateLayout.setOnClickListener(onEndDateClickedListener(endDateField));
        rejectShipmentConfirmButton.setOnClickListener(onConfirmButtonClickedListener(endDateField));
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

    private void showDatePickerDialog(EditText editText){
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                onDateSetListener(editText),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener(final EditText editText) {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText.setText(
                        String.valueOf(dayOfMonth)
                        + "/"
                        + String.valueOf(monthOfYear)
                        + "/" + String.valueOf(year)
                );
            }
        };
    }

    private View.OnClickListener onConfirmButtonClickedListener(final EditText endDateField) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(endDateField.getText().toString().isEmpty()) {
                    NetworkErrorHelper.showSnackbar(
                            getActivity(),
                            "Mohon Pilih Tanggal Akhir Tutup");
                } else {
                    TKPDMapParam<String, String> params = new TKPDMapParam<>();
                    params.put("order_id",
                            ((OrderDetailData) getArguments().getParcelable(ORDER_DETAIL_DATA_KEY))
                                    .getOrderId());
                    params.put("close_end", endDateField.getText().toString());
                    params.put("reason_code", "4");
                    listener.onClosedDateSelected(params);
                }
            }
        };
    }

    public interface RejectOrderShopClosedListener{

        void onClosedDateSelected(TKPDMapParam<String, String> rejectParam);

    }

}
