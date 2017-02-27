package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.widget.DatePickerLabelView;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsEditPromoShopDI;
import com.tokopedia.seller.topads.view.dialog.DatePickerDialog;
import com.tokopedia.seller.topads.view.dialog.TimePickerdialog;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsEditPromoPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class TopAdsEditPromoFragment extends BasePresenterFragment<TopAdsEditPromoPresenter> implements TopAdsEditPromoView {

    private TextInputLayout maxPriceInputLayout;
    private EditText maxPriceEditText;
    private RadioGroup budgetRadioGroup;
    private RadioButton budgetLifeTimeRadioButton;
    private RadioButton budgetPerDayRadioButton;
    private TextInputLayout budgetPerDayInputLayout;
    private EditText budgetPerDayEditText;
    private RadioGroup showTimeRadioGroup;
    private RadioButton showTimeAutomaticRadioButton;
    private RadioButton showTimeConfiguredRadioButton;
    private View showTimeConfiguredView;
    private DatePickerLabelView showTimeStartDateDatePicker;
    private DatePickerLabelView showTimeStartTimeDatePicker;
    private DatePickerLabelView showTimeEndDateDatePicker;
    private DatePickerLabelView showTimeEndTimeDatePicker;
    private RadioGroup iconRadioGroup;

    private Date startDate;
    private Date endDate;

    protected String adId;

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
        presenter = TopAdsEditPromoShopDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle bundle) {
        adId = bundle.getString(TopAdsExtraConstant.EXTRA_AD_ID);
    }

    @Override
    protected void initView(View view) {
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
        showTimeStartDateDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_start_date);
        showTimeStartTimeDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_start_time);
        showTimeEndDateDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_end_date);
        showTimeEndTimeDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_end_time);
        iconRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_icon);
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
        showTimeStartDateDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate.getTime());
                new DatePickerDialog(getActivity(), calendar, new DatePickerDialog.OnDateSetListener(showTimeStartDateDatePicker, startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        startDate = date;
                    }
                }).show();
            }
        });
        showTimeStartTimeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate.getTime());
                new TimePickerdialog(getActivity(), calendar, false, new TimePickerdialog.OnTimeSetListener(showTimeStartTimeDatePicker, startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        startDate = date;
                    }
                }).show();
            }
        });
        showTimeEndDateDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endDate.getTime());
                new DatePickerDialog(getActivity(), calendar, new DatePickerDialog.OnDateSetListener(showTimeEndDateDatePicker, endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        endDate = date;
                    }
                }).show();
            }
        });
        showTimeEndTimeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endDate.getTime());
                new TimePickerdialog(getActivity(), calendar, false, new TimePickerdialog.OnTimeSetListener(showTimeEndTimeDatePicker, endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME) {
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
        presenter.getDetailAd(adId);
    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        loadAd(topAdsDetailAdViewModel);
    }

    @Override
    public void onLoadDetailAdError() {

    }

    protected void loadAd(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        maxPriceEditText.setText(String.valueOf(topAdsDetailAdViewModel.getPriceBid()));
        budgetPerDayEditText.setText(String.valueOf(topAdsDetailAdViewModel.getPriceDaily()));
    }

    private void showBudgetPerDay(boolean show) {
        budgetPerDayInputLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showTimeConfigurationTime(boolean show) {
        showTimeConfiguredView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void updateDisplayDateView() {
        showTimeStartDateDatePicker.setContent(getDateText(startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE));
        showTimeStartTimeDatePicker.setContent(getDateText(startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME));
        showTimeEndDateDatePicker.setContent(getDateText(endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE));
        showTimeEndTimeDatePicker.setContent(getDateText(endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME));
    }

    private String getDateText(Date date, String formatDate) {
        return new SimpleDateFormat(formatDate, Locale.ENGLISH).format(date);
    }
}