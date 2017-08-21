package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelSwitch;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoView;
import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.Option;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.VariantOption;
import com.tokopedia.seller.product.variant.data.model.varianthelper.ProductVariantHelper;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantSubmit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductAdditionalInfoViewHolder extends ProductViewHolder {

    public static final int REQUEST_CODE_GET_VIDEO = 1;
    public static final int REQUEST_CODE_VARIANT = 2;

    public static final int INACTIVE_PREORDER = -1;

    public static final String SAVED_PRD_VARIANT_SUBMIT = "svd_variant";
    public static final String SAVED_VARIANT_CAT = "svd_var";

    private EditText descriptionEditText;
    private LabelView labelAddVideoView;
    private LabelView variantLabelView;
    private ExpandableOptionSwitch preOrderExpandableOptionSwitch;
    private SpinnerCounterInputView preOrderSpinnerCounterInputView;
    private LabelSwitch shareLabelSwitch;
    private Listener listener;
    private boolean goldMerchant;

    private ProductVariantSubmit productVariantSubmit;
    private ArrayList<ProductVariantByCatModel> productVariantByCatModelList;
    private ProductVariantByPrdModel productVariantByPrdModel;

    /**
     * this prevent duplication at videoIdList;
     */
    private List<String> videoIdList;

    public ProductAdditionalInfoViewHolder(View view) {
        videoIdList = new ArrayList<>();
        descriptionEditText = (EditText) view.findViewById(R.id.edit_text_description);
        labelAddVideoView = (LabelView) view.findViewById(R.id.label_add_video_view);
        variantLabelView = (LabelView) view.findViewById(R.id.label_view_variant);
        preOrderExpandableOptionSwitch = (ExpandableOptionSwitch) view.findViewById(R.id.expandable_option_switch_pre_order);
        preOrderSpinnerCounterInputView = (SpinnerCounterInputView) view.findViewById(R.id.spinner_counter_input_view_pre_order);
        shareLabelSwitch = (LabelSwitch) view.findViewById(R.id.label_switch_share);
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onDescriptionTextChanged(editable.toString().trim());
            }
        });
        labelAddVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.checkNotNull(listener)) {
                    if (!goldMerchant && GlobalConfig.isSellerApp()) {
                        UnifyTracking.eventClickVideoAddProduct();
                        listener.showDialogMoveToGM(R.string.add_product_label_alert_dialog_video);
                    } else {
                        listener.startYoutubeVideoActivity(new ArrayList<>(videoIdList));
                    }
                }
            }
        });
        variantLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.startProductVariantActivity(productVariantByCatModelList);
            }
        });
        preOrderExpandableOptionSwitch.setExpandableListener(new BaseExpandableOption.ExpandableListener() {
            @Override
            public void onExpandViewChange(boolean isExpand) {
                if (!isExpand) {
                    preOrderSpinnerCounterInputView.setCounterValue(Double.parseDouble(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                }
            }
        });
        preOrderSpinnerCounterInputView.addTextChangedListener(new NumberTextWatcher(preOrderSpinnerCounterInputView.getCounterEditText()) {
            @Override
            public void onNumberChanged(double number) {
                if (isPreOrderValid()) {
                    preOrderSpinnerCounterInputView.setCounterError(null);
                }
            }
        });
        preOrderSpinnerCounterInputView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                preOrderSpinnerCounterInputView.setCounterValue(Double.parseDouble(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                preOrderSpinnerCounterInputView.setCounterError(null);
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void updateViewGoldMerchant(boolean isShown) {
        goldMerchant = isShown;
        if (isShown || GlobalConfig.isSellerApp()) {
            labelAddVideoView.setVisibility(View.VISIBLE);
        } else {
            videoIdList.clear();
            labelAddVideoView.setVisibility(View.GONE);
        }
    }

    public String getDescription() {
        return descriptionEditText.getText().toString();
    }

    public void setDescription(String description) {
        descriptionEditText.setText(MethodChecker.fromHtml(description));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GET_VIDEO:
                if (resultCode == Activity.RESULT_OK) {
                    processVideos(data);
                } else {
                    // means that no data at all.
                    this.videoIdList.clear();
                    setLabelViewText(new ArrayList<>(videoIdList));
                }
                break;
        }
    }

    private void processVideos(Intent data) {
        ArrayList<String> videoIdList = data.getStringArrayListExtra(YoutubeAddVideoView.KEY_VIDEOS_LINK);
        this.videoIdList.clear();
        setVideoIdList(videoIdList);
    }

    private void setLabelViewText(List<String> videoIdList) {
        if (CommonUtils.checkCollectionNotNull(videoIdList)) {
            labelAddVideoView.setContent(labelAddVideoView.getContext().getString(R.string.product_video_count, videoIdList.size()));
        } else {
            labelAddVideoView.setContent(labelAddVideoView.getContext().getString(R.string.product_etalase_picker_add_etalase_add_button_dialog));
        }
    }

    public List<String> getVideoIdList() {
        return videoIdList;
    }

    public ProductVariantSubmit getProductVariantSubmit() {
        return productVariantSubmit;
    }

    public void setVideoIdList(List<String> videoIdList) {
        this.videoIdList.addAll(videoIdList);
        setLabelViewText(videoIdList);
    }

    public void expandPreOrder(boolean expand) {
        preOrderExpandableOptionSwitch.setExpand(expand);
    }

    public int getPreOrderUnit() {
        if (preOrderExpandableOptionSwitch.isExpanded()) {
            return Integer.parseInt(preOrderSpinnerCounterInputView.getSpinnerValue());
        } else {
            return INACTIVE_PREORDER;
        }
    }

    public void setPreOrderUnit(int unit) {
        preOrderSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public void onSuccessGetProductVariant(List<ProductVariantByCatModel> productVariantByCatModelList) {
        this.productVariantByCatModelList = (ArrayList<ProductVariantByCatModel>) productVariantByCatModelList;
        if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
            variantLabelView.setVisibility(View.GONE);
        } else {
            variantLabelView.setVisibility(View.VISIBLE);
        }
        generateProductVariantSubmit();
    }

    public void onSuccessGetSelectedProductVariant(ProductVariantByPrdModel productVariantByPrdModel) {
        this.productVariantByPrdModel = productVariantByPrdModel;
        generateProductVariantSubmit();
    }

    public void setProductVariantSubmit(ProductVariantSubmit productVariantSubmit) {
        this.productVariantSubmit = productVariantSubmit;
        // TODO hendry use productVariantSubmit to populate the description
        if (productVariantByPrdModel == null ||
                productVariantByPrdModel.getVariantOptionList() == null ||
                productVariantByPrdModel.getVariantOptionList().size() == 0) {
            variantLabelView.resetContentText();
        } else {
            String selectedVariantString = "";
            List<VariantOption> variantOptionList = productVariantByPrdModel.getVariantOptionList();
            for (int i = 0, sizei = variantOptionList.size(); i < sizei; i++) {
                VariantOption variantOption = variantOptionList.get(i);
                String variantName = variantOption.getName();
                if (i != 0 && !TextUtils.isEmpty(selectedVariantString)) {
                    selectedVariantString += "\n";
                }
                List<Option> optionList = variantOption.getOptionList();
                if (optionList == null || optionList.size() == 0) {
                    continue;
                }
                selectedVariantString += optionList.size() + " " + variantName;
            }
            variantLabelView.setContent(selectedVariantString);
        }
    }

    public int getPreOrderValue() {
        if (preOrderExpandableOptionSwitch.isExpanded()) {
            return (int) preOrderSpinnerCounterInputView.getCounterValue();
        } else {
            return INACTIVE_PREORDER;
        }
    }

    public void setPreOrderValue(int value) {
        preOrderSpinnerCounterInputView.setCounterValue(value);
    }

    public boolean isShare() {
        return shareLabelSwitch.isChecked();
    }

    private boolean isPreOrderValid() {
        if (!preOrderExpandableOptionSwitch.isExpanded()) {
            return true;
        }
        String minPreOrderString = CurrencyFormatHelper.removeCurrencyPrefix(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_minimum_pre_order_day));
        String maxPreOrderString = CurrencyFormatHelper.removeCurrencyPrefix(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_maximum_pre_order_day));
        if (preOrderSpinnerCounterInputView.getSpinnerValue().equalsIgnoreCase(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_pre_order_value_week))) {
            minPreOrderString = CurrencyFormatHelper.removeCurrencyPrefix(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_minimum_pre_order_week));
            maxPreOrderString = CurrencyFormatHelper.removeCurrencyPrefix(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_maximum_pre_order_week));
        }
        double minValue = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(minPreOrderString));
        double maxValue = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(maxPreOrderString));
        if (minValue > getPreOrderValue() || getPreOrderValue() > maxValue) {
            preOrderSpinnerCounterInputView.setCounterError(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_error_product_pre_order_not_valid, minPreOrderString, maxPreOrderString));
            preOrderSpinnerCounterInputView.clearFocus();
            preOrderSpinnerCounterInputView.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public Pair<Boolean, String> isDataValid() {
        if (!isPreOrderValid()) {
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_OPTIONAL_PREORDER);
        }
        return new Pair<>(true, "");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList(YoutubeAddVideoView.KEY_VIDEOS_LINK, new ArrayList<>(videoIdList));
        savedInstanceState.putParcelable(SAVED_PRD_VARIANT_SUBMIT, productVariantSubmit);
        savedInstanceState.putParcelableArrayList(SAVED_VARIANT_CAT, productVariantByCatModelList);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        ArrayList<String> stringArrayList = savedInstanceState.getStringArrayList(YoutubeAddVideoView.KEY_VIDEOS_LINK);
        if (stringArrayList != null) {
            setVideoIdList(stringArrayList);
        }
        productVariantSubmit = savedInstanceState.getParcelable(SAVED_PRD_VARIANT_SUBMIT);
        productVariantByCatModelList = savedInstanceState.getParcelableArrayList(SAVED_VARIANT_CAT);
    }

    private ProductVariantSubmit generateProductVariantSubmit() {
        if (productVariantSubmit != null) {
            return productVariantSubmit;
        }
        if (productVariantByPrdModel != null && productVariantByCatModelList != null
                && productVariantByPrdModel.getVariantOptionList() != null
                && productVariantByPrdModel.getVariantOptionList().size() > 0
                && productVariantByCatModelList.size() > 0) {
            ProductVariantHelper productVariantHelper = new ProductVariantHelper(productVariantByCatModelList, productVariantByPrdModel);
            return productVariantHelper.generateProductVariantSubmit();
        }
        return null;
    }

    /**
     * @author normansyahputa on 4/18/17.
     *         <p>
     *         this represent contract for {@link ProductAdditionalInfoViewHolder}
     *         <p>
     *         for example calling {@link ProductAddFragment#startActivityForResult(Intent, int)}
     *         this will delefate to {@link ProductAddFragment} for doing that
     */
    public interface Listener {

        void startYoutubeVideoActivity(ArrayList<String> videoIds);

        void startProductVariantActivity(ArrayList<ProductVariantByCatModel> productVariantByCatModelArrayList);

        void onDescriptionTextChanged(String text);

        void showDialogMoveToGM(@StringRes int message);
    }
}
