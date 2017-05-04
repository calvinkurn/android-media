package com.tokopedia.seller.product.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.view.widget.CounterInputView;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.util.CurrencyUsdTextWatcher;
import com.tokopedia.seller.util.NumberTextWatcher;

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
        if (previousValue == null) {
            minQuantityRaw = baseValue.getQtyTwo() + 1;
            minQuantity = Integer.toString(minQuantityRaw);
        } else {
            minQuantityRaw = previousValue.getQtyTwo() + 1;
            minQuantity = Integer.toString(minQuantityRaw);
        }
        minWholeSale.setText(minQuantity);
        minWholeSale.invalidate();
        minWholeSale.requestLayout();


        maxWholeSale.addTextChangedListener(new NumberTextWatcher(maxWholeSale.getEditText(), minQuantity) {
            @Override
            public void onNumberChanged(float maxQuantity) {
                super.onNumberChanged(maxQuantity);
                /**
                 * less than minimum is not allowed, equal and larger is a must.
                 */
                if (maxQuantity < minQuantityRaw) {
                    maxWholeSale.setError(getString(R.string.quantity_range_is_not_valid));
                    isErrorReturn = true;
                    return;
                }

                isErrorReturn = false;
                maxWholeSale.setError(null);
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

        idrTextWatcher = new CurrencyIdrTextWatcher(wholesalePrice.getEditText(),
                Double.toString(idrBaseMinimumValue)) {
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
        usdTextWatcher = new CurrencyUsdTextWatcher(wholesalePrice.getEditText(),
                Double.toString(usdBaseMinimumValue)) {
            @Override
            public void onNumberChanged(float number) {
                validatePrice(number);
            }
        };

        String result = null;
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                wholesalePrice.addTextChangedListener(usdTextWatcher);
                wholesalePrice.setText(result = Double.toString(usdBaseMinimumValue));
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                wholesalePrice.addTextChangedListener(idrTextWatcher);
                wholesalePrice.setText(result = Double.toString(idrBaseMinimumValue));
                break;

        }
        wholesalePrice.invalidate();
        wholesalePrice.requestLayout();

        return view;
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
