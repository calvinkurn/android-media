package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class TopAdsEditPromoFragment<T extends TopAdsEditPromoPresenter> extends BasePresenterFragment<T> implements TopAdsEditPromoView {

    private static final int STICKER_SPEAKER = 3;
    private static final int STICKER_THUMBS_UP = 2;
    private static final int STICKER_FIRE = 1;

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
    private View promoIconView;
    private RadioGroup iconRadioGroup;
    private RadioButton iconSpeakerRadioButton;
    private RadioButton iconThumbsUpRadioButton;
    private RadioButton iconFireRadioButton;
    private Button submitButton;

    private Date startDate;
    private Date endDate;

    protected String adId;
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
        promoIconView = view.findViewById(R.id.layout_top_ads_edit_promo_icon);
        iconRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_icon);
        iconSpeakerRadioButton = (RadioButton) view.findViewById(R.id.radio_button_icon_speaker);
        iconThumbsUpRadioButton = (RadioButton) view.findViewById(R.id.radio_button_icon_thumbs_up);
        iconFireRadioButton = (RadioButton) view.findViewById(R.id.radio_button_icon_fire);
        submitButton = (Button) view.findViewById(R.id.button_submit);
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
                submitAd();
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

    protected void submitAd() {
        populateDataFromfields();
    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel detailAd) {
        loadAd(detailAd);
    }

    @Override
    public void onLoadDetailAdError() {

    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {

    }

    @Override
    public void onSaveAdError() {

    }

    protected void loadAd(TopAdsDetailAdViewModel detailAd) {
        this.detailAd = detailAd;
        maxPriceEditText.setText(String.valueOf(detailAd.getPriceBid()));
        if (detailAd.getPriceDaily() > 0) {
            showBudgetPerDay(true);
            budgetPerDayEditText.setText(String.valueOf(detailAd.getPriceDaily()));
        } else {
            showBudgetPerDay(false);
            budgetPerDayEditText.setText("");
        }
        convertDate(detailAd);
        if (!TextUtils.isEmpty(detailAd.getEndDate())) {
            showTimeConfigurationTime(true);
        } else {
            showTimeConfigurationTime(false);
        }
        updateDisplayDateView();
        if (detailAd.getStickerId() > 0) {
            promoIconView.setVisibility(View.VISIBLE);
            updateTopAdsSticker(detailAd.getStickerId());
        } else {
            promoIconView.setVisibility(View.GONE);
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
        budgetPerDayInputLayout.setVisibility(show ? View.VISIBLE : View.GONE);
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

    protected void populateDataFromfields() {
        String priceBid = maxPriceEditText.getText().toString();
        if (TextUtils.isEmpty(priceBid)) {
            detailAd.setPriceBid(0);
        } else {
            detailAd.setPriceBid(Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(priceBid)));
        }
        if (budgetLifeTimeRadioButton.isChecked()) {
            detailAd.setBudget(false);
            detailAd.setPriceDaily(0);
        } else {
            String priceDaily = budgetPerDayEditText.getText().toString();
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
    }

}