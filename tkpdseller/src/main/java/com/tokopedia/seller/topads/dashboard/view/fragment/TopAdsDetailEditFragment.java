package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.common.datepicker.view.widget.DatePickerLabelView;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.utils.ViewUtils;
import com.tokopedia.seller.topads.dashboard.view.dialog.DatePickerDialog;
import com.tokopedia.seller.topads.dashboard.view.dialog.TimePickerdialog;
import com.tokopedia.seller.topads.dashboard.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditPresenter;
import com.tokopedia.seller.topads.dashboard.view.widget.PrefixEditText;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class TopAdsDetailEditFragment<T extends TopAdsDetailEditPresenter> extends BasePresenterFragment<T> implements TopAdsDetailEditView {

    protected TextInputLayout nameInputLayout;
    protected EditText nameEditText;
    private TextInputLayout maxPriceInputLayout;
    private PrefixEditText maxPriceEditText;
    private RadioGroup budgetRadioGroup;
    private RadioButton budgetLifeTimeRadioButton;
    private RadioButton budgetPerDayRadioButton;
    private TextInputLayout budgetPerDayInputLayout;
    private View containerBudgetPerDay;
    private PrefixEditText budgetPerDayEditText;
    private RadioGroup showTimeRadioGroup;
    private RadioButton showTimeAutomaticRadioButton;
    private RadioButton showTimeConfiguredRadioButton;
    private View showTimeConfiguredView;
    private DatePickerLabelView showTimeStartDateDatePicker;
    private DatePickerLabelView showTimeStartTimeDatePicker;
    private DatePickerLabelView showTimeEndDateDatePicker;
    private DatePickerLabelView showTimeEndTimeDatePicker;
    protected Button submitButton;

    private ProgressDialog progressDialog;

    protected String adId;
    protected String name;

    private Date startDate;
    private Date endDate;

    String itemIdToAdd;

    protected TopAdsDetailAdViewModel detailAd;

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void setupArguments(Bundle bundle) {
        adId = bundle.getString(TopAdsExtraConstant.EXTRA_AD_ID);
        name = bundle.getString(TopAdsExtraConstant.EXTRA_NAME);
        itemIdToAdd = bundle.getString(TopAdsExtraConstant.EXTRA_ITEM_ID);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null && !TextUtils.isEmpty(itemIdToAdd)) {
            presenter.getProductDetail(itemIdToAdd);
        }
    }

    @Override
    protected void initView(View view) {
        nameInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        nameEditText = (EditText) view.findViewById(R.id.edit_text_name);
        maxPriceInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_max_price);
        maxPriceEditText = (PrefixEditText) view.findViewById(R.id.edit_text_max_price);
        budgetRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_budget);
        budgetLifeTimeRadioButton = (RadioButton) view.findViewById(R.id.radio_button_budget_life_time);
        budgetPerDayRadioButton = (RadioButton) view.findViewById(R.id.radio_button_budget_per_day);
        budgetPerDayInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_budget_per_day);
        containerBudgetPerDay = (View) view.findViewById(R.id.container_budget_per_day);
        budgetPerDayEditText = (PrefixEditText) view.findViewById(R.id.edit_text_budget_per_day);
        showTimeRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_show_time);
        showTimeAutomaticRadioButton = (RadioButton) view.findViewById(R.id.radio_button_show_time_automatic);
        showTimeConfiguredRadioButton = (RadioButton) view.findViewById(R.id.radio_button_show_time_configured);
        showTimeConfiguredView = view.findViewById(R.id.layout_show_time_configured);
        showTimeStartDateDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_start_date);
        showTimeStartTimeDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_start_time);
        showTimeEndDateDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_end_date);
        showTimeEndTimeDatePicker = (DatePickerLabelView) view.findViewById(R.id.date_picker_show_time_end_time);
        submitButton = (Button) view.findViewById(R.id.button_submit);
        maxPriceEditText.addTextChangedListener(new CurrencyIdrTextWatcher(maxPriceEditText, getString(R.string.top_ads_detail_edit_default_currency_value)) {

            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                String errorMessage = ViewUtils.getClickBudgetError(getActivity(), number);
                if (!TextUtils.isEmpty(errorMessage)) {
                    maxPriceInputLayout.setError(errorMessage);
                } else {
                    maxPriceInputLayout.setError(null);
                }
            }
        });
        budgetPerDayEditText.addTextChangedListener(new CurrencyIdrTextWatcher(budgetPerDayEditText, getString(R.string.top_ads_detail_edit_default_currency_value)) {

            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                String clickBudgetString = CurrencyFormatHelper.RemoveNonNumeric(maxPriceEditText.getTextWithoutPrefix());
                float clickBudget = 0;
                if (!TextUtils.isEmpty(clickBudgetString)) {
                    clickBudget = Float.parseFloat(clickBudgetString);
                }
                String errorMessage = ViewUtils.getDailyBudgetError(getActivity(), clickBudget, number);
                if (!TextUtils.isEmpty(errorMessage)) {
                    budgetPerDayInputLayout.setError(errorMessage);
                } else {
                    budgetPerDayInputLayout.setError(null);
                }
            }
        });
        budgetPerDayEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    CommonUtils.hideKeyboard(getActivity(), view);
                }
            }
        });
        // trigger the watcher first time
        budgetPerDayEditText.setText(budgetPerDayEditText.getText());

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), calendar, new DatePickerDialog.OnDateSetListener(showTimeStartDateDatePicker, startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        startDate = date;
                    }
                });
                datePickerDialog.setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        showTimeStartTimeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate.getTime());
                TimePickerdialog timePickerdialog = new TimePickerdialog(getActivity(), calendar, false, new TimePickerdialog.OnTimeSetListener(showTimeStartTimeDatePicker, startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        startDate = date;
                    }
                });
                timePickerdialog.show();
            }
        });
        showTimeEndDateDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endDate.getTime());
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), calendar, new DatePickerDialog.OnDateSetListener(showTimeEndDateDatePicker, endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        endDate = date;
                    }
                });
                datePickerDialog.setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        showTimeEndTimeDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(endDate.getTime());
                TimePickerdialog timePickerdialog = new TimePickerdialog(getActivity(), calendar, false, new TimePickerdialog.OnTimeSetListener(showTimeEndTimeDatePicker, endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME) {
                    @Override
                    public void onDateUpdated(Date date) {
                        super.onDateUpdated(date);
                        endDate = date;
                    }
                });
                timePickerdialog.show();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAd();
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
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
        if (!TextUtils.isEmpty(adId)) {
            loadAdDetail();
        }
        if (!TextUtils.isEmpty(name)) {
            nameEditText.setText(name);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    protected void loadAdDetail() {
        showLoading();
    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel detailAd) {
        hideLoading();
        loadAd(detailAd);
    }

    @Override
    public void onLoadDetailAdError(String errorMessage) {
        hideLoading();
        showEmptyState(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadAdDetail();
            }
        });
    }

    protected void saveAd() {
        showLoading();
        populateDataFromFields();
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        hideLoading();
        setResultAdSaved();
        getActivity().finish();
    }

    @Override
    public void onSaveAdError(String errorMessage) {
        hideLoading();
        showSnackBarError(errorMessage);
    }

    protected void loadAd(TopAdsDetailAdViewModel detailAd) {
        this.detailAd = detailAd;
        nameEditText.setText(detailAd.getTitle());
        maxPriceEditText.setText(String.valueOf(detailAd.getPriceBid()));
        if (detailAd.getPriceDaily() > 0) {
            showBudgetPerDay(true);
            budgetPerDayEditText.setText(String.valueOf(detailAd.getPriceDaily()));
        } else {
            showBudgetPerDay(false);
            // no need to set, already default from xml
            // budgetPerDayEditText.setText(R.string.top_ads_detail_edit_default_currency_value);
        }
        convertDate(detailAd);
        if (!TextUtils.isEmpty(detailAd.getEndDate())) {
            showTimeConfigurationTime(true);
        } else {
            showTimeConfigurationTime(false);
        }
        updateDisplayDateView();
    }

    private void convertDate(TopAdsDetailAdViewModel detailAd) {
        String parseFormat = TopAdsConstant.EDIT_PROMO_DISPLAY_DATE + ", " + TopAdsConstant.EDIT_PROMO_DISPLAY_TIME;
        try {
            String date = detailAd.getStartDate() + ", " + detailAd.getStartTime();
            startDate = getDate(date, parseFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            String date = detailAd.getEndDate() + ", " + detailAd.getEndTime();
            endDate = getDate(date, parseFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showBudgetPerDay(boolean show) {
        containerBudgetPerDay.setVisibility(show ? View.VISIBLE : View.GONE);
        if (!show && !budgetLifeTimeRadioButton.isChecked()) {
            budgetLifeTimeRadioButton.setChecked(true);
        }
        if (show && !budgetPerDayRadioButton.isChecked()) {
            budgetPerDayRadioButton.setChecked(true);
        }
    }

    private void showTimeConfigurationTime(boolean show) {
        showTimeConfiguredView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (!show && !showTimeAutomaticRadioButton.isChecked()) {
            showTimeAutomaticRadioButton.setChecked(true);
        }
        if (show && !showTimeConfiguredRadioButton.isChecked()) {
            showTimeConfiguredRadioButton.setChecked(true);
        }
    }

    private void updateDisplayDateView() {
        showTimeStartDateDatePicker.setContent(getDateText(startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE));
        showTimeStartTimeDatePicker.setContent(getDateText(startDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME));
        showTimeEndDateDatePicker.setContent(getDateText(endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_DATE));
        showTimeEndTimeDatePicker.setContent(getDateText(endDate, TopAdsConstant.EDIT_PROMO_DISPLAY_TIME));
    }

    private String getDateText(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(date);
    }

    private Date getDate(String dateText, String dateFormat) throws ParseException {
        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(dateText);
    }

    protected void populateDataFromFields() {
        if (detailAd ==  null) {
            onSaveAdError(null);
            return;
        }
        String priceBid = maxPriceEditText.getTextWithoutPrefix();
        if (TextUtils.isEmpty(priceBid)) {
            detailAd.setPriceBid(0);
        } else {
            detailAd.setPriceBid(Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(priceBid)));
        }
        if (budgetLifeTimeRadioButton.isChecked()) {
            detailAd.setBudget(false);
            detailAd.setPriceDaily(0);
        } else {
            String priceDaily = budgetPerDayEditText.getTextWithoutPrefix();
            if (TextUtils.isEmpty(priceDaily)) {
                detailAd.setPriceDaily(0);
            } else {
                detailAd.setPriceDaily(Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(priceDaily)));
            }
            detailAd.setBudget(true);
        }
        if (showTimeAutomaticRadioButton.isChecked()) {
            detailAd.setScheduled(false);
        } else {
            detailAd.setScheduled(true);
        }
        detailAd.setStartDate(showTimeStartDateDatePicker.getValue());
        detailAd.setStartTime(showTimeStartTimeDatePicker.getValue());
        detailAd.setEndDate(showTimeEndDateDatePicker.getValue());
        detailAd.setEndTime(showTimeEndTimeDatePicker.getValue());
        detailAd.setTitle(nameEditText.getText().toString());
    }

    protected void showLoading() {
        progressDialog.show();
        getView().setVisibility(View.GONE);
    }

    protected void hideLoading() {
        progressDialog.dismiss();
        getView().setVisibility(View.VISIBLE);
    }

    protected void showSnackBarError(String errorMessage) {
        if (!TextUtils.isEmpty(errorMessage)) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(), errorMessage);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_network_error));
        }
    }

    protected void showEmptyState(NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryClickedListener);
    }

    private void setResultAdSaved() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }
}