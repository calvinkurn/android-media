package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.design.text.CounterInputView;
import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.utils.ProductPriceRangeUtils;
import com.tokopedia.seller.product.edit.view.adapter.WholesaleAdapter;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddWholesaleFragment;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.util.CurrencyUsdTextWatcher;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductPriceViewHolder extends ProductViewHolder{

    public static final int REQUEST_CODE_GET_PRODUCT_WHOLESALE = 100;

    List<ProductWholesaleViewModel> productWholesaleViewModels;
    private ImageButton editPriceImageButton;
    private SpinnerCounterInputView priceSpinnerCounterInputView;
    private CounterInputView minimumOrderCounterInputView;
    private LabelView wholesaleLabelView;

    private boolean goldMerchant;
    private CurrencyIdrTextWatcher idrTextWatcher;
    private CurrencyUsdTextWatcher usdTextWatcher;
    private Listener listener;
    private boolean officialStore;

    public ProductPriceViewHolder(View view, final Listener listener) {
        editPriceImageButton = view.findViewById(R.id.image_button_edit_price);
        priceSpinnerCounterInputView = view.findViewById(R.id.spinner_counter_input_view_price);
        wholesaleLabelView = view.findViewById(R.id.label_view_wholesale);
        minimumOrderCounterInputView = view.findViewById(R.id.counter_input_view_minimum_order);
        editPriceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPriceDialog();
            }
        });
        idrTextWatcher = new CurrencyIdrTextWatcher(priceSpinnerCounterInputView.getCounterEditText()) {
            @Override
            public void onNumberChanged(double number) {
                isPriceValid();
            }
        };
        usdTextWatcher = new CurrencyUsdTextWatcher(priceSpinnerCounterInputView.getCounterEditText()) {
            @Override
            public void onNumberChanged(double number) {
                isPriceValid();
            }
        };
        priceSpinnerCounterInputView.addTextChangedListener(idrTextWatcher);
        priceSpinnerCounterInputView.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                setTextWatcher(value);
            }

            private void setTextWatcher(String spinnerValue) {
                // To reset/change from idr <==> USD, vice versa.
                priceSpinnerCounterInputView.removeTextChangedListener(idrTextWatcher);
                priceSpinnerCounterInputView.removeTextChangedListener(usdTextWatcher);
                if (spinnerValue.equalsIgnoreCase(priceSpinnerCounterInputView.getContext().getString(R.string.product_currency_value_idr))) {
                    priceSpinnerCounterInputView.addTextChangedListener(idrTextWatcher);
                } else {
                    priceSpinnerCounterInputView.addTextChangedListener(usdTextWatcher);
                }
            }
        });

        priceSpinnerCounterInputView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                priceSpinnerCounterInputView.post(new Runnable() {
                    @Override
                    public void run() {
                        onItemClicked(position);
                    }
                });
            }

            private void onItemClicked(int position) {
                if (!goldMerchant && priceSpinnerCounterInputView.getSpinnerValue(position).equalsIgnoreCase(priceSpinnerCounterInputView.getContext().getString(R.string.product_currency_value_usd))) {
                    priceSpinnerCounterInputView.setSpinnerValue(priceSpinnerCounterInputView.getContext().getString(R.string.product_currency_value_idr));
                    if (GlobalConfig.isSellerApp()) {
                        UnifyTracking.eventSwitchRpToDollarAddProduct();
                        ProductPriceViewHolder.this.listener.showDialogMoveToGM(R.string.add_product_label_alert_dialog_dollar);
                    } else {
                        Snackbar.make(priceSpinnerCounterInputView.getRootView().findViewById(android.R.id.content), R.string.product_error_must_be_gold_merchant, Snackbar.LENGTH_LONG)
                                .setActionTextColor(ContextCompat.getColor(priceSpinnerCounterInputView.getContext(), R.color.green_400))
                                .show();
                    }
                    return;
                }
                priceSpinnerCounterInputView.setCounterValue(Double.parseDouble(priceSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                EditText editText = priceSpinnerCounterInputView.getCounterEditText();
                editText.setSelection(editText.getText().length());
                priceSpinnerCounterInputView.setCounterError(null);
            }
        });

        wholesaleLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.startProductAddWholesaleActivity();
            }
        });

        minimumOrderCounterInputView.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (isMinOrderValid()) {
                    minimumOrderCounterInputView.setError(null);
                }
            }
        });

        setListener(listener);
    }

    @Override
    public void renderData(ProductViewModel model) {
        productWholesaleViewModels = model.getProductWholesale();
        double price = model.getPrdPriceOrMinVariantProductPrice();

        // to sync price variant with the price in the main product.
        model.setProductPrice(price);

        setPriceUnit((int) model.getProductPriceCurrency());
        setPriceValue(price);

        if (price > 0) {
            if(listener.hasVariant()){
                priceSpinnerCounterInputView.setEnabled(false);
                editPriceImageButton.setVisibility(View.VISIBLE);
            }
            setLabelWHolesale(productWholesaleViewModels);
        }

        if (model.getProductMinOrder() > 0) {
            setMinimumOrder((int) model.getProductMinOrder());
        }
    }

    private void setLabelWHolesale(List<ProductWholesaleViewModel> productWholesaleViewModels){
        Context context = wholesaleLabelView.getContext();
        if (productWholesaleViewModels != null){
            if(productWholesaleViewModels.isEmpty()){
                wholesaleLabelView.setContent(context.getString(R.string.product_label_add));
                priceSpinnerCounterInputView.setEnabled(true);
                editPriceImageButton.setVisibility(View.GONE);
            }
            else {
                wholesaleLabelView.setContent(String.valueOf(productWholesaleViewModels.size()) + " " + context.getString(R.string.product_label_price));
                priceSpinnerCounterInputView.setEnabled(false);
                editPriceImageButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showEditPriceDialog() {
        if (listener.hasVariant()) {
            listener.showDialogEditPriceVariant();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(editPriceImageButton.getContext(),
                    R.style.AppCompatAlertDialogStyle);
            builder.setTitle(R.string.product_title_confirmation_change_wholesale_price);
            builder.setMessage(R.string.product_confirmation_change_wholesale_price);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    priceSpinnerCounterInputView.setEnabled(true);
                    editPriceImageButton.setVisibility(View.GONE);
                    productWholesaleViewModels.clear();
                    setLabelWHolesale(productWholesaleViewModels);
                    dialog.cancel();
                }
            });
            builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        this.goldMerchant = goldMerchant;
    }

    public boolean isGoldMerchant() {
        return goldMerchant;
    }

    public int getPriceUnit() {
        return Integer.parseInt(priceSpinnerCounterInputView.getSpinnerValue());
    }

    public void setPriceUnit(@CurrencyTypeDef int unit) {
        priceSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public double getPriceValue() {
        return priceSpinnerCounterInputView.getCounterValue();
    }

    public void setPriceValue(double price) {
        priceSpinnerCounterInputView.setCounterValue(price);
        priceSpinnerCounterInputView.getCounterEditText().setError(null);
    }

    public int getMinimumOrder() {
        int minOrder;
        try {
            minOrder = (int) minimumOrderCounterInputView.getDoubleValue();
        } catch (Exception e) {
            minOrder = -1;
        }
        return minOrder;
    }

    public void setMinimumOrder(int value) {
        minimumOrderCounterInputView.setValue(value);
    }

    public @NonNull
    List<ProductWholesaleViewModel> getProductWholesaleViewModels() {
        return productWholesaleViewModels;
    }

    private boolean isPriceValid() {
        if (listener.hasVariant()) {
            return true;
        } else {
            double priceValue = getPriceValue();
            int currencyType = getCurrencyType();
            if (!ProductPriceRangeUtils.isPriceValid(priceValue, currencyType, officialStore)) {
                priceSpinnerCounterInputView.setCounterError(
                        priceSpinnerCounterInputView.getContext().getString(R.string.product_error_product_price_not_valid,
                                ProductPriceRangeUtils.getMinPriceString( currencyType, officialStore),
                                ProductPriceRangeUtils.getMaxPriceString( currencyType, officialStore)));
                wholesaleLabelView.setVisibility(View.GONE);
                return false;
            } else if (ProductPriceRangeUtils.getMinPrice(currencyType, officialStore) == priceValue) {
                wholesaleLabelView.setVisibility(View.GONE);
            } else {
                wholesaleLabelView.setVisibility(View.VISIBLE);
            }
            priceSpinnerCounterInputView.setCounterError(null);
            return true;
        }
    }

    public int getCurrencyType() {
        if (priceSpinnerCounterInputView.getSpinnerTextView().getSpinnerValue()
                .equalsIgnoreCase(priceSpinnerCounterInputView.getContext().getString(R.string.product_currency_value_idr))) {
            return CurrencyTypeDef.TYPE_IDR;
        } else {
            return CurrencyTypeDef.TYPE_USD;
        }
    }

    @Override
    public boolean isDataValid() {
        if (!isPriceValid()) {
            priceSpinnerCounterInputView.requestFocus();
            UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_PRICE);
            return false;
        }
        if (!isMinOrderValid()) {
            minimumOrderCounterInputView.requestFocus();
            UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_MIN_PURCHASE);
            return false;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {

    }

    @Override
    public void updateModel(ProductViewModel model) {
        model.setProductPriceCurrency(getPriceUnit());
        model.setProductPrice(getPriceValue());
        model.setProductMinOrder(getMinimumOrder());
        model.setProductWholesale(getProductWholesaleViewModels());
    }

    public boolean isMinOrderValid() {
        Context context = minimumOrderCounterInputView.getContext();
        String minOrderString = context.getString(R.string.product_minimum_order);
        String maxOrderString = context.getString(R.string.product_maximum_order);

        double minOrder = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(minOrderString));
        double maxOrder = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(maxOrderString));
        if (minOrder > getMinimumOrder() || getMinimumOrder() > maxOrder) {
            minimumOrderCounterInputView.setError(context.getString(R.string.product_error_product_minimum_order_not_valid, minOrderString, maxOrderString));
            return false;
        }
        minimumOrderCounterInputView.setError(null);
        return true;
    }

    public void setOfficialStore(boolean officialStore) {
        this.officialStore = officialStore;
        if (getPriceValue() > 0) {
            setPriceValue(priceSpinnerCounterInputView.getCounterValue());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GET_PRODUCT_WHOLESALE:
                if (resultCode == Activity.RESULT_OK) {
                    productWholesaleViewModels = data.getParcelableArrayListExtra(ProductAddWholesaleFragment.EXTRA_PRODUCT_WHOLESALE);
                    setLabelWHolesale(productWholesaleViewModels);
                }
                break;
        }
    }

    public boolean isOfficialStore() {
        return officialStore;
    }

    public interface Listener {

        void showDialogMoveToGM(@StringRes int message);

        boolean hasVariant();

        void showDialogEditPriceVariant();

        void startProductAddWholesaleActivity();
    }
}