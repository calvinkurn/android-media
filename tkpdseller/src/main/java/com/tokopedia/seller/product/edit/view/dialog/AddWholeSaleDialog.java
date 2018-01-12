package com.tokopedia.seller.product.edit.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.design.text.CounterInputView;
import com.tokopedia.design.text.DecimalInputView;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.util.CurrencyUsdTextWatcher;
import com.tokopedia.design.text.watcher.NumberTextWatcher;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author normansyahputa on 4/20/17.
 *         <p>
 *         KEEP IN MIND !!!
 *         rules for minimum and maximum
 *         <p>
 *         { 1 < minimum quantity <= maximum quantity <= INFINITY }
 */
public class AddWholeSaleDialog extends DialogFragment {

    public static final String TAG = "AddWholeSaleDialog";
    public static final String KEY_WHOLE_SALE_BASE_VALUE = "KEY_WHOLE_SALE_BASE_VALUE";
    public static final String KEY_WHOLE_SALE_PREVIOUS_VALUE = "KEY_WHOLE_SALE_PREVIOUS_VALUE";
    public static final String KEY_CURRENCY_TYPE = "KEY_CURRENCY_TYPE";
    public static final String IS_OFFICIAL_STORE = "isOfficialStore";
    private final Locale dollarLocale = Locale.US;
    private final Locale idrLocale = new Locale("in", "ID");
    OnDismissListener onDismissListener;
    private WholeSaleDialogListener listener;
    private CounterInputView maxWholeSale;
    private CounterInputView minWholeSale;
    private DecimalInputView wholesalePrice;
    private WholesaleModel baseValue;
    private TextView title;
    @CurrencyTypeDef
    private int currencyType;
    private WholesaleModel previousValue;
    private boolean isErrorReturn;
    private int minQuantityRaw = 0;
    private CurrencyIdrTextWatcher idrTextWatcher;
    private CurrencyUsdTextWatcher usdTextWatcher;
    private NumberFormat formatter;
    private boolean isOfficialStore;

    public static AddWholeSaleDialog newInstance(
            WholesaleModel fixedPrice,
            @CurrencyTypeDef int currencyType,
            WholesaleModel previousWholesalePrice,
            boolean isOfficialStore) {
        AddWholeSaleDialog addWholeSaleDialog = new AddWholeSaleDialog();
        Bundle bundle = new Bundle();
        if (previousWholesalePrice != null)
            bundle.putParcelable(KEY_WHOLE_SALE_PREVIOUS_VALUE, previousWholesalePrice);

        bundle.putParcelable(KEY_WHOLE_SALE_BASE_VALUE, fixedPrice);
        bundle.putInt(KEY_CURRENCY_TYPE, currencyType);
        bundle.putBoolean(IS_OFFICIAL_STORE, isOfficialStore);
        addWholeSaleDialog.setArguments(bundle);
        return addWholeSaleDialog;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (previousValue != null) {
            outState.putParcelable(KEY_WHOLE_SALE_PREVIOUS_VALUE, previousValue);
        }
        outState.putParcelable(KEY_WHOLE_SALE_BASE_VALUE, baseValue);
        outState.putInt(KEY_CURRENCY_TYPE, currencyType);
        outState.putBoolean(IS_OFFICIAL_STORE, isOfficialStore);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_product_add_whole_sale, container, false);
        determineFormatter();
        title = (TextView) view.findViewById(R.id.string_picker_dialog_title);
        title.setText(R.string.product_add_whole_sale_title);
        minWholeSale = (CounterInputView) view.findViewById(R.id.counter_input_view_minimum_whole_sale);
        maxWholeSale = (CounterInputView) view.findViewById(R.id.counter_input_view_maximum_whole_sale);
        wholesalePrice = (DecimalInputView) view.findViewById(R.id.counter_input_view_wholesale_price);
        view.findViewById(R.id.string_picker_dialog_confirm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addItem(getItem());
                    }
                });
        view.findViewById(R.id.string_picker_dialog_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        if (savedInstanceState == null && getArguments() != null) {
            extractBundle(getArguments());
        } else {
            extractBundle(savedInstanceState);
        }

        // set min based on previous data
        String minQuantity = null;
        boolean isFirsttime = false;
        if (previousValue == null) {
            isFirsttime = true;
            minQuantityRaw = baseValue.getQtyTwo() + 1;
            minQuantity = Integer.toString(minQuantityRaw);
        } else {
            minQuantityRaw = previousValue.getQtyTwo() + 1;
            minQuantity = Integer.toString(minQuantityRaw);
        }
        minWholeSale.setText(minQuantity);
        /**
         * first time means whole sale price at index 1.
         */
        if (isFirsttime) {
            minWholeSale.setEnabled(true);
            minWholeSale.addTextChangedListener(new NumberTextWatcher(minWholeSale.getEditText()) {
                @Override
                public void onNumberChanged(double minQuantity) {
                    super.onNumberChanged(minQuantity);
                    if (validateMinQuantity(minQuantity)) return;
                    validateMaxQuantity(maxWholeSale.getDoubleValue(), true);
                }
            });
        } else {
            minWholeSale.setEnabled(false);
        }

        final boolean finalIsFirsttime = isFirsttime;
        maxWholeSale.addTextChangedListener(new NumberTextWatcher(maxWholeSale.getEditText()) {
            @Override
            public void onNumberChanged(double maxQuantity) {
                super.onNumberChanged(maxQuantity);
                validateMaxQuantity(maxQuantity, finalIsFirsttime);
            }
        });
        maxWholeSale.setText(Integer.toString(minQuantityRaw + 1));
        maxWholeSale.invalidate();
        maxWholeSale.requestLayout();

        double idrBaseMinimumValue = 0;
        if (previousValue == null) {
            idrBaseMinimumValue = baseValue.getQtyPrice() - 1;
        } else {
            idrBaseMinimumValue = previousValue.getQtyPrice() - 1;
        }

        idrTextWatcher = new CurrencyIdrTextWatcher(wholesalePrice.getEditText()) {
            @Override
            public void onNumberChanged(double number) {
                validatePrice(number);
            }
        };

        double usdBaseMinimumValue = 0;
        if (previousValue == null) {
            usdBaseMinimumValue = baseValue.getQtyPrice() - 0.01;
        } else {
            usdBaseMinimumValue = previousValue.getQtyPrice() - 0.01;
        }
        usdTextWatcher = new CurrencyUsdTextWatcher(wholesalePrice.getEditText()) {
            @Override
            public void onNumberChanged(double number) {
                validatePrice(number);
            }
        };

        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                wholesalePrice.addTextChangedListener(usdTextWatcher);
                wholesalePrice.setValue(usdBaseMinimumValue);
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                wholesalePrice.addTextChangedListener(idrTextWatcher);
                wholesalePrice.setValue(idrBaseMinimumValue);
                break;

        }
        wholesalePrice.invalidate();
        wholesalePrice.requestLayout();

        return view;
    }

    private void determineFormatter() {
        formatter = NumberFormat.getNumberInstance(dollarLocale);
    }

    protected void validateMaxQuantity(double maxQuantity, boolean finalIsFirsttime) {
        /**
         * less than minimum is not allowed, equal and larger is a must.
         */
        if (maxQuantity < minQuantityRaw) {
            maxWholeSale.setError(getString(R.string.product_quantity_range_is_not_valid));
            isErrorReturn = true;
            return;
        }

        if (finalIsFirsttime) {
            if (maxQuantity <= minWholeSale.getDoubleValue()) {
                maxWholeSale.setError(getString(R.string.product_prompt_larger));
                isErrorReturn = true;
                return;
            }
            validateMinQuantity(minWholeSale.getDoubleValue());
        }

        isErrorReturn = false;
        maxWholeSale.setError(null);
    }

    protected boolean validateMinQuantity(double minQuantity) {
        if (minQuantity <= 0) {
            minWholeSale.setError(getString(R.string.product_quantity_range_is_not_valid));
            minWholeSale.updateMinusButtonState(false);
            return isErrorReturn = true;
        }

        if (minQuantity >= maxWholeSale.getDoubleValue()) {
            minWholeSale.setError(getString(R.string.product_prompt_lesser));
            return isErrorReturn = true;
        }

        minWholeSale.setError(null);
        minWholeSale.updateMinusButtonState(true);
        return isErrorReturn = false;
    }

    protected void extractBundle(Bundle data) {
        data.setClassLoader(WholesaleModel.class.getClassLoader());
        baseValue = data.getParcelable(KEY_WHOLE_SALE_BASE_VALUE);

        // parse to certain currency
        switch (data.getInt(KEY_CURRENCY_TYPE)) {
            case CurrencyTypeDef.TYPE_USD:
                currencyType = CurrencyTypeDef.TYPE_USD;
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                currencyType = CurrencyTypeDef.TYPE_IDR;
                break;
        }

        previousValue = data.getParcelable(KEY_WHOLE_SALE_PREVIOUS_VALUE);
        isOfficialStore = data.getBoolean(IS_OFFICIAL_STORE);
    }

    protected void validatePrice(double currencyValue) {
        Pair<Double, Double> minMaxPrice = ViewUtils.minMaxPrice(getActivity(), currencyType, isOfficialStore);
        if (minMaxPrice.first > currencyValue || currencyValue > minMaxPrice.second) {
            wholesalePrice.setError(getString(R.string.product_error_product_price_not_valid,
                    formatter.format(minMaxPrice.first), formatter.format(minMaxPrice.second)));
            isErrorReturn = true;
            return;
        }

        if (currencyValue >= baseValue.getQtyPrice()) {
            wholesalePrice.setError(getString(R.string.product_price_should_be_cheaper_than_fix_price));
            isErrorReturn = true;
            return;
        }

        if (previousValue != null) {
            if (currencyValue >= previousValue.getQtyPrice()) {
                wholesalePrice.setError(getString(R.string.product_price_should_be_cheaper_than_previous_wholesale_price));
                isErrorReturn = true;
                return;
            }
        }

        isErrorReturn = false;
        wholesalePrice.setError(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WholeSaleDialogListener) {
            listener = (WholeSaleDialogListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }

    protected void addItem(WholesaleModel object) {
        if(!isErrorReturn)
            validatePrice(wholesalePrice.getDoubleValue());

        if (isErrorReturn)
            return;

        listener.addWholesaleItem(object);
        dismiss();
    }

    protected WholesaleModel getItem() {
        return new WholesaleModel(
                Integer.parseInt(removeComma(minWholeSale.getEditText().getText().toString())),
                Integer.parseInt(removeComma(maxWholeSale.getEditText().getText().toString())),
                Double.parseDouble(removeComma(wholesalePrice.getEditText().getText().toString()))
        );
    }

    private String removeComma(String value) {
        return value.replace(",", "");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener!= null) {
            onDismissListener.onDismiss();
            onDismissListener = null;
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public interface WholeSaleDialogListener {
        void addWholesaleItem(WholesaleModel item);
    }
}
