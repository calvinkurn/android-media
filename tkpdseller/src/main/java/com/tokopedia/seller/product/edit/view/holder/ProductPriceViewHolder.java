package com.tokopedia.seller.product.edit.view.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Pair;
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
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.product.edit.view.adapter.WholesaleAdapter;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.util.CurrencyUsdTextWatcher;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductPriceViewHolder extends ProductViewHolder
        implements WholesaleAdapter.Listener {

    public static final String IS_ENABLE_WHOLESALE = "IS_ENABLE_WHOLESALE";
    private static final String IS_WHOLESALE_VISIBLE = "IS_WHOLE_VISIBLE";

    private static final int MAX_WHOLESALE = 5;

    private final Locale dollarLocale = Locale.US;

    private RecyclerView recyclerViewWholesale;
    private WholesaleAdapter wholesaleAdapter;
    private ImageButton editPriceImageButton;
    private SpinnerCounterInputView priceSpinnerCounterInputView;
    private ExpandableOptionSwitch wholesaleExpandableOptionSwitch;
    private CounterInputView minimumOrderCounterInputView;

    private TextView textViewAddWholesale;

    private boolean goldMerchant;
    private CurrencyIdrTextWatcher idrTextWatcher;
    private CurrencyUsdTextWatcher usdTextWatcher;
    private Listener listener;
    @CurrencyTypeDef
    private int currencyType = CurrencyTypeDef.TYPE_IDR;
    private NumberFormat formatter;
    private boolean officialStore;

    public ProductPriceViewHolder(View view, Listener listener) {

        determineFormatter();
        editPriceImageButton = (ImageButton) view.findViewById(R.id.image_button_edit_price);
        priceSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_price);
        wholesaleExpandableOptionSwitch = (ExpandableOptionSwitch) view.findViewById(R.id.expandable_option_switch_wholesale);
        minimumOrderCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_minimum_order);
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
                clearWholesaleItems();
            }
        };
        usdTextWatcher = new CurrencyUsdTextWatcher(priceSpinnerCounterInputView.getCounterEditText()) {
            @Override
            public void onNumberChanged(double number) {
                isPriceValid();
                clearWholesaleItems();
            }
        };
        priceSpinnerCounterInputView.addTextChangedListener(idrTextWatcher);
        priceSpinnerCounterInputView.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                setTextWatcher(value);
            }

            private void setTextWatcher(String spinnerValue) {
                priceSpinnerCounterInputView.removeTextChangedListener(idrTextWatcher);
                priceSpinnerCounterInputView.removeTextChangedListener(usdTextWatcher);
                if (spinnerValue.equalsIgnoreCase(priceSpinnerCounterInputView.getContext().getString(R.string.product_currency_value_idr))) {
                    priceSpinnerCounterInputView.addTextChangedListener(idrTextWatcher);
                    currencyType = CurrencyTypeDef.TYPE_IDR;
                } else {
                    priceSpinnerCounterInputView.addTextChangedListener(usdTextWatcher);
                    currencyType = CurrencyTypeDef.TYPE_USD;
                }
                determineFormatter();
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

        wholesaleExpandableOptionSwitch.setExpandableListener(new BaseExpandableOption.ExpandableListener() {
            @Override
            public void onExpandViewChange(boolean isExpand) {
                setPriceEnabled(!isExpand);
            }

            private void setPriceEnabled(boolean enable) {
                priceSpinnerCounterInputView.setEnabled(enable);
                editPriceImageButton.setVisibility(enable ? View.GONE : View.VISIBLE);
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

        textViewAddWholesale = (TextView) view.findViewById(R.id.text_view_add_wholesale);
        textViewAddWholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProductPriceViewHolder.this.listener != null) {
                    ProductPriceViewHolder.this.listener.startAddWholeSaleDialog(getBaseValue(), currencyType, getPreviousValue(), officialStore);
                }
            }
        });
        recyclerViewWholesale = (RecyclerView) view.findViewById(R.id.recycler_view_wholesale);
        recyclerViewWholesale.setLayoutManager(new LinearLayoutManager(recyclerViewWholesale.getContext(), LinearLayoutManager.VERTICAL, false));
        wholesaleAdapter = new WholesaleAdapter();
        wholesaleAdapter.setListener(this);
        recyclerViewWholesale.setAdapter(wholesaleAdapter);

        setListener(listener);
    }

    @Override
    public void renderData(ProductViewModel model) {
        setPriceUnit((int)model.getProductPriceCurrency());
        if (model.getProductPrice()>0) {
            setPriceValue(model.getProductPrice());
        }
        if (model.getProductWholesale().size() > 0) {
            expandWholesale(true);
            setWholesalePrice(model.getProductWholesale());
        }
        if (model.getProductMinOrder() > 0) {
            setMinimumOrder((int)model.getProductMinOrder());
        }
    }

    private void showEditPriceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(editPriceImageButton.getContext(),
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.product_title_confirmation_change_wholesale_price);
        builder.setMessage(R.string.product_confirmation_change_wholesale_price);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                expandWholesale(false);
                clearWholesaleItems();
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateWholesaleButton() {
        textViewAddWholesale.setVisibility(wholesaleAdapter.getItemCount() < MAX_WHOLESALE ? View.VISIBLE : View.GONE);
    }

    public void expandWholesale(boolean expand) {
        wholesaleExpandableOptionSwitch.setExpand(expand);
    }

    public void setWholesalePrice(List<ProductWholesaleViewModel> wholesalePrice) {
        // parse to wholesaleadapter
        wholesaleAdapter.addAllWholeSalePrice(wholesalePrice);
        updateWholesaleButton();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        this.goldMerchant = goldMerchant;
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

    public void addWholesaleItem(WholesaleModel wholesaleModel) {
        wholesaleAdapter.addItem(wholesaleModel);
        wholesaleAdapter.notifyDataSetChanged();
        updateWholesaleButton();
    }

    private WholesaleModel getBaseValue() {
        return new WholesaleModel(1, getPriceValue());
    }

    private WholesaleModel getPreviousValue() {
        if (wholesaleAdapter != null) {
            return wholesaleAdapter.getLastItem();
        }
        return null;
    }

    private void clearWholesaleItems() {
        if (wholesaleAdapter.getItemCount() > 0) {
            wholesaleAdapter.clearAll();
            wholesaleAdapter.notifyDataSetChanged();
        }
        updateWholesaleButton();
    }

    public List<ProductWholesaleViewModel> getProductWholesaleViewModels() {
        if (!wholesaleExpandableOptionSwitch.isExpanded()) {
            return new ArrayList<>();
        }
        return wholesaleAdapter.getProductWholesaleViewModels();
    }

    private boolean isPriceValid() {
        Pair<Double, Double> minMaxPrice = ViewUtils.minMaxPrice(
                priceSpinnerCounterInputView.getContext(),
                Integer.parseInt(priceSpinnerCounterInputView.getSpinnerValue()), officialStore);

        if (minMaxPrice.first > getPriceValue() || getPriceValue() > minMaxPrice.second) {
            priceSpinnerCounterInputView.setCounterError(priceSpinnerCounterInputView.getContext().getString(R.string.product_error_product_price_not_valid,
                    formatter.format(minMaxPrice.first), formatter.format(minMaxPrice.second)));
            wholesaleExpandableOptionSwitch.setVisibility(View.GONE);
            return false;
        } else if (minMaxPrice.first == getPriceValue()) {
            wholesaleExpandableOptionSwitch.setVisibility(View.GONE);
        } else {
            wholesaleExpandableOptionSwitch.setVisibility(View.VISIBLE);
        }
        priceSpinnerCounterInputView.setCounterError(null);
        return true;
    }

    private void determineFormatter() {
        formatter = NumberFormat.getNumberInstance(dollarLocale);
    }

    @Override
    public void notifySizeChanged(int currentSize) {
        updateWholesaleButton();
    }

    @Override
    public int getCurrencyType() {
        return currencyType;
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
    public void updateModel(ProductViewModel model) {
        model.setProductPriceCurrency(getPriceUnit());
        model.setProductPrice(getPriceValue());
        model.setProductMinOrder(getMinimumOrder());
        model.setProductWholesale(getProductWholesaleViewModels());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(IS_ENABLE_WHOLESALE, wholesaleExpandableOptionSwitch.isEnabled());
        savedInstanceState.putBoolean(IS_WHOLESALE_VISIBLE, wholesaleExpandableOptionSwitch.getVisibility() == View.VISIBLE);
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        wholesaleExpandableOptionSwitch.setEnabled(savedInstanceState.getBoolean(IS_ENABLE_WHOLESALE));

        boolean isWholeSaleVisible = savedInstanceState.getBoolean(IS_WHOLESALE_VISIBLE, false);
        wholesaleExpandableOptionSwitch.setVisibility(isWholeSaleVisible ? View.VISIBLE : View.GONE);
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
        if(getPriceValue() > 0){
            setPriceValue(priceSpinnerCounterInputView.getCounterValue());
        }
    }

    public interface Listener {

        /**
         * @param fixedPrice             means for fixed price.
         * @param currencyType           {@link CurrencyTypeDef}
         * @param previousWholesalePrice previousWholesalePrice
         * @param officialStore
         */
        void startAddWholeSaleDialog(WholesaleModel fixedPrice,
                                     @CurrencyTypeDef int currencyType,
                                     WholesaleModel previousWholesalePrice, boolean officialStore);

        void showDialogMoveToGM(@StringRes int message);
    }
}