package com.tokopedia.seller.product.manage.view.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.util.CurrencyUsdTextWatcher;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by zulfikarrahman on 10/3/17.
 */

public class ProductManageEditPriceDialogFragment extends DialogFragment {

    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String PRODUCT_CURRENCY_ID = "product_currency_id";
    public static final String IS_GOLD_MERCHANT = "isGoldMerchant";
    private CurrencyIdrTextWatcher idrTextWatcher;
    private CurrencyUsdTextWatcher usdTextWatcher;

    public interface ListenerDialogEditPrice {
        void onSubmitEditPrice(String productId, String price, String currencyId, String currencyText);
    }

    private String productId;
    private String productPrice;
    private String productCurrencyId;
    private boolean isGoldMerchant;
    private ListenerDialogEditPrice listenerDialogEditPrice;

    private SpinnerCounterInputView spinnerCounterInputViewPrice;
    private TextView saveButton;
    private TextView cancelButton;

    public static ProductManageEditPriceDialogFragment createInstance(final String productId, String productPrice,
                                                                      String productCurrencyId, boolean isGoldMerchant) {
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        args.putString(PRODUCT_PRICE, productPrice);
        args.putString(PRODUCT_CURRENCY_ID, productCurrencyId);
        args.putBoolean(IS_GOLD_MERCHANT, isGoldMerchant);
        ProductManageEditPriceDialogFragment fragment = new ProductManageEditPriceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListenerDialogEditPrice(ListenerDialogEditPrice listenerDialogEditPrice) {
        this.listenerDialogEditPrice = listenerDialogEditPrice;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getArguments().getString(PRODUCT_ID);
        productPrice = getArguments().getString(PRODUCT_PRICE);
        productCurrencyId = getArguments().getString(PRODUCT_CURRENCY_ID);
        isGoldMerchant = getArguments().getBoolean(IS_GOLD_MERCHANT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_prompt_change_price_product, container, false);
        spinnerCounterInputViewPrice = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_price);
        saveButton = (TextView) view.findViewById(R.id.string_picker_dialog_confirm);
        cancelButton = (TextView) view.findViewById(R.id.string_picker_dialog_cancel);

        spinnerCounterInputViewPrice.setCounterValue(Double.valueOf(productPrice));
        spinnerCounterInputViewPrice.setSpinnerValue(productCurrencyId);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPriceValid()) {
                    listenerDialogEditPrice.onSubmitEditPrice(productId,
                            formatDecimal(spinnerCounterInputViewPrice.getCounterValue()),
                            spinnerCounterInputViewPrice.getSpinnerValue(),
                            spinnerCounterInputViewPrice.getSpinnerEntry());
                    dismiss();
                } else {
                    spinnerCounterInputViewPrice.requestFocus();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        idrTextWatcher = new CurrencyIdrTextWatcher(spinnerCounterInputViewPrice.getCounterEditText()) {
            @Override
            public void onNumberChanged(double number) {
                isPriceValid();
            }
        };
        usdTextWatcher = new CurrencyUsdTextWatcher(spinnerCounterInputViewPrice.getCounterEditText()) {
            @Override
            public void onNumberChanged(double number) {
                isPriceValid();
            }
        };
        spinnerCounterInputViewPrice.addTextChangedListener(idrTextWatcher);
        spinnerCounterInputViewPrice.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                setTextWatcher(value);
            }

            private void setTextWatcher(String spinnerValue) {
                spinnerCounterInputViewPrice.removeTextChangedListener(idrTextWatcher);
                spinnerCounterInputViewPrice.removeTextChangedListener(usdTextWatcher);
                if (spinnerValue.equalsIgnoreCase(spinnerCounterInputViewPrice.getContext().getString(R.string.product_currency_value_idr))) {
                    spinnerCounterInputViewPrice.addTextChangedListener(idrTextWatcher);
                } else {
                    spinnerCounterInputViewPrice.addTextChangedListener(usdTextWatcher);
                }
            }
        });
        spinnerCounterInputViewPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                spinnerCounterInputViewPrice.post(new Runnable() {
                    @Override
                    public void run() {
                        onItemClicked(position);
                    }
                });
            }

            private void onItemClicked(int position) {
                if (!isGoldMerchant && spinnerCounterInputViewPrice.getSpinnerValue(position).equalsIgnoreCase(spinnerCounterInputViewPrice.getContext().getString(R.string.product_currency_value_usd))) {
                    spinnerCounterInputViewPrice.setSpinnerValue(spinnerCounterInputViewPrice.getContext().getString(R.string.product_currency_value_idr));
                    Snackbar.make(spinnerCounterInputViewPrice.getRootView().findViewById(android.R.id.content), R.string.product_error_must_be_gold_merchant, Snackbar.LENGTH_LONG)
                            .setActionTextColor(ContextCompat.getColor(spinnerCounterInputViewPrice.getContext(), R.color.green_400))
                            .show();
                    return;
                }
                spinnerCounterInputViewPrice.setCounterValue(Double.parseDouble(spinnerCounterInputViewPrice.getContext().getString(R.string.product_default_counter_text)));
                EditText editText = spinnerCounterInputViewPrice.getCounterEditText();
                editText.setSelection(editText.getText().length());
                spinnerCounterInputViewPrice.setCounterError(null);
            }
        });

        return view;
    }

    private boolean isPriceValid() {
        Pair<Double, Double> minMaxPrice = ViewUtils.minMaxPrice(
                spinnerCounterInputViewPrice.getContext(),
                Integer.parseInt(spinnerCounterInputViewPrice.getSpinnerValue()));

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        if (minMaxPrice.first > getPriceValue() || getPriceValue() > minMaxPrice.second) {
            spinnerCounterInputViewPrice.setCounterError(spinnerCounterInputViewPrice.getContext().getString(R.string.product_error_product_price_not_valid,
                    numberFormat.format(minMaxPrice.first), numberFormat.format(minMaxPrice.second)));
            return false;
        }
        spinnerCounterInputViewPrice.setCounterError(null);
        return true;
    }

    public double getPriceValue() {
        return spinnerCounterInputViewPrice.getCounterValue();
    }

    private String formatDecimal(double productPrice) {
        if (productPrice == (long) productPrice)
            return String.format(Locale.US, "%d", (long) productPrice);
        else
            return String.format("%s", productPrice);
    }
}
