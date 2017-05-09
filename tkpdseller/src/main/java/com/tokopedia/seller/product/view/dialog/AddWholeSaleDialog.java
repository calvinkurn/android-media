package com.tokopedia.seller.product.view.dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.tokopedia.seller.product.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.view.holder.ProductDetailViewHolder;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.view.widget.CounterInputView;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.util.CurrencyUsdTextWatcher;
import com.tokopedia.seller.util.NumberTextWatcher;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author normansyahputa on 4/20/17.
 *
 * KEEP IN MIND !!!
 * rules for minimum and maximum
 *
 * { 1 < minimum quantity <= maximum quantity <= INFINITY }
 */
public class AddWholeSaleDialog extends DialogFragment {

    public static final String TAG = "AddWholeSaleDialog";
    public static final String KEY_WHOLE_SALE_BASE_VALUE = "KEY_WHOLE_SALE_BASE_VALUE";
    public static final String KEY_WHOLE_SALE_PREVIOUS_VALUE = "KEY_WHOLE_SALE_PREVIOUS_VALUE";
    public static final String KEY_CURRENCY_TYPE = "KEY_CURRENCY_TYPE";
    private final Locale dollarLocale = Locale.US;
    private final Locale idrLocale = new Locale("in", "ID");
    private WholeSaleDialogListener listener;
    private CounterInputView maxWholeSale;
    private CounterInputView minWholeSale;
    private CounterInputView wholesalePrice;
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

    public static AddWholeSaleDialog newInstance(
            WholesaleModel fixedPrice,
            @CurrencyTypeDef int currencyType,
            WholesaleModel previousWholesalePrice) {
        AddWholeSaleDialog addWholeSaleDialog = new AddWholeSaleDialog();
        Bundle bundle = new Bundle();
        if (previousWholesalePrice != null)
            bundle.putParcelable(KEY_WHOLE_SALE_PREVIOUS_VALUE, previousWholesalePrice);

        bundle.putParcelable(KEY_WHOLE_SALE_BASE_VALUE, fixedPrice);
        bundle.putInt(KEY_CURRENCY_TYPE, currencyType);
        addWholeSaleDialog.setArguments(bundle);
        return addWholeSaleDialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (previousValue != null)
            outState.putParcelable(KEY_WHOLE_SALE_PREVIOUS_VALUE, previousValue);
        outState.putParcelable(KEY_WHOLE_SALE_BASE_VALUE, baseValue);
        outState.putInt(KEY_CURRENCY_TYPE, currencyType);
    }

    @NonNull
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_whole_sale_dialog_layout, container, false);
        determineFormatter();
        title = (TextView) view.findViewById(R.id.string_picker_dialog_title);
        title.setText(R.string.add_whole_sale_title);
        minWholeSale = (CounterInputView) view.findViewById(R.id.counter_input_view_minimum_whole_sale);
        maxWholeSale = (CounterInputView) view.findViewById(R.id.counter_input_view_maximum_whole_sale);
        wholesalePrice = (CounterInputView) view.findViewById(R.id.counter_input_view_wholesale_price);
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
                public void onNumberChanged(float minQuantity) {
                    super.onNumberChanged(minQuantity);
                    if (validateMinQuantity(minQuantity)) return;
                    validateMaxQuantity(maxWholeSale.getFloatValue(), true);
                }
            });
        }

        final boolean finalIsFirsttime = isFirsttime;
        maxWholeSale.addTextChangedListener(new NumberTextWatcher(maxWholeSale.getEditText()) {
            @Override
            public void onNumberChanged(float maxQuantity) {
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
            public void onNumberChanged(float number) {
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
            public void onNumberChanged(float number) {
                validatePrice(number);
            }
        };

        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                wholesalePrice.addTextChangedListener(usdTextWatcher);
//                wholesalePrice.setValue((float) usdBaseMinimumValue);
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                wholesalePrice.addTextChangedListener(idrTextWatcher);
//                wholesalePrice.setValue((float) idrBaseMinimumValue);
                break;

        }
        wholesalePrice.invalidate();
        wholesalePrice.requestLayout();

        return view;
    }

    private void determineFormatter() {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                formatter = NumberFormat.getNumberInstance(dollarLocale);
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                formatter = NumberFormat.getNumberInstance(idrLocale);
                break;
        }
    }

    protected void validateMaxQuantity(float maxQuantity, boolean finalIsFirsttime) {
        /**
         * less than minimum is not allowed, equal and larger is a must.
         */
        if (maxQuantity < minQuantityRaw) {
            maxWholeSale.setError(getString(R.string.quantity_range_is_not_valid));
            isErrorReturn = true;
            return;
        }

        if (finalIsFirsttime) {
            if (maxQuantity <= minWholeSale.getFloatValue()) {
                maxWholeSale.setError(getString(R.string.prompt_larger));
                isErrorReturn = true;
                return;
            }
            validateMinQuantity(minWholeSale.getFloatValue());
        }

        isErrorReturn = false;
        maxWholeSale.setError(null);
    }

    protected boolean validateMinQuantity(float minQuantity) {
        if (minQuantity <= 1) {
            minWholeSale.setError(getString(R.string.quantity_range_is_not_valid));
            return isErrorReturn = true;
        }

        if (minQuantity >= maxWholeSale.getFloatValue()) {
            minWholeSale.setError(getString(R.string.prompt_lesser));
            return isErrorReturn = true;
        }

        minWholeSale.setError(null);
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
    }

    protected void validatePrice(float currencyValue) {

        Pair<Float, Float> minMaxPrice = ProductDetailViewHolder.minMaxPrice(getActivity(), currencyType);
        if (minMaxPrice.first > currencyValue || currencyValue > minMaxPrice.second) {
            wholesalePrice.setError(getString(R.string.product_error_product_price_not_valid,
                    formatter.format(minMaxPrice.first), formatter.format(minMaxPrice.second)));
            isErrorReturn = true;
            return;
        }

        if (currencyValue >= baseValue.getQtyPrice()) {
            wholesalePrice.setError(getString(R.string.price_should_be_cheaper_than_fix_price));
            isErrorReturn = true;
            return;
        }

        if (previousValue != null) {
            if (currencyValue >= previousValue.getQtyPrice()) {
                wholesalePrice.setError(getString(R.string.price_should_be_cheaper_than_previous_wholesale_price));
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
        validatePrice(wholesalePrice.getFloatValue());

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

    public interface WholeSaleDialogListener {
        void addWholesaleItem(WholesaleModel item);
    }
}
