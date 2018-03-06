package com.tokopedia.seller.product.variant.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelSwitch;
import com.tokopedia.seller.common.widget.VerticalLabelView;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.StockTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.view.widget.VariantImageView;

import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDetailLeafFragment extends BaseVariantImageFragment {

    private OnProductVariantDetailLeafFragmentListener listener;
    private LabelSwitch labelSwitchStatus;

    private ProductVariantCombinationViewModel productVariantCombinationViewModel;
    private @CurrencyTypeDef
    int currencyType;
    private CurrencyTextWatcher currencyTextWatcher;

    private VariantImageView variantImageView;

    public interface OnProductVariantDetailLeafFragmentListener {
        void onSubmitVariant();

        ProductVariantCombinationViewModel getProductVariantCombinationViewModel();

        String getVariantName();

        @CurrencyTypeDef
        int getCurrencyTypeDef();

        ProductVariantOptionChild getProductVariantChild();

        boolean needRetainImage();

        void onImageChanged();

        double getDefaultPrice();

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
        View buttonSave = view.findViewById(R.id.button_save);

        SpinnerCounterInputView spinnerCounterInputView = view.findViewById(R.id.spinner_counter_input_view_price);
        spinnerCounterInputView.getSpinnerTextView().setEnabled(false);
        spinnerCounterInputView.getSpinnerTextView().setClickable(false);
        spinnerCounterInputView.setSpinnerValue(String.valueOf(currencyType));

        spinnerCounterInputView.removeDefaultTextWatcher();
        EditText priceEditText = spinnerCounterInputView.getCounterEditText();
        priceEditText.removeTextChangedListener(currencyTextWatcher);
        currencyTextWatcher = new CurrencyTextWatcher(
                priceEditText,
                null,
                currencyType == CurrencyTypeDef.TYPE_USD);
        currencyTextWatcher.setOnNumberChangeListener(new CurrencyTextWatcher.OnNumberChangeListener() {
            @Override
            public void onNumberChanged(double v) {
                productVariantCombinationViewModel.setPriceVar(v);
                //TODO check validation. If any error, setError to edittext.
            }
        });
        priceEditText.addTextChangedListener(currencyTextWatcher);
        priceEditText.setText(
                CurrencyFormatUtil.convertPriceValue(productVariantCombinationViewModel.getPriceVar(),
                        currencyType == CurrencyTypeDef.TYPE_USD));

        lvTitle.setTitle(listener.getVariantName());
        lvTitle.setSummary(productVariantCombinationViewModel.getLeafString());

        labelSwitchStatus.setListenerValue(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setStockLabel(isChecked);
                productVariantCombinationViewModel.setActive(isChecked);
                //TODO set stock value
            }
        });

        labelSwitchStatus.setChecked(productVariantCombinationViewModel.isActive());
        setStockLabel(productVariantCombinationViewModel.isActive());

//        frameImage = view.findViewById(R.id.frame_image);
//        ivVariant = view.findViewById(R.id.image_view);
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

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO check validation before submit
                listener.onSubmitVariant();
            }
        });
        view.requestFocus();
        return view;
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