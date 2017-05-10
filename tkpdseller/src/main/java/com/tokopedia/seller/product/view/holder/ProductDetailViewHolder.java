package com.tokopedia.seller.product.view.holder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.view.adapter.WholesaleAdapter;
import com.tokopedia.seller.product.view.model.upload.ProductWholesaleViewModel;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.view.widget.CounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerCounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.util.CurrencyUsdTextWatcher;
import com.tokopedia.seller.util.NumberTextWatcher;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductDetailViewHolder extends ProductViewHolder
        implements WholesaleAdapter.Listener {

    public static final int REQUEST_CODE_ETALASE = 301;
    public static final String IS_ACTIVE_WHOLESALE = "IS_ACTIVE_WHOLESALE";
    public static final String IS_ACTIVE_STOCK = "IS_ACTIVE_STOCK";
    private static final String BUNDLE_ETALASE_ID = "BUNDLE_ETALASE_ID";
    private static final String BUNDLE_ETALASE_NAME = "BUNDLE_ETALASE_NAME";
    private static final String KEY_WHOLESALE = "KEY_WHOLESALE";
    private static final String BUNDLE_SPINNER_POSITION = "BUNDLE_SPINNER_POSITION";
    private static final String BUNDLE_COUNTER_PRICE = "BUNDLE_COUNTER_PRICE";
    private static final String IS_WHOLESALE_VISIBLE = "IS_WHOLE_VISIBLE";
    private static final int MAX_WHOLESALE = 5;
    private static final int DEFAULT_ETALASE_ID = -1;
    private final Locale dollarLocale = Locale.US;
    private final Locale idrLocale = new Locale("in", "ID");
    private RecyclerView recyclerViewWholesale;
    private WholesaleAdapter wholesaleAdapter;
    private ImageButton editPriceImageButton;
    private SpinnerCounterInputView priceSpinnerCounterInputView;
    private ExpandableOptionSwitch wholesaleExpandableOptionSwitch;
    private SpinnerCounterInputView weightSpinnerCounterInputView;
    private CounterInputView minimumOrderCounterInputView;
    private SpinnerTextView stockStatusSpinnerTextView;
    private ExpandableOptionSwitch stockTotalExpandableOptionSwitch;
    private CounterInputView stockTotalCounterInputView;
    private LabelView etalaseLabelView;
    private SpinnerTextView conditionSpinnerTextView;
    private SpinnerTextView insuranceSpinnerTextView;
    private SpinnerTextView freeReturnsSpinnerTextView;
    private TextView textViewAddWholesale;
    private long etalaseId;
    private boolean goldMerchant;
    private CurrencyIdrTextWatcher idrTextWatcher;
    private CurrencyUsdTextWatcher usdTextWatcher;
    private Listener listener;
    @CurrencyTypeDef
    private int currencyType = CurrencyTypeDef.TYPE_IDR;
    private NumberFormat formatter;

    public ProductDetailViewHolder(View view) {
        etalaseId = DEFAULT_ETALASE_ID;
        determineFormatter();
        editPriceImageButton = (ImageButton) view.findViewById(R.id.image_button_edit_price);
        priceSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_price);
        wholesaleExpandableOptionSwitch = (ExpandableOptionSwitch) view.findViewById(R.id.expandable_option_switch_wholesale);
        weightSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_weight);
        minimumOrderCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_minimum_order);
        stockStatusSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_stock_status);
        stockTotalExpandableOptionSwitch = (ExpandableOptionSwitch) view.findViewById(R.id.expandable_option_switch_stock_total);
        stockTotalCounterInputView = (CounterInputView) view.findViewById(R.id.counter_input_view_stock_total);
        etalaseLabelView = (LabelView) view.findViewById(R.id.label_view_shop_window);
        conditionSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_condition);
        insuranceSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_insurance);
        freeReturnsSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_free_returns);
        editPriceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPriceDialog();
            }
        });
        idrTextWatcher = new CurrencyIdrTextWatcher(priceSpinnerCounterInputView.getCounterEditText()) {
            @Override
            public void onNumberChanged(float number) {
                isPriceValid();
                clearWholesaleItems();
            }
        };
        usdTextWatcher = new CurrencyUsdTextWatcher(priceSpinnerCounterInputView.getCounterEditText()) {
            @Override
            public void onNumberChanged(float number) {
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

            private void onItemClicked (int position) {
                priceSpinnerCounterInputView.setCounterValue(Float.parseFloat(priceSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                EditText editText = priceSpinnerCounterInputView.getCounterEditText();
                editText.setSelection(editText.getText().length());
                priceSpinnerCounterInputView.setCounterError(null);

                if (!goldMerchant && priceSpinnerCounterInputView.getSpinnerValue(position).equalsIgnoreCase(priceSpinnerCounterInputView.getContext().getString(R.string.product_currency_value_usd))) {
                    priceSpinnerCounterInputView.setSpinnerValue(priceSpinnerCounterInputView.getContext().getString(R.string.product_currency_value_idr));
                    Snackbar.make(priceSpinnerCounterInputView.getRootView().findViewById(android.R.id.content), R.string.product_error_must_be_gold_merchant, Snackbar.LENGTH_LONG)
                            .setActionTextColor(ContextCompat.getColor(priceSpinnerCounterInputView.getContext(), R.color.green_400))
                            .show();
                }
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

        weightSpinnerCounterInputView.addTextChangedListener(new NumberTextWatcher(weightSpinnerCounterInputView.getCounterEditText(), weightSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)) {
            @Override
            public void onNumberChanged(float number) {
                if (isWeightValid()) {
                    weightSpinnerCounterInputView.setCounterError(null);
                }
            }
        });
        weightSpinnerCounterInputView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weightSpinnerCounterInputView.setCounterValue(Float.parseFloat(weightSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                weightSpinnerCounterInputView.post(new Runnable() {
                    @Override
                    public void run() {
                        isWeightValid();
                    }
                });
            }
        });
        etalaseLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onEtalaseViewClicked(etalaseId);
                }
            }
        });

        textViewAddWholesale = (TextView) view.findViewById(R.id.text_view_add_wholesale);
        textViewAddWholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.startAddWholeSaleDialog(getBaseValue(), currencyType, getPreviousValue());
                }
            }
        });

        freeReturnsSpinnerTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onFreeReturnChecked(false);
            }
        });
        stockStatusSpinnerTextView.setOnItemChangeListener(new SpinnerTextView.OnItemChangeListener() {
            @Override
            public void onItemChanged(int position, String entry, String value) {
                if (value.equalsIgnoreCase(stockStatusSpinnerTextView.getContext().getString(R.string.product_stock_not_available_value))) {
                    stockTotalExpandableOptionSwitch.setExpand(false);
                    stockTotalExpandableOptionSwitch.setEnabled(false);
                } else {
                    stockTotalExpandableOptionSwitch.setEnabled(true);
                }
            }
        });
        stockTotalExpandableOptionSwitch.setEnabled(false);
        stockTotalExpandableOptionSwitch.setExpandableListener(new BaseExpandableOption.ExpandableListener() {
            @Override
            public void onExpandViewChange(boolean isExpand) {
                listener.onTotalStockUpdated(isExpand ? (int) stockTotalCounterInputView.getFloatValue() : 0);
            }
        });
        stockTotalCounterInputView.addTextChangedListener(new NumberTextWatcher(stockTotalCounterInputView.getEditText()) {
            @Override
            public void onNumberChanged(float number) {
                if (isTotalStockValid()) {
                    stockTotalCounterInputView.setError(null);
                }
                listener.onTotalStockUpdated((int) number);
            }
        });
        recyclerViewWholesale = (RecyclerView) view.findViewById(R.id.recycler_view_wholesale);
        recyclerViewWholesale.setLayoutManager(new LinearLayoutManager(recyclerViewWholesale.getContext(), LinearLayoutManager.VERTICAL, false));
        wholesaleAdapter = new WholesaleAdapter();
        wholesaleAdapter.setListener(this);
        recyclerViewWholesale.setAdapter(wholesaleAdapter);
    }

    public static Pair<Float, Float> minMaxPrice(Context context, int currencyType) {
        String spinnerValue = null;
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                spinnerValue = Integer.toString(CurrencyTypeDef.TYPE_USD);
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                spinnerValue = Integer.toString(CurrencyTypeDef.TYPE_IDR);
                break;
        }
        String minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_minimum_price_rp));
        String maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_maximum_price_rp));
        if (spinnerValue.equalsIgnoreCase(context.getString(R.string.product_currency_value_usd))) {
            minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_minimum_price_usd));
            maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_maximum_price_usd));
        }
        float minPrice = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(minPriceString));
        float maxPrice = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(maxPriceString));
        return Pair.create(minPrice, maxPrice);
    }

    private void showEditPriceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(editPriceImageButton.getContext(),
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.title_product_confirmation_change_wholesale_price);
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

    public void updateViewFreeReturn(boolean isFreeReturn) {
        if (isFreeReturn) {
            freeReturnsSpinnerTextView.setVisibility(View.VISIBLE);
        } else {
            freeReturnsSpinnerTextView.setVisibility(View.GONE);
        }
    }

    public int getPriceUnit() {
        return Integer.parseInt(priceSpinnerCounterInputView.getSpinnerValue());
    }

    public void setPriceUnit(@CurrencyTypeDef int unit) {
        priceSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public float getPriceValue() {
        return priceSpinnerCounterInputView.getCounterValue();
    }

    public void setPriceValue(float price) {
        priceSpinnerCounterInputView.setCounterValue(price);
    }

    public int getWeightUnit() {
        return Integer.parseInt(weightSpinnerCounterInputView.getSpinnerValue());
    }

    public void setWeightUnit(int unit) {
        weightSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public int getWeightValue() {
        return (int) weightSpinnerCounterInputView.getCounterValue();
    }

    public void setWeightValue(float value) {
        weightSpinnerCounterInputView.setCounterValue(value);
    }

    public int getMinimumOrder() {
        return (int) minimumOrderCounterInputView.getFloatValue();
    }

    public void setMinimumOrder(float value) {
        minimumOrderCounterInputView.setValue(value);
    }

    public int getStatusStock() {
        return Integer.parseInt(stockStatusSpinnerTextView.getSpinnerValue());
    }

    public void setStockStatus(int unit) {
        stockStatusSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    public boolean isStockManaged() {
        return stockTotalExpandableOptionSwitch.isExpanded();
    }

    public void setStockManaged(boolean stockManaged) {
        stockTotalExpandableOptionSwitch.setExpand(stockManaged);
    }

    public int getTotalStock() {
        return (int) stockTotalCounterInputView.getFloatValue();
    }

    public void setTotalStock(float value) {
        stockTotalCounterInputView.setValue(value);
    }

    public int getCondition() {
        return Integer.parseInt(conditionSpinnerTextView.getSpinnerValue());
    }

    public void setCondition(int unit) {
        conditionSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    public int getInsurance() {
        return Integer.parseInt(insuranceSpinnerTextView.getSpinnerValue());
    }

    public void setInsurance(int unit) {
        insuranceSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    public int getFreeReturns() {
        if(freeReturnsSpinnerTextView.getVisibility() != View.VISIBLE){
            return Integer.parseInt(freeReturnsSpinnerTextView.getContext().getString(R.string.product_free_return_values_inactive));
        }else {
            return Integer.parseInt(freeReturnsSpinnerTextView.getSpinnerValue());
        }
    }

    public void setFreeReturn(int unit) {
        freeReturnsSpinnerTextView.setSpinnerValue(String.valueOf(unit));
    }

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseLabelView.getValue();
    }

    public void setEtalaseName(String name) {
        this.etalaseLabelView.setContent(MethodChecker.fromHtml(name));
    }

    public void addWholesaleItem(WholesaleModel wholesaleModel) {
        wholesaleAdapter.addItem(wholesaleModel);
        wholesaleAdapter.notifyDataSetChanged();
        updateWholesaleButton();
    }

    private WholesaleModel getBaseValue() {
        return new WholesaleModel(1, 1, getPriceValue());
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            etalaseId = data.getIntExtra(EtalasePickerActivity.ETALASE_ID, -1);
            String etalaseNameString = data.getStringExtra(EtalasePickerActivity.ETALASE_NAME);
            setEtalaseName(etalaseNameString);
        }
    }

    private boolean isPriceValid() {
        Pair<Float, Float> minMaxPrice = minMaxPrice(
                priceSpinnerCounterInputView.getContext(),
                Integer.parseInt(priceSpinnerCounterInputView.getSpinnerValue()));

        if (minMaxPrice.first > getPriceValue() || getPriceValue() > minMaxPrice.second) {
            priceSpinnerCounterInputView.setCounterError(priceSpinnerCounterInputView.getContext().getString(R.string.product_error_product_price_not_valid,
                    formatter.format(minMaxPrice.first), formatter.format(minMaxPrice.second)));
            wholesaleExpandableOptionSwitch.setVisibility(View.GONE);
            return false;
        }
        priceSpinnerCounterInputView.setCounterError(null);
        wholesaleExpandableOptionSwitch.setVisibility(View.VISIBLE);
        return true;
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

    private boolean isWeightValid() {
        String minWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_minimum_weight_gram));
        String maxWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_maximum_weight_gram));
        if (weightSpinnerCounterInputView.getSpinnerValue().equalsIgnoreCase(weightSpinnerCounterInputView.getContext().getString(R.string.product_weight_value_kg))) {
            minWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_minimum_weight_kg));
            maxWeightString = CurrencyFormatHelper.removeCurrencyPrefix(weightSpinnerCounterInputView.getContext().getString(R.string.product_maximum_weight_kg));
        }
        float minWeight = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(minWeightString));
        float maxWeight = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(maxWeightString));
        if (minWeight > getWeightValue() || getWeightValue() > maxWeight) {
            weightSpinnerCounterInputView.setCounterError(weightSpinnerCounterInputView.getContext().getString(R.string.product_error_product_weight_not_valid, minWeightString, maxWeightString));
            return false;
        }
        return true;
    }

    private boolean isTotalStockValid() {
        if (!stockTotalExpandableOptionSwitch.isExpanded()) {
            return true;
        }
        String minStockString = CurrencyFormatHelper.removeCurrencyPrefix(stockTotalCounterInputView.getContext().getString(R.string.product_minimum_total_stock));
        String maxStockString = CurrencyFormatHelper.removeCurrencyPrefix(stockTotalCounterInputView.getContext().getString(R.string.product_maximum_total_stock));
        float minStock = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(minStockString));
        float maxStock = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(maxStockString));
        if (minStock > getTotalStock() || getTotalStock() > maxStock) {
            stockTotalCounterInputView.setError(stockTotalCounterInputView.getContext().getString(R.string.product_error_total_stock_not_valid, minStockString, maxStockString));
            return false;
        }
        return true;
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
    public Pair<Boolean, String> isDataValid() {
        if (!isPriceValid()) {
            priceSpinnerCounterInputView.requestFocus();
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_MANDATORY_PRICE);
        }
        if (!isWeightValid()) {
            weightSpinnerCounterInputView.requestFocus();
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_MANDATORY_WEIGHT);
        }
        if(!isMinPurchaseValid()){
            minimumOrderCounterInputView.requestFocus();
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_MANDATORY_MIN_PURCHASE);
        }
        if (!isTotalStockValid()) {
            stockTotalCounterInputView.requestFocus();
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_MANDATORY_STOCK_STATUS);
        }
        if (getEtalaseId() < 0) {
            etalaseLabelView.requestFocus();
            Snackbar.make(etalaseLabelView.getRootView().findViewById(android.R.id.content), R.string.product_error_product_etalase_empty, Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(etalaseLabelView.getContext(), R.color.green_400))
                    .show();
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_MANDATORY_SHOWCASE);
        }
        return new Pair<>(true, "");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(BUNDLE_ETALASE_ID, etalaseId);
        savedInstanceState.putString(BUNDLE_ETALASE_NAME, etalaseLabelView.getValue());
        savedInstanceState.putBoolean(IS_ACTIVE_STOCK, stockTotalExpandableOptionSwitch.isEnabled());
        savedInstanceState.putBoolean(IS_ACTIVE_WHOLESALE, wholesaleExpandableOptionSwitch.isEnabled());
        savedInstanceState.putParcelableArrayList(KEY_WHOLESALE,
                new ArrayList<Parcelable>(wholesaleAdapter.getWholesaleModels()));
        savedInstanceState.putInt(BUNDLE_SPINNER_POSITION, priceSpinnerCounterInputView.getSpinnerPosition());
        savedInstanceState.putString(BUNDLE_COUNTER_PRICE, priceSpinnerCounterInputView.getCounterEditText().getText().toString());
        savedInstanceState.putBoolean(IS_WHOLESALE_VISIBLE, wholesaleExpandableOptionSwitch.getVisibility() == View.VISIBLE);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        etalaseId = savedInstanceState.getLong(BUNDLE_ETALASE_ID, DEFAULT_ETALASE_ID);
        if (!TextUtils.isEmpty(savedInstanceState.getString(BUNDLE_ETALASE_NAME))) {
            etalaseLabelView.setContent(savedInstanceState.getString(BUNDLE_ETALASE_NAME));
        }

        stockTotalExpandableOptionSwitch.setEnabled(savedInstanceState.getBoolean(IS_ACTIVE_STOCK));
        wholesaleExpandableOptionSwitch.setEnabled(savedInstanceState.getBoolean(IS_ACTIVE_WHOLESALE));
        ArrayList<WholesaleModel> wholesaleModels = savedInstanceState.getParcelableArrayList(KEY_WHOLESALE);
        if (wholesaleModels == null) {
            wholesaleModels = new ArrayList<>();
        }
        wholesaleAdapter.addAllWholeSale(wholesaleModels);
        wholesaleAdapter.notifyDataSetChanged();

        int spinnerPricePosition = savedInstanceState.getInt(BUNDLE_SPINNER_POSITION, 0);
        priceSpinnerCounterInputView.setSpinnerPosition(spinnerPricePosition);

        String counterPriceValue = savedInstanceState.getString(BUNDLE_COUNTER_PRICE, "0");
        priceSpinnerCounterInputView.getCounterEditText().setText(counterPriceValue);

        boolean isWholeSaleVisible = savedInstanceState.getBoolean(IS_WHOLESALE_VISIBLE, false);
        wholesaleExpandableOptionSwitch.setVisibility(isWholeSaleVisible? View.VISIBLE: View.GONE);
    }

    public boolean isMinPurchaseValid() {
        int purchaseMinimum = Integer.parseInt(minimumOrderCounterInputView.getValueText());

        if (purchaseMinimum <= 0) {
            minimumOrderCounterInputView.setError(minimumOrderCounterInputView.getContext().
                    getString(R.string.product_error_product_minimum_order_not_valid));
            return false;
        }
        minimumOrderCounterInputView.setError(null);
        return true;
    }

    public interface Listener {

        /**
         * @param fixedPrice             means for fixed price.
         * @param currencyType           {@link CurrencyTypeDef}
         * @param previousWholesalePrice previousWholesalePrice
         */
        void startAddWholeSaleDialog(WholesaleModel fixedPrice,
                                     @CurrencyTypeDef int currencyType,
                                     WholesaleModel previousWholesalePrice);

        void onTotalStockUpdated(int total);

        void onEtalaseViewClicked(long etalaseId);

        void onFreeReturnChecked(boolean checked);
    }
}