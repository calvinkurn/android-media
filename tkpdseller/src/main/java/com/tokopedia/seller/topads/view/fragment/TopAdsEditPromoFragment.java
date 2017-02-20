package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.view.presenter.TopAdsEditPromoPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsEditPromoPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductActivity;
import com.tokopedia.seller.topads.view.dialog.DatePickerDialog;
import com.tokopedia.seller.topads.view.dialog.TimePickerdialog;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TopAdsEditPromoFragment extends BasePresenterFragment<TopAdsEditPromoPresenter> implements TopAdsEditPromoFragmentListener {

    RecyclerView productListRecylerView;
    TextInputLayout maxPriceInputLayout;
    EditText maxPriceEditText;
    RadioGroup budgetRadioGroup;
    RadioButton budgetLifeTimeRadioButton;
    RadioButton budgetPerDayRadioButton;
    TextInputLayout budgetPerDayInputLayout;
    EditText budgetPerDayEditText;
    RadioGroup showTimeRadioGroup;
    RadioButton showTimeAutomaticRadioButton;
    RadioButton showTimeConfiguredRadioButton;
    View showTimeConfiguredView;
    EditText showTimeStartDateEditText;
    EditText showTimeStartTimeEditText;
    EditText showTimeEndDateEditText;
    EditText showTimeEndTimeEditText;
    RadioGroup iconRadioGroup;
    Button addProductButton;

    private Date startDate;
    private Date endDate;

    public static TopAdsEditPromoFragment createInstance() {
        TopAdsEditPromoFragment fragment = new TopAdsEditPromoFragment();
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
        presenter = new TopAdsEditPromoPresenterImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_promo;
    }

    @Override
    protected void initView(View view) {
        productListRecylerView = (RecyclerView) view.findViewById(R.id.recyler_view_product_list);
        maxPriceInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_max_price);
        maxPriceEditText = (EditText) view.findViewById(R.id.edit_text_max_price);
        budgetRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_budget);
        budgetLifeTimeRadioButton = (RadioButton) view.findViewById(R.id.radio_button_budget_life_time);
        budgetPerDayRadioButton = (RadioButton) view.findViewById(R.id.radio_button_budget_per_day);
        budgetPerDayInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_budget_per_day);
        budgetPerDayEditText = (EditText) view.findViewById(R.id.edit_text_budget_per_day);
        showTimeRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_show_time);
        showTimeAutomaticRadioButton = (RadioButton) view.findViewById(R.id.radio_button_show_time_automatic);
        showTimeConfiguredRadioButton = (RadioButton) view.findViewById(R.id.radio_button_show_time_configured);
        showTimeConfiguredView = view.findViewById(R.id.layout_show_time_configured);
        showTimeStartDateEditText = (EditText) view.findViewById(R.id.edit_text_show_time_start_date);
        showTimeStartTimeEditText = (EditText) view.findViewById(R.id.edit_text_show_time_start_time);
        showTimeEndDateEditText = (EditText) view.findViewById(R.id.edit_text_show_time_end_date);
        showTimeEndTimeEditText = (EditText) view.findViewById(R.id.edit_text_show_time_end_time);
        iconRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_icon);
        addProductButton = (Button) view.findViewById(R.id.button_add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
        maxPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CurrencyFormatHelper.SetToRupiah(maxPriceEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        budgetPerDayEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CurrencyFormatHelper.SetToRupiah(budgetPerDayEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        budgetRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radio_button_budget_life_time) {
                    showBudgetPerDay(false);
                } else if (checkedId == R.id.radio_button_budget_per_day) {
                    showBudgetPerDay(true);
                }
            }
        });
        showTimeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radio_button_show_time_automatic) {
                    showTimeConfigurationTime(false);
                } else if (checkedId == R.id.radio_button_show_time_configured) {
                    showTimeConfigurationTime(true);
                }
            }
        });
        showTimeStartDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate.getTime());
                new DatePickerDialog(getActivity(), calendar, new DatePickerDialog.OnDateSetListener(showTimeStartDateEditText, startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        startDate = date;
                    }
                }).show();
            }
        });
        showTimeStartTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate.getTime());
                new TimePickerdialog(getActivity(), calendar, false, new TimePickerdialog.OnTimeSetListener(showTimeStartTimeEditText, startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        startDate = date;
                    }
                }).show();
            }
        });
        showTimeEndDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endDate.getTime());
                new DatePickerDialog(getActivity(), calendar, new DatePickerDialog.OnDateSetListener(showTimeEndDateEditText, endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        endDate = date;
                    }
                }).show();
            }
        });
        showTimeEndTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endDate.getTime());
                new TimePickerdialog(getActivity(), calendar, false, new TimePickerdialog.OnTimeSetListener(showTimeEndTimeEditText, endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        endDate = date;
                    }
                }).show();
            }
        });
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_YEAR, 7);
        startDate = startCalendar.getTime();
        endDate = endCalendar.getTime();
        updateDisplayDateView();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onGroupNameListLoaded(@NonNull List<DataCredit> creditList) {

    }

    @Override
    public void onLoadGroupNameListError() {

    }

    private void showBudgetPerDay(boolean show) {
        budgetPerDayInputLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showTimeConfigurationTime(boolean show) {
        showTimeConfiguredView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void updateDisplayDateView() {
        showTimeStartDateEditText.setText(getDateText(startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE));
        showTimeStartTimeEditText.setText(getDateText(startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME));
        showTimeEndDateEditText.setText(getDateText(endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE));
        showTimeEndTimeEditText.setText(getDateText(endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME));
    }

    private String getDateText(Date date, String formatDate) {
        return new SimpleDateFormat(formatDate, Locale.ENGLISH).format(date);
    }

    void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductActivity.class);
        startActivity(intent);
    }
}