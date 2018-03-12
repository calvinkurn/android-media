package com.tokopedia.seller.product.variant.view.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;

/**
 * @author hendry on 4/20/17.
 */
public class ProductChangeVariantPriceDialogFragment extends DialogFragment {

    private static final String TAG = ProductChangeVariantPriceDialogFragment.class.getSimpleName();

    public static final String KEY_CURRENCY_TYPE = "curr_type";
    public static final String KEY_CURRENCY_VALUE = "curr_value";
    public static final String IS_OFFICIAL_STORE = "is_os";

    private OnDismissListener onDismissListener;
    private OnProductChangeVariantPriceFragmentListener onProductChangeVariantPriceFragmentListener;

    public interface OnProductChangeVariantPriceFragmentListener {

    }

//    private CounterInputView minQuantityCounterInputView;
//    private DecimalInputView priceDecimalInputView;
//    @CurrencyTypeDef
//    private int currencyType;
//    private WholesaleModel previousValue;
//    private NumberFormat formatter;
//    private boolean isOfficialStore;

    public static ProductChangeVariantPriceDialogFragment newInstance(@CurrencyTypeDef int currencyType,
                                                                      double prevCurrencyValue,
                                                                      boolean isOfficialStore) {
        ProductChangeVariantPriceDialogFragment addWholeSaleDialog = new ProductChangeVariantPriceDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(KEY_CURRENCY_VALUE, prevCurrencyValue);
        bundle.putInt(KEY_CURRENCY_TYPE, currencyType);
        bundle.putBoolean(IS_OFFICIAL_STORE, isOfficialStore);
        addWholeSaleDialog.setArguments(bundle);
        return addWholeSaleDialog;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
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
        View view = inflater.inflate(R.layout.dialog_change_price_variant, container, false);
//        formatter = NumberFormat.getNumberInstance(Locale.US);
//        TextView titleTextView = view.findViewById(R.id.text_view_title);
//        titleTextView.setText(R.string.product_add_whole_sale_title);
//        minQuantityCounterInputView = view.findViewById(R.id.counter_input_view_min_quantity);
//        priceDecimalInputView = view.findViewById(R.id.decimal_input_view_price);
        view.findViewById(R.id.string_picker_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClicked();
            }
        });
        view.findViewById(R.id.string_picker_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (savedInstanceState == null) {
            //TODO setText for price and currency here
        }

//        // set min based on previous data
//        minQuantityCounterInputView.setText(Long.toString(previousValue.getQtyMin() + 1));
//        minQuantityCounterInputView.addTextChangedListener(new NumberTextWatcher(minQuantityCounterInputView.getEditText()) {
//            @Override
//            public void onNumberChanged(double minQuantity) {
//                super.onNumberChanged(minQuantity);
//                isMinQuantityValid(minQuantity);
//            }
//        });
//        isMinQuantityValid(minQuantityCounterInputView.getDoubleValue());

//        switch (currencyType) {
//            case CurrencyTypeDef.TYPE_USD:
//                double usdBaseMinimumValue = previousValue.getQtyPrice() - 0.01;
//                priceDecimalInputView.addTextChangedListener(new CurrencyUsdTextWatcher(priceDecimalInputView.getEditText()) {
//                    @Override
//                    public void onNumberChanged(double number) {
//                        isPriceValid(number);
//                    }
//                });
//                priceDecimalInputView.setValue(usdBaseMinimumValue);
//                break;
//            default:
//            case CurrencyTypeDef.TYPE_IDR:
//                double idrBaseMinimumValue = previousValue.getQtyPrice() - 1;
//                priceDecimalInputView.addTextChangedListener(new CurrencyIdrTextWatcher(priceDecimalInputView.getEditText()) {
//                    @Override
//                    public void onNumberChanged(double number) {
//                        isPriceValid(number);
//                    }
//                });
//                priceDecimalInputView.setValue(idrBaseMinimumValue);
//                break;
//
//        }
//        priceDecimalInputView.invalidate();
//        priceDecimalInputView.requestLayout();
        return view;
    }

    private void onSubmitClicked(){

    }

//    protected boolean isMinQuantityValid(double minQuantity) {
//        if (minQuantity <= previousValue.getQtyMin()) {
//            minQuantityCounterInputView.setError(getString(R.string.product_quantity_range_is_not_valid));
//            minQuantityCounterInputView.updateMinusButtonState(false);
//            return false;
//        } else if (minQuantity - 1 == previousValue.getQtyMin()) {
//            minQuantityCounterInputView.setError(null);
//            minQuantityCounterInputView.updateMinusButtonState(false);
//            return true;
//        }
//        minQuantityCounterInputView.setError(null);
//        minQuantityCounterInputView.updateMinusButtonState(true);
//        return true;
//    }
//
//    protected void extractBundle(Bundle data) {
//        data.setClassLoader(WholesaleModel.class.getClassLoader());
//        // parse to certain currency
//        switch (data.getInt(KEY_CURRENCY_TYPE)) {
//            case CurrencyTypeDef.TYPE_USD:
//                currencyType = CurrencyTypeDef.TYPE_USD;
//                break;
//            default:
//            case CurrencyTypeDef.TYPE_IDR:
//                currencyType = CurrencyTypeDef.TYPE_IDR;
//                break;
//        }
//
//        previousValue = data.getParcelable(KEY_WHOLE_SALE_PREVIOUS_VALUE);
//        isOfficialStore = data.getBoolean(IS_OFFICIAL_STORE);
//    }

//    protected boolean isPriceValid(double currencyValue) {
//        if (!ViewUtils.isPriceValid(currencyValue, currencyType, isOfficialStore)) {
//            priceDecimalInputView.setError(getString(R.string.product_error_product_price_not_valid,
//                    ViewUtils.getMinPriceString(currencyType, isOfficialStore),
//                    ViewUtils.getMaxPriceString(currencyType, isOfficialStore)));
//            return false;
//        }
//
//        if (currencyValue >= previousValue.getQtyPrice()) {
//            if (previousValue.getQtyMin() <= 1) {
//                priceDecimalInputView.setError(getString(R.string.product_price_should_be_cheaper_than_fix_price));
//            } else {
//                priceDecimalInputView.setError(getString(R.string.product_price_should_be_cheaper_than_previous_wholesale_price));
//            }
//            return false;
//        }
//        priceDecimalInputView.setError(null);
//        return true;
//    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachListener(activity);
        }
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }

    protected void onAttachListener(Context context) {
        onProductChangeVariantPriceFragmentListener = (OnProductChangeVariantPriceFragmentListener) context;
    }

//    protected void addItem(WholesaleModel wholesaleModel) {
//        if (!isPriceValid(priceDecimalInputView.getDoubleValue())) {
//            return;
//        }
//        if (!isMinQuantityValid(Integer.parseInt(removeComma(minQuantityCounterInputView.getEditText().getText().toString())))) {
//            return;
//        }
//        listener.addWholesaleItem(wholesaleModel);
//        dismiss();
//    }
//
//    protected WholesaleModel getItem() {
//        return new WholesaleModel(
//                Integer.parseInt(removeComma(minQuantityCounterInputView.getEditText().getText().toString())),
//                Double.parseDouble(removeComma(priceDecimalInputView.getEditText().getText().toString()))
//        );
//    }

//    private String removeComma(String value) {
//        return value.replace(",", "");
//    }

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

}
