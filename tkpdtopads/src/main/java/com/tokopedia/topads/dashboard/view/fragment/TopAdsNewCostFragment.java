package com.tokopedia.topads.dashboard.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.widget.PrefixEditText;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.topads.keyword.utils.EmptyCurrencyIdrTextWatcher;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public abstract class TopAdsNewCostFragment<T extends StepperModel, V extends TopAdsDetailAdViewModel> extends BasePresenterFragment {

    private static final String TAG = "TopAdsNewCostFragment";
    public static final int DEFAULT_KELIPATAN = 50;

    protected T stepperModel;
    protected StepperListener stepperListener;

    private TextInputLayout maxPriceInputLayout;
    private PrefixEditText maxPriceEditText;
    private RadioGroup budgetRadioGroup;
    private RadioButton budgetLifeTimeRadioButton;
    private RadioButton budgetPerDayRadioButton;
    private TextInputLayout budgetPerDayInputLayout;
    private View containerBudgetPerDay;
    private PrefixEditText budgetPerDayEditText;
    protected Button submitButton;
    protected ProgressDialog progressDialog;
    protected TextView headerText;
    protected TextView titleCost;
    protected TextView titleSuggestionBidUse;
    protected TextView titleSuggestionBid;

    protected V detailAd;
    private String suggestionBidText;
    private String prefixSuggestion;

    protected void onClickedNext() {
        showLoading();
        populateDataFromFields();
    }

    protected boolean isError() {
        return (maxPriceInputLayout.isErrorEnabled() && maxPriceInputLayout.getError() != null) ||
                (budgetPerDayInputLayout.isErrorEnabled() && budgetPerDayInputLayout.getError() != null);
    }

    protected abstract V initiateDetailAd();

    @Override
    protected void initView(View view) {
        super.initView(view);
        maxPriceInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_max_price);
        maxPriceEditText = (PrefixEditText) view.findViewById(R.id.edit_text_max_price);
        budgetRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_budget);
        budgetLifeTimeRadioButton = (RadioButton) view.findViewById(R.id.radio_button_budget_life_time);
        budgetPerDayRadioButton = (RadioButton) view.findViewById(R.id.radio_button_budget_per_day);
        budgetPerDayInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_budget_per_day);
        containerBudgetPerDay = (View) view.findViewById(R.id.container_budget_per_day);
        budgetPerDayEditText = (PrefixEditText) view.findViewById(R.id.edit_text_budget_per_day);
        submitButton = (Button) view.findViewById(R.id.button_submit);
        headerText = (TextView) view.findViewById(R.id.header_text);
        titleCost = (TextView) view.findViewById(R.id.title_cost);
        // trigger the watcher first time
        budgetPerDayEditText.setText(budgetPerDayEditText.getText());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        titleSuggestionBid = (TextView) view.findViewById(R.id.text_suggestion_bid);
        titleSuggestionBidUse = (TextView)view.findViewById(R.id.text_suggestion_bid_use);
        titleSuggestionBidUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxPriceEditText.setText(suggestionBidText.replaceAll(prefixSuggestion, "").trim());
            }
        });
        prefixSuggestion = getString(R.string.title_currency_rp_space);
    }

    private String getSuggestionBidRaw(){
        if(suggestionBidText == null)
            return null;

        return suggestionBidText.substring(prefixSuggestion.length()).trim();
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        loadSuggestionBid();
    }

    protected abstract void loadSuggestionBid();

    protected abstract void onSuggestionTitleUseClick();

    protected void setSuggestionBidText(@Nullable GetSuggestionResponse data){
        if(data == null)
            return;
        setSuggestionBidText(data.getData().get(0).getMedianFmt());
    }

    protected void setSuggestionBidText(@Nullable String data){
        if(data == null)
            return;

        this.suggestionBidText = data;

        CommonUtils.dumper(TAG+" >> "+ data);
        titleSuggestionBid.setText(MethodChecker.fromHtml(getRecSuggestionBid(data)));
    }

    protected String getRecSuggestionBid(String data){
        return getString(R.string.label_top_ads_max_price_description)+" <b>"+data+"</b> ";
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        detailAd = initiateDetailAd();
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        EmptyCurrencyIdrTextWatcher emptyCurrencyIdrTextWatcher = new EmptyCurrencyIdrTextWatcher(maxPriceEditText, getString(R.string.top_ads_detail_edit_default_currency_value)) {

            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);


                String errorMessage = ViewUtils.getClickBudgetError(getActivity(), number);
                if (!TextUtils.isEmpty(errorMessage)) {
                    maxPriceInputLayout.setError(errorMessage);
                } else {
                    maxPriceInputLayout.setError(null);
                }

                String suggestionBidRaw = getSuggestionBidRaw();
                if (suggestionBidRaw == null)
                    return;

                if (number >= Double.valueOf(suggestionBidRaw)) {
                    titleSuggestionBid.setText(R.string.top_ads_label_price_desc);
                    titleSuggestionBidUse.setVisibility(View.GONE);
                } else {
                    setSuggestionBidText(suggestionBidText);
                    titleSuggestionBidUse.setVisibility(View.VISIBLE);
                }
            }
        };
        emptyCurrencyIdrTextWatcher.setAvoidMessageErrorValue(DEFAULT_KELIPATAN);
        maxPriceEditText.addTextChangedListener(emptyCurrencyIdrTextWatcher);
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
                if (!hasFocus) {
                    CommonUtils.hideKeyboard(getActivity(), view);
                }
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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedNext();
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
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

    protected void showLoading() {
        progressDialog.show();
    }

    protected void hideLoading() {
        progressDialog.dismiss();
    }

    protected void populateDataFromFields() {
        String priceBid = maxPriceEditText.getTextWithoutPrefix();
        if (TextUtils.isEmpty(priceBid)) {
            detailAd.setPriceBid(Float.valueOf(getSuggestionBidRaw()));
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
    }

    protected void loadAd(V detailAd) {
        if (detailAd != null) {
            this.detailAd = detailAd;
            maxPriceEditText.setText(String.valueOf(detailAd.getPriceBid()));
            if (detailAd.getPriceDaily() > 0) {
                showBudgetPerDay(true);
                budgetPerDayEditText.setText(String.valueOf(detailAd.getPriceDaily()));
            } else {
                showBudgetPerDay(false);
            }
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_new_cost;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context instanceof StepperListener) {
            this.stepperListener = (StepperListener) context;
        }
    }
}
