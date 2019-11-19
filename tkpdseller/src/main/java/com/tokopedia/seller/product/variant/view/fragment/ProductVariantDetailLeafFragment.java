package com.tokopedia.seller.product.variant.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.design.text.CounterInputView;
import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher;
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef;
import com.tokopedia.product.manage.item.common.util.CurrencyUsdTextWatcher;
import com.tokopedia.product.manage.item.common.util.StockTypeDef;
import com.tokopedia.product.manage.item.main.base.data.model.VariantPictureViewModel;
import com.tokopedia.product.manage.item.utils.LabelSwitch;
import com.tokopedia.product.manage.item.utils.ProductPriceRangeUtils;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.VerticalLabelView;
import com.tokopedia.seller.product.variant.view.widget.VariantImageView;

import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDetailLeafFragment extends BaseVariantImageFragment {

    public static final int MAX_STOCK = 999999;
    public static final int MIN_STOCK = 1;
    public static final int MIN_STOCK_EDIT = 0;
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

        boolean isAddStatus();

        @CurrencyTypeDef
        int getCurrencyTypeDef();

        ProductVariantOptionChild getProductVariantChild();

        boolean needRetainImage();

        void onImageChanged();

        @StockTypeDef
        int getStockType();

        boolean isOfficialStore();

        boolean hasWholesale();
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
    public void refreshImageView() {
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
        labelSwitchStatus = view.findViewById(R.id.label_switch_product_status);
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
        if (listener.hasWholesale()) {
            counterInputPrice.setEnabled(false);
        }


        lvTitle.setTitle(listener.getVariantName());
        lvTitle.setSummary(productVariantCombinationViewModel.getLeafString());

        counterInputViewStock.setVisibility(View.VISIBLE);

        if (listener.isAddStatus()) {
            labelSwitchStatus.setChecked(true);
            labelSwitchStatus.setClickable(false);
            labelSwitchStatus.disableSwitch();
            counterInputViewStock.setValue(1);
            productVariantCombinationViewModel.setStock(1);

        } else {
            labelSwitchStatus.setChecked(productVariantCombinationViewModel.getStock() != 0);
            counterInputViewStock.setValue(productVariantCombinationViewModel.getStock());
        }

        counterInputViewStock.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                int stock = (int) counterInputViewStock.getDoubleValue();

                String sString = StringUtils.omitNonNumeric(s.toString());
                if (TextUtils.isEmpty(sString) || sString.equals("0")) {
                    if (listener.isAddStatus()) {
                        stock = 1;
                    }
                }

                checkStockValid(stock);
                if (productVariantCombinationViewModel.getStock() == stock) {
                    return;
                }

                productVariantCombinationViewModel.setStock(stock);
            }
        });

        labelSwitchStatus.setListenerValue((buttonView, isChecked) -> onLabelSwitchStatusChanged(isChecked));

        labelSwitchStatus.setChecked(productVariantCombinationViewModel.isActive());
        onLabelSwitchStatusChanged(productVariantCombinationViewModel.isActive());

        variantImageView = view.findViewById(R.id.variant_image_view);

        ProductVariantOptionChild childLvl1Model = listener.getProductVariantChild();
        if (childLvl1Model == null) {
            variantImageView.setVisibility(View.GONE);
        } else {
            variantImageView.setVisibility(View.VISIBLE);
            refreshImageView();
            variantImageView.setOnImageClickListener(new VariantImageView.OnImageClickListener() {
                @Override
                public void onImageVariantClicked() {
                    if (getProductVariantOptionChild().getProductPictureViewModelList() == null ||
                            getProductVariantOptionChild().getProductPictureViewModelList().size() == 0 ||
                            getProductVariantOptionChild().getProductPictureViewModelList().get(0) == null) {
                        showAddImageDialog();
                    } else {
                        showEditImageDialog(getProductVariantOptionChild().getProductPictureViewModelList().get(0).getUriOrPath());
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
        int minStock;
        if (listener.isAddStatus()) {
            minStock = MIN_STOCK;
        } else {
            minStock = MIN_STOCK_EDIT;
        }

        if (listener.getProductVariantCombinationViewModel().isActive() &&
                isStockLimited()) {
            if (stock < minStock || stock > MAX_STOCK) {counterInputViewStock.setError(getContext().getString(R.string.product_error_product_minimum_order_not_valid,
                        String.valueOf(minStock),
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
        if (!checkStockValid((int) counterInputViewStock.getDoubleValue())) {
            CommonUtils.hideKeyboard(getActivity(), getView());
            return;
        }
        String sku = etSku.getText().toString();
        listener.getProductVariantCombinationViewModel().setSku(sku);

        listener.onSubmitVariant();
    }

    private boolean checkPriceValid(double value) {
        if (ProductPriceRangeUtils.isPriceValid(value, currencyType, listener.isOfficialStore())) {
            counterInputPrice.setCounterError(null);
            return true;
        }
        counterInputPrice.setCounterError(getContext().getString(R.string.product_error_product_price_not_valid,
                ProductPriceRangeUtils.getMinPriceString(currencyType, listener.isOfficialStore()),
                ProductPriceRangeUtils.getMaxPriceString(currencyType, listener.isOfficialStore())));
        return false;
    }

    private void onLabelSwitchStatusChanged(boolean isChecked) {
        setStockLabel(isChecked);
        if (listener.isAddStatus() && ((int) counterInputViewStock.getDoubleValue()) == 0) {
            counterInputViewStock.setValue(1);
        }
        productVariantCombinationViewModel.setActive(isChecked);
        counterInputViewStock.setEnabled(true);

    }

    private boolean isStockLimited() {
        return listener.getStockType() == StockTypeDef.TYPE_ACTIVE_LIMITED;
    }

    private void setStockLabel(boolean isChecked) {
        if (isChecked) {
            labelSwitchStatus.subTitleText(getString(R.string.label_always_active));
        } else {
            labelSwitchStatus.subTitleText(getString(R.string.label_always_nonactive));
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }

    protected void onAttachListener(Context context) {
        listener = (OnProductVariantDetailLeafFragmentListener) context;
    }

}