package com.tokopedia.seller.product.variant.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.design.text.CounterInputView;
import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelSwitch;
import com.tokopedia.seller.common.widget.VerticalLabelView;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.StockTypeDef;
import com.tokopedia.seller.product.edit.utils.ViewUtils;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.view.widget.VariantImageView;
import com.tokopedia.seller.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.util.CurrencyUsdTextWatcher;

import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDetailLeafFragment extends BaseVariantImageFragment {

    public static final int MAX_STOCK = 10000;
    public static final int MIN_STOCK = 1;
    private OnProductVariantDetailLeafFragmentListener listener;
    private LabelSwitch labelSwitchStatus;

    private ProductVariantCombinationViewModel productVariantCombinationViewModel;
    private @CurrencyTypeDef
    int currencyType;

    private NumberTextWatcher numberTextWatcher;

    private VariantImageView variantImageView;
    private CounterInputView counterInputViewStock;
    private EditText etSku;
    private TextView tvVariantStockInfo;
    private SpinnerCounterInputView counterInputPrice;

    public interface OnProductVariantDetailLeafFragmentListener {
        void onSubmitVariant();

        ProductVariantCombinationViewModel getProductVariantCombinationViewModel();

        String getVariantName();

        @CurrencyTypeDef
        int getCurrencyTypeDef();

        ProductVariantOptionChild getProductVariantChild();

        boolean needRetainImage();

        void onImageChanged();

        @StockTypeDef
        int getStockType();

        boolean isOfficialStore();
    }

    public static ProductVariantDetailLeafFragment newInstance() {
        return new ProductVariantDetailLeafFragment();
    }

    @Override
    public boolean needRetainImage() {
        return listener.needRetainImage();
    }

    @Override
    public ProductVariantOptionChild getProductVariantOptionChild() {
        return listener.getProductVariantChild();
    }

    @Override
    public void refreshVariantImage() {
        refreshInitialVariantImage();
        listener.onImageChanged();
    }

    public void refreshInitialVariantImage() {
        ProductVariantOptionChild childLvl1Model = listener.getProductVariantChild();
        List<VariantPictureViewModel> productPictureViewModelList = childLvl1Model.getProductPictureViewModelList();
        VariantPictureViewModel pictureViewModel = null;
        if (productPictureViewModelList != null && productPictureViewModelList.size() > 0) {
            pictureViewModel = productPictureViewModelList.get(0);
        }
        variantImageView.setImage(pictureViewModel, childLvl1Model.getHex());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productVariantCombinationViewModel = listener.getProductVariantCombinationViewModel();
        currencyType = listener.getCurrencyTypeDef();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_variant_detail_leaf, container, false);
        VerticalLabelView lvTitle = view.findViewById(R.id.lv_title);
        labelSwitchStatus = (LabelSwitch) view.findViewById(R.id.label_switch_product_status);
        counterInputViewStock = view.findViewById(R.id.counter_input_view_stock_total);
        etSku = view.findViewById(R.id.edit_text_sku);
        tvVariantStockInfo = view.findViewById(R.id.tv_variant_info);
        View buttonSave = view.findViewById(R.id.button_save);

        counterInputPrice = view.findViewById(R.id.spinner_counter_input_view_price);
        counterInputPrice.getSpinnerTextView().setEnabled(false);
        counterInputPrice.getSpinnerTextView().setClickable(false);
        counterInputPrice.setSpinnerValue(String.valueOf(currencyType));

        counterInputPrice.removeTextChangedListener(numberTextWatcher);
        if (currencyType == CurrencyTypeDef.TYPE_USD) {
            numberTextWatcher = new CurrencyUsdTextWatcher(counterInputPrice.getCounterEditText(), "0.00") {
                @Override
                public void onNumberChanged(double number) {
                    onNumberPriceChanged();
                }
            };
        } else {
            numberTextWatcher = new CurrencyIdrTextWatcher(counterInputPrice.getCounterEditText(), "0") {
                @Override
                public void onNumberChanged(double number) {
                    onNumberPriceChanged();
                }
            };
        }
        counterInputPrice.addTextChangedListener(numberTextWatcher);
        counterInputPrice.setCounterValue(productVariantCombinationViewModel.getPriceVar());

        lvTitle.setTitle(listener.getVariantName());
        lvTitle.setSummary(productVariantCombinationViewModel.getLeafString());

        if (isStockLimited()) {
            counterInputViewStock.setVisibility(View.VISIBLE);
            counterInputViewStock.setValue(productVariantCombinationViewModel.getStock());
            counterInputViewStock.addTextChangedListener(new AfterTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    counterInputViewStock.removeTextChangedListener(this);
                    String sString = StringUtils.omitNonNumeric(s.toString());
                    int stock;
                    if (TextUtils.isEmpty(sString)) {
                        stock = 1;
                    } else {
                        stock = Integer.parseInt(sString);
                    }
                    counterInputViewStock.setValue(stock);
                    checkStockValid(stock);
                    counterInputViewStock.addTextChangedListener(this);

                    if (productVariantCombinationViewModel.getStock() == stock) {
                        return;
                    }
                    productVariantCombinationViewModel.setStock(stock);
                    if (stock == 0) {
                        labelSwitchStatus.setChecked(false);
                    } else {
                        labelSwitchStatus.setChecked(true);
                    }
                }
            });
        } else {
            counterInputViewStock.setVisibility(View.GONE);
        }

        labelSwitchStatus.setListenerValue(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onLabelSwitchStatusChanged(isChecked);
            }
        });

        labelSwitchStatus.setChecked(productVariantCombinationViewModel.isActive());
        onLabelSwitchStatusChanged(productVariantCombinationViewModel.isActive());

        variantImageView = view.findViewById(R.id.variant_image_view);

        ProductVariantOptionChild childLvl1Model = listener.getProductVariantChild();
        if (childLvl1Model == null) {
            variantImageView.setVisibility(View.GONE);
        } else {
            variantImageView.setVisibility(View.VISIBLE);
            refreshVariantImage();
            variantImageView.setOnImageClickListener(new VariantImageView.OnImageClickListener() {
                @Override
                public void onImageVariantClicked() {
                    if (getProductVariantOptionChild().getProductPictureViewModelList() == null ||
                            getProductVariantOptionChild().getProductPictureViewModelList().size() == 0) {
                        showAddImageDialog();
                    } else {
                        showEditImageDialog(getProductVariantOptionChild().getProductPictureViewModelList().get(0));
                    }
                }
            });
        }

        if (savedInstanceState == null) {
            etSku.setText(productVariantCombinationViewModel.getSku());
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSaveClicked();
            }
        });
        view.requestFocus();
        return view;
    }

    private boolean checkStockValid(int stock) {
        if (listener.getProductVariantCombinationViewModel().isActive() &&
                isStockLimited()) {
            if (stock < MIN_STOCK || stock > MAX_STOCK) {
                counterInputViewStock.setError(getContext().getString(R.string.product_error_product_minimum_order_not_valid,
                        getContext().getString(R.string.product_minimum_total_stock),
                        getContext().getString(R.string.product_maximum_total_stock)));
                return false;
            }
        }
        counterInputViewStock.setError(null);
        return true;
    }

    private void onNumberPriceChanged() {
        double counterValue = counterInputPrice.getCounterValue();
        productVariantCombinationViewModel.setPriceVar(counterValue);
        checkPriceValid(counterValue);
    }

    private void onButtonSaveClicked() {
        if (!checkPriceValid(counterInputPrice.getCounterValue())) {
            CommonUtils.hideKeyboard(getActivity(), getView());
            return;
        }
        if (!checkStockValid((int)counterInputViewStock.getDoubleValue())) {
            CommonUtils.hideKeyboard(getActivity(), getView());
            return;
        }
        String sku = etSku.getText().toString();
        listener.getProductVariantCombinationViewModel().setSku(sku);

        listener.onSubmitVariant();
    }

    private boolean checkPriceValid(double value) {
        if (ViewUtils.isPriceValid(value, currencyType, listener.isOfficialStore())) {
            counterInputPrice.setCounterError(null);
            return true;
        }
        counterInputPrice.setCounterError(getContext().getString(R.string.product_error_product_price_not_valid,
                ViewUtils.getMinPriceString(currencyType, listener.isOfficialStore()),
                ViewUtils.getMaxPriceString(currencyType, listener.isOfficialStore())));
        return false;
    }

    private void onLabelSwitchStatusChanged(boolean isChecked) {
        setStockLabel(isChecked);
        productVariantCombinationViewModel.setActive(isChecked);

        // counter input for stock only visible for stock limited only
        if (isStockLimited()) {
            if (isChecked) {
                counterInputViewStock.setValue(1);
                counterInputViewStock.setEnabled(true);
            } else {
                counterInputViewStock.setValue(0);
                counterInputViewStock.setEnabled(false);
            }
        } else {
            productVariantCombinationViewModel.setStock(0);
            if (isChecked) {
                tvVariantStockInfo.setVisibility(View.VISIBLE);
            } else {
                tvVariantStockInfo.setVisibility(View.GONE);
            }
        }
    }

    private boolean isStockLimited() {
        return listener.getStockType() == StockTypeDef.TYPE_ACTIVE_LIMITED;
    }

    private void setStockLabel(boolean isChecked) {
        if (isChecked) {
            labelSwitchStatus.setSummary(getString(R.string.product_variant_status_available));
        } else {
            labelSwitchStatus.setSummary(getString(R.string.product_variant_status_not_available));
        }
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
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
        listener = (OnProductVariantDetailLeafFragmentListener) context;
    }

}