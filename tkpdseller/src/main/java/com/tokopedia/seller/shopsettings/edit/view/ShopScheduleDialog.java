package com.tokopedia.seller.shopsettings.edit.view;

import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.shop.model.ShopScheduleModel;
import com.tokopedia.core.shop.model.shopData.ClosedDetail;
import com.tokopedia.core.shop.model.shopData.ClosedScheduleDetail;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.edit.presenter.ShopEditorPresenter;
import com.tokopedia.core.util.MethodChecker;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Erry on 5/30/2016.
 */
public class ShopScheduleDialog extends DialogFragment {

    public static final String DETAIL = "detail";
    public static final String SCHEDULE = "schedule";
    private ShopScheduleModel shopScheduleModel;
    private int day;
    private int month;
    private int year;

    public static ShopScheduleDialog newInstance(ClosedDetail closedDetail, ClosedScheduleDetail scheduleDetail) {

        Bundle args = new Bundle();
        args.putParcelable(DETAIL, Parcels.wrap(closedDetail));
        args.putParcelable(SCHEDULE, Parcels.wrap(scheduleDetail));
        ShopScheduleDialog fragment = new ShopScheduleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    EditText startDate;
    EditText endDate;
    CheckBox closeCheckbox;
    EditText noteText;

    private DatePickerUtil datePicker;
    private String formatDate = "dd/MM/yyyy";
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    private ClosedScheduleDetail scheduleDetail;
    private ClosedDetail closedDetail;
    private ShopEditorPresenter shopEditorPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datePicker = new DatePickerUtil(getActivity());
        simpleDateFormat = new SimpleDateFormat(formatDate);
        scheduleDetail = Parcels.unwrap(getArguments().getParcelable(SCHEDULE));
        closedDetail =Parcels.unwrap(getArguments().getParcelable(DETAIL));
    }

    public void setShopEditorPresenter(ShopEditorPresenter shopEditorPresenter) {
        this.shopEditorPresenter = shopEditorPresenter;
    }

    public void closeDialog(){
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "";
        if (scheduleDetail.getCloseStart().isEmpty() && scheduleDetail.getCloseEnd().isEmpty()) {
            title = getString(R.string.add_shop_schedule_dialog_title);
        } else {
            title = getString(R.string.edit_shop_schedule_dialog_title);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        View view = View.inflate(getContext(), R.layout.dialog_shop_schedule, null);
        startDate =(EditText) view.findViewById(R.id.start_date);
        endDate = (EditText) view.findViewById(R.id.end_date);
        closeCheckbox = (CheckBox) view.findViewById(R.id.close_checkbox);
        noteText = (EditText) view.findViewById(R.id.note_text);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartDatePickerDialog();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDatePickerDialog();
            }
        });
        closeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckCloseShop();
            }
        });
        builder.setView(view);
        setData(closedDetail, scheduleDetail);

        switch (scheduleDetail.getCloseStatus()) {
            case 1: //Open Shop
                builder.setPositiveButton(getString(R.string.edit_shop_schedule_dialog_positive_btn_atur), null);
                break;
            case 2: //Close Shop
                builder.setPositiveButton(getString(R.string.edit_shop_schedule_dialog_positive_btn_ubah), null);
                builder.setNegativeButton(getString(R.string.open_shop_now), null);
                break;
            case 3: //Open With Close Schedule
                builder.setPositiveButton(getString(R.string.edit_shop_schedule_dialog_positive_btn_ubah), null);
                builder.setNegativeButton(getString(R.string.action_delete), null);
                break;
        }


        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                Button posButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                switch (scheduleDetail.getCloseStatus()) {
                    case 1:
                        posButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validateForm()) {
                                    if (closeCheckbox.isChecked()) {
                                        shopScheduleModel = new ShopScheduleModel(noteText.getText().toString(),
                                                startDate.getText().toString(), endDate.getText().toString(), 2); // close shop
                                        shopEditorPresenter.updateShopSchedule(shopScheduleModel);
                                    } else {
                                        shopScheduleModel = new ShopScheduleModel(noteText.getText().toString(),
                                                startDate.getText().toString(), endDate.getText().toString(), 3); // set close schedule
                                        shopEditorPresenter.updateShopSchedule(shopScheduleModel);
                                    }
//                                    dialog.dismiss();
                                }
                            }
                        });
                        break;
                    case 2:
                        startDate.setEnabled(false);
                        closeCheckbox.setEnabled(false);
                        posButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validateForm()) {
                                    shopScheduleModel = new ShopScheduleModel(noteText.getText().toString(),
                                            startDate.getText().toString(), endDate.getText().toString(), 5); // extend close shop
                                    shopEditorPresenter.updateShopSchedule(shopScheduleModel);
//                                    dialog.dismiss();
                                }
                            }
                        });
                        negButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shopScheduleModel = new ShopScheduleModel(noteText.getText().toString(),
                                        startDate.getText().toString(), endDate.getText().toString(), 1); // shop open
                                shopEditorPresenter.updateShopSchedule(shopScheduleModel);
//                                dialog.dismiss();
                            }
                        });
                        break;
                    case 3:
                        posButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (validateForm()) {
                                    if (closeCheckbox.isChecked()) {
                                        shopScheduleModel = new ShopScheduleModel(noteText.getText().toString(),
                                                startDate.getText().toString(), endDate.getText().toString(), 2); // close shop
                                        shopEditorPresenter.updateShopSchedule(shopScheduleModel);
                                    } else {
                                        shopScheduleModel = new ShopScheduleModel(noteText.getText().toString(),
                                                startDate.getText().toString(), endDate.getText().toString(), 3); // set close schedule
                                        shopEditorPresenter.updateShopSchedule(shopScheduleModel);
                                    }
//                                    dialog.dismiss();
                                }
                            }
                        });
                        negButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shopScheduleModel = new ShopScheduleModel(noteText.getText().toString(),
                                        startDate.getText().toString(), endDate.getText().toString(), 4); // shop open
                                shopEditorPresenter.updateShopSchedule(shopScheduleModel);
//                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
        return dialog;
    }

    void showStartDatePickerDialog() {
        calendar = Calendar.getInstance(TimeZone.getDefault());
        if(!startDate.getText().toString().isEmpty()){
            calculateDate(startDate.getText().toString());
        }else{
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }
        datePicker.SetDate(day, month, year);
        calendar.add(Calendar.DATE, 1);
        datePicker.setMinDate(calendar.getTimeInMillis());
        datePicker.DatePickerSpinnerShopClose(new DatePickerUtil.onDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                startDate.setText(simpleDateFormat.format(calendar.getTime()));
                startDate.setError(null);
            }
        });
    }

    void showEndDatePickerDialog() {
        calendar = Calendar.getInstance(TimeZone.getDefault());
        if(!endDate.getText().toString().isEmpty()){
            calculateDate(endDate.getText().toString());
        }else{
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }
        datePicker.SetDate(day, month, year);
        if(!endDate.getText().toString().isEmpty()){
            calculateDate(startDate.getText().toString()); // get date start
            calendar.set(Calendar.DATE, day); //set date calender to date start
        }
        calendar.add(Calendar.DATE, 1); //increment 1 date calender
        datePicker.setMinDate(calendar.getTimeInMillis()); // set min date to plus 1 from start date
        datePicker.DatePickerSpinnerShopClose(new DatePickerUtil.onDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                endDate.setText(simpleDateFormat.format(calendar.getTime()));
                endDate.setError(null);
            }
        });
    }

    void onCheckCloseShop() {
        calendar = Calendar.getInstance(TimeZone.getDefault());
        if (closeCheckbox.isChecked()) {
            startDate.setEnabled(false);
            startDate.setText(simpleDateFormat.format(calendar.getTime()));
        } else {
            calendar.add(Calendar.DATE, 1);
            startDate.setEnabled(true);
            startDate.setText(simpleDateFormat.format(calendar.getTime()));
        }
    }

    private void setData(ClosedDetail closedDetail, ClosedScheduleDetail data) {
        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);
        startDate.setFocusable(false);
        endDate.setFocusable(false);
        if (!data.getCloseStart().isEmpty()) {
            startDate.setText(data.getCloseStart());
        }
        if (!data.getCloseEnd().isEmpty()) {
            endDate.setText(data.getCloseEnd());
        }
        /*if(checkNullForZeroJson(closedDetail.getNote())){
            noteText.setText(MethodChecker.fromHtml(closedDetail.getNote()));
        }*/
        if(!isEmpty(data.getCloseLaterNote())){
            noteText.setText(MethodChecker.fromHtml(data.getCloseLaterNote()));
        }
    }

    private boolean checkNullForZeroJson(String data){
        if(data == null || data.equals("0") || data.equals("") || data.equals("null")){
            return false;
        }
        return true;
    }

    private boolean validateForm() {
        if (startDate.getText().toString().isEmpty()) {
            startDate.setError(getString(R.string.error_start_date_empty));
            return false;
        } else {
            startDate.setError(null);
        }
        if (endDate.getText().toString().isEmpty()) {
            endDate.setError(getString(R.string.error_end_date_empty));
            return false;
        } else {
            endDate.setError(null);
        }
        if (noteText.getText().toString().isEmpty()) {
            noteText.setError(getString(R.string.error_note_empty));
            return false;
        } else {
            noteText.setError(null);
        }

        try {
            if (simpleDateFormat.parse(endDate.getText().toString()).before(simpleDateFormat.parse(startDate.getText().toString())) ||
                    simpleDateFormat.parse(endDate.getText().toString()).equals(simpleDateFormat.parse(startDate.getText().toString())) ){
                CommonUtils.UniversalToast(getActivity(), getString(R.string.error_date_start_bigger_than_end_date));
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void calculateDate(String date){
        String[] dates = date.split("/");
        if (!TextUtils.isEmpty(dates[0])) {
            day = Integer.parseInt(dates[0]);
        }
        if (!TextUtils.isEmpty(dates[1])) {
            month = Integer.parseInt(dates[1]);
        }
        if (!TextUtils.isEmpty(dates[2])) {
            year = Integer.parseInt(dates[2]);
        }
    }
}
