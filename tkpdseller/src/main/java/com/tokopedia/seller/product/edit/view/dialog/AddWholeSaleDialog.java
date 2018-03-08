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

import com.tokopedia.design.text.CounterInputView;
import com.tokopedia.design.text.DecimalInputView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.util.CurrencyUsdTextWatcher;

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
    public static final String KEY_WHOLE_SALE_PREVIOUS_VALUE = "KEY_WHOLE_SALE_PREVIOUS_VALUE";
    public static final String KEY_CURRENCY_TYPE = "KEY_CURRENCY_TYPE";
    public static final String IS_OFFICIAL_STORE = "isOfficialStore";

    private OnDismissListener onDismissListener;
    private WholeSaleDialogListener listener;

    private CounterInputView minQuantityCounterInputView;
    private DecimalInputView priceDecimalInputView;
    @CurrencyTypeDef
    private int currencyType;
    private WholesaleModel previousValue;
    private NumberFormat formatter;
    private boolean isOfficialStore;

    public static AddWholeSaleDialog newInstance(@CurrencyTypeDef int currencyType, WholesaleModel previousWholesalePrice, boolean isOfficialStore) {
        AddWholeSaleDialog addWholeSaleDialog = new AddWholeSaleDialog();
        Bundle bundle = new Bundle();
        if (previousWholesalePrice != null) {
            bundle.putParcelable(KEY_WHOLE_SALE_PREVIOUS_VALUE, previousWholesalePrice);
        }
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
        formatter = NumberFormat.getNumberInstance(Locale.US);
        TextView titleTextView = view.findViewById(R.id.text_view_title);
        titleTextView.setText(R.string.product_add_whole_sale_title);
        minQuantityCounterInputView = view.findViewById(R.id.counter_input_view_min_quantity);
        priceDecimalInputView = view.findViewById(R.id.decimal_input_view_price);
        view.findViewById(R.id.string_picker_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(getItem());
            }
        });
        view.findViewById(R.id.string_picker_dialog_cancel).setOnClickListener(new View.OnClickListener() {
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
        minQuantityCounterInputView.setText(Long.toString(previousValue.getQtyMin() + 1));
        minQuantityCounterInputView.addTextChangedListener(new NumberTextWatcher(minQuantityCounterInputView.getEditText()) {
            @Override
            public void onNumberChanged(double minQuantity) {
                super.onNumberChanged(minQuantity);
                isMinQuantityValid(minQuantity);
            }
        });
        isMinQuantityValid(minQuantityCounterInputView.getDoubleValue());

        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                double usdBaseMinimumValue = previousValue.getQtyPrice() - 0.01;
                priceDecimalInputView.addTextChangedListener(new CurrencyUsdTextWatcher(priceDecimalInputView.getEditText()) {
                    @Override
                    public void onNumberChanged(double number) {
                        isPriceValid(number);
                    }
                });
                priceDecimalInputView.setValue(usdBaseMinimumValue);
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                double idrBaseMinimumValue = previousValue.getQtyPrice() - 1;
                priceDecimalInputView.addTextChangedListener(new CurrencyIdrTextWatcher(priceDecimalInputView.getEditText()) {
                    @Override
                    public void onNumberChanged(double number) {
                        isPriceValid(number);
                    }
                });
                priceDecimalInputView.setValue(idrBaseMinimumValue);
                break;

        }
        priceDecimalInputView.invalidate();
        priceDecimalInputView.requestLayout();
        return view;
    }

    protected boolean isMinQuantityValid(double minQuantity) {
        if (minQuantity <= previousValue.getQtyMin()) {
            minQuantityCounterInputView.setError(getString(R.string.product_quantity_range_is_not_valid));
            minQuantityCounterInputView.updateMinusButtonState(false);
            return false;
        } else if (minQuantity - 1 == previousValue.getQtyMin()) {
            minQuantityCounterInputView.setError(null);
            minQuantityCounterInputView.updateMinusButtonState(false);
            return true;
        }
        minQuantityCounterInputView.setError(null);
        minQuantityCounterInputView.updateMinusButtonState(true);
        return true;
    }

    protected void extractBundle(Bundle data) {
        data.setClassLoader(WholesaleModel.class.getClassLoader());
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

    protected boolean isPriceValid(double currencyValue) {
        if (!ViewUtils.isPriceValid(currencyValue,currencyType, isOfficialStore)) {
            priceDecimalInputView.setError(getString(R.string.product_error_product_price_not_valid,
                    ViewUtils.getMinPriceString(currencyType, isOfficialStore),
                    ViewUtils.getMaxPriceString(currencyType, isOfficialStore)));
            return false;
        }

        if (currencyValue >= previousValue.getQtyPrice()) {
            if (previousValue.getQtyMin() <= 1) {
                priceDecimalInputView.setError(getString(R.string.product_price_should_be_cheaper_than_fix_price));
            } else {
                priceDecimalInputView.setError(getString(R.string.product_price_should_be_cheaper_than_previous_wholesale_price));
            }
            return false;
        }
        priceDecimalInputView.setError(null);
        return true;
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

    protected void addItem(WholesaleModel wholesaleModel) {
        if (!isPriceValid(priceDecimalInputView.getDoubleValue())) {
            return;
        }
        if (!isMinQuantityValid(Integer.parseInt(removeComma(minQuantityCounterInputView.getEditText().getText().toString())))) {
            return;
        }
        listener.addWholesaleItem(wholesaleModel);
        dismiss();
    }

    protected WholesaleModel getItem() {
        return new WholesaleModel(
                Integer.parseInt(removeComma(minQuantityCounterInputView.getEditText().getText().toString())),
                Double.parseDouble(removeComma(priceDecimalInputView.getEditText().getText().toString()))
        );
    }

    private String removeComma(String value) {
        return value.replace(",", "");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
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
