package com.tokopedia.seller.topads.view.fragment;

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

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.widget.DatePickerLabelView;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.utils.ViewUtils;
import com.tokopedia.seller.topads.view.dialog.DatePickerDialog;
import com.tokopedia.seller.topads.view.dialog.TimePickerdialog;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditPresenter;
import com.tokopedia.seller.topads.view.widget.PrefixEditText;
import com.tokopedia.seller.util.CurrencyTextWatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class TopAdsDetailEditFragment<T extends TopAdsDetailEditPresenter> extends BasePresenterFragment<T> implements TopAdsDetailEditView {

    private static final int STICKER_SPEAKER = 3;
    private static final int STICKER_THUMBS_UP = 2;
    private static final int STICKER_FIRE = 1;

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
    protected View promoIconView;
    private RadioGroup iconRadioGroup;
    private RadioButton iconSpeakerRadioButton;
    private RadioButton iconThumbsUpRadioButton;
    private RadioButton iconFireRadioButton;
    protected Button submitButton;

    private ProgressDialog progressDialog;

    protected String adId;
    protected String name;

    private Date startDate;
    private Date endDate;

    protected TopAdsDetailAdViewModel detailAd;

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
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle bundle) {
        adId = bundle.getString(TopAdsExtraConstant.EXTRA_AD_ID);
        name = bundle.getString(TopAdsExtraConstant.EXTRA_NAME);
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
        promoIconView = view.findViewById(R.id.layout_top_ads_edit_promo_icon);
        iconRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_icon);
        iconSpeakerRadioButton = (RadioButton) view.findViewById(R.id.radio_button_icon_speaker);
        iconThumbsUpRadioButton = (RadioButton) view.findViewById(R.id.radio_button_icon_thumbs_up);
        iconFireRadioButton = (RadioButton) view.findViewById(R.id.radio_button_icon_fire);
        submitButton = (Button) view.findViewById(R.id.button_submit);
        maxPriceEditText.addTextChangedListener(new CurrencyTextWatcher(maxPriceEditText, getString(R.string.top_ads_detail_edit_default_currency_value)) {

            @Override
            public void onCurrencyChanged(float currencyValue) {
                super.onCurrencyChanged(currencyValue);
                String errorMessage = ViewUtils.getClickBudgetError(getActivity(), currencyValue);
                if (!TextUtils.isEmpty(errorMessage)) {
                    maxPriceInputLayout.setError(errorMessage);
                } else {
                    maxPriceInputLayout.setError(null);
                }
            }
        });
        budgetPerDayEditText.addTextChangedListener(new CurrencyTextWatcher(budgetPerDayEditText, getString(R.string.top_ads_detail_edit_default_currency_value)) {

            @Override
            public void onCurrencyChanged(float currencyValue) {
                super.onCurrencyChanged(currencyValue);
                String clickBudgetString = CurrencyFormatHelper.RemoveNonNumeric(maxPriceEditText.getTextWithoutPrefix());
                float clickBudget = 0;
                if (!TextUtils.isEmpty(clickBudgetString)) {
                    clickBudget = Float.parseFloat(clickBudgetString);
                }
                String errorMessage = ViewUtils.getDailyBudgetError(getActivity(), clickBudget, currencyValue);
                if (!TextUtils.isEmpty(errorMessage)) {
                    budgetPerDayInputLayout.setError(errorMessage);
                } else {
                    budgetPerDayInputLayout.setError(null);
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
        iconRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radio_button_icon_speaker) {
                    updateTopAdsSticker(STICKER_SPEAKER);
                } else if (checkedId == R.id.radio_button_icon_thumbs_up) {
                    updateTopAdsSticker(STICKER_THUMBS_UP);
                } else {
                    updateTopAdsSticker(STICKER_FIRE);
                }
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
        if (detailAd.getStickerId() > 0) {
            updateTopAdsSticker(detailAd.getStickerId());
        }
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

    private void updateTopAdsSticker(int stickerId) {
        switch (stickerId) {
            case STICKER_SPEAKER:
                if (!iconSpeakerRadioButton.isChecked()) {
                    iconSpeakerRadioButton.setChecked(true);
                }
                break;
            case STICKER_THUMBS_UP:
                if (!iconThumbsUpRadioButton.isChecked()) {
                    iconThumbsUpRadioButton.setChecked(true);
                }
                break;
            case STICKER_FIRE:
                if (!iconFireRadioButton.isChecked()) {
                    iconFireRadioButton.setChecked(true);
                }
                break;
        }
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
        if (promoIconView.getVisibility() == View.VISIBLE) {
            if (iconRadioGroup.getCheckedRadioButtonId() == R.id.radio_button_icon_speaker) {
                detailAd.setStickerId(STICKER_SPEAKER);
            } else if (iconRadioGroup.getCheckedRadioButtonId() == R.id.radio_button_icon_thumbs_up) {
                detailAd.setStickerId(STICKER_THUMBS_UP);
            } else if (iconRadioGroup.getCheckedRadioButtonId() == R.id.radio_button_icon_fire) {
                detailAd.setStickerId(STICKER_FIRE);
            }
        }
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
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
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