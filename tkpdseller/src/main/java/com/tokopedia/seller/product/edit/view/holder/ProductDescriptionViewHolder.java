package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddDescriptionPickerFragment;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoView;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPreOrderViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductDescriptionViewHolder extends ProductViewHolder {

    public static final int REQUEST_CODE_GET_VIDEO = 400;
    public static final int REQUEST_CODE_GET_DESCRIPTION = 401;

    public static final int STATUS_PRE_ORDER_INACTIVE = 0;
    public static final int STATUS_PRE_ORDER_ACTIVE = 1;

    private LabelView descriptionLabelView;
    private LabelView labelAddVideoView;
    private ExpandableOptionSwitch preOrderExpandableOptionSwitch;
    private SpinnerCounterInputView preOrderSpinnerCounterInputView;

    private Listener listener;

    private String description;
    private boolean goldMerchant;

    /**
     * this prevent duplication at videoIdList;
     */
    private List<String> videoIdList;
    private SpinnerTextView conditionSpinnerTextView;

    public ProductDescriptionViewHolder(View view, Listener listener) {
        videoIdList = new ArrayList<>();
        conditionSpinnerTextView = view.findViewById(R.id.spinner_text_view_condition);
        descriptionLabelView = view.findViewById(R.id.label_view_description);
        descriptionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDescriptionViewHolder.this.listener.goToProductDescriptionPicker(getDescription());
            }
        });
        labelAddVideoView = view.findViewById(R.id.label_add_video_view);
        labelAddVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.checkNotNull(ProductDescriptionViewHolder.this.listener)) {
                    if (!goldMerchant && GlobalConfig.isSellerApp()) {
                        UnifyTracking.eventClickVideoAddProduct();
                        ProductDescriptionViewHolder.this.listener.showDialogMoveToGM(R.string.add_product_label_alert_dialog_video);
                    } else {
                        ProductDescriptionViewHolder.this.listener.startYoutubeVideoActivity(new ArrayList<>(videoIdList));
                    }
                }
            }
        });

        preOrderExpandableOptionSwitch = view.findViewById(R.id.expandable_option_switch_pre_order);
        preOrderSpinnerCounterInputView = view.findViewById(R.id.spinner_counter_input_view_pre_order);
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

        setListener(listener);

    }

    @Override
    public void renderData(ProductViewModel model) {
        setCondition((int) model.getProductCondition());
        setDescription(model.getProductDescription());
        if (model.getProductVideo() != null) {
            setVideoIdList(convertToListString(model.getProductVideo()));
        }
        if (model.getProductPreorder() != null && model.getProductPreorder().getPreorderProcessTime() > 0) {
            expandPreOrder(true);
            setPreOrderUnit((int) model.getProductPreorder().getPreorderTimeUnit());
            setPreOrderValue((int) model.getProductPreorder().getPreorderProcessTime());
        } else {
            expandPreOrder(false);
        }
    }

    @Override
    public void updateModel(ProductViewModel model) {
        model.setProductCondition(getCondition());
        model.setProductDescription(getDescription());
        model.setProductVideo(getVideoList());
        model.setProductPreorder(getPreOrder());
    }

    private List<String> convertToListString(List<ProductVideoViewModel> productVideo) {
        List<String> productVideos = new ArrayList<>();
        for (ProductVideoViewModel productVideoViewModel : productVideo) {
            productVideos.add(productVideoViewModel.getUrl());
        }
        return productVideos;
    }

    public int getCondition() {
        return Integer.parseInt(conditionSpinnerTextView.getSpinnerValue());
    }

    public void setCondition(int unit) {
        conditionSpinnerTextView.setSpinnerValue(String.valueOf(unit));
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" :
                description.trim();
        if (TextUtils.isEmpty(this.description)) {
            descriptionLabelView.setContent(descriptionLabelView.getContext().getString(R.string.label_add));
        } else {
            descriptionLabelView.setContent(this.description);
        }
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
            case REQUEST_CODE_GET_DESCRIPTION:
                if (resultCode == Activity.RESULT_OK) {
                    String description = data.getStringExtra(ProductAddDescriptionPickerFragment.PRODUCT_DESCRIPTION);
                    setDescription(description);
                    listener.onDescriptionTextChanged(getDescription());
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
            listener.onHasVideoChange(true);
            labelAddVideoView.setContent(labelAddVideoView.getContext().getString(R.string.product_video_count, videoIdList.size()));
        } else {
            listener.onHasVideoChange(false);
            labelAddVideoView.setContent(labelAddVideoView.getContext().getString(R.string.label_add));
        }
    }

    public List<String> getVideoIdList() {
        return videoIdList;
    }

    public List<ProductVideoViewModel> getVideoList() {
        List<ProductVideoViewModel> productVideoViewModelList = new ArrayList<>();
        for (String videoId : getVideoIdList()) {
            ProductVideoViewModel productVideoViewModel = new ProductVideoViewModel(videoId);
            productVideoViewModelList.add(productVideoViewModel);
        }
        return productVideoViewModelList;
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
            return STATUS_PRE_ORDER_INACTIVE;
        }
    }

    public ProductPreOrderViewModel getPreOrder() {
        ProductPreOrderViewModel productPreorderViewModel = new ProductPreOrderViewModel();
        if (getPreOrderValue() > 0) {
            productPreorderViewModel.setPreorderStatus(STATUS_PRE_ORDER_ACTIVE);
            productPreorderViewModel.setPreorderProcessTime(getPreOrderValue());
            productPreorderViewModel.setPreorderTimeUnit(getPreOrderUnit());
        } else {
            productPreorderViewModel.setPreorderStatus(STATUS_PRE_ORDER_INACTIVE);
        }
        return productPreorderViewModel;
    }

    public void setPreOrderUnit(int unit) {
        preOrderSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public int getPreOrderValue() {
        if (preOrderExpandableOptionSwitch.isExpanded()) {
            return (int) preOrderSpinnerCounterInputView.getCounterValue();
        } else {
            return STATUS_PRE_ORDER_INACTIVE;
        }
    }

    public void setPreOrderValue(int value) {
        preOrderSpinnerCounterInputView.setCounterValue(value);
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
    public boolean isDataValid() {
        if (!isPreOrderValid()) {
            UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PREORDER);
            return false;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // no need; already on the model
    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        // no need; already on the model
    }

    /**
     * @author normansyahputa on 4/18/17.
     *         <p>
     *         this represent contract for {@link ProductDescriptionViewHolder}
     *         <p>
     *         for example calling {@link ProductAddFragment#startActivityForResult(Intent, int)}
     *         this will delefate to {@link ProductAddFragment} for doing that
     */
    public interface Listener {

        void goToProductDescriptionPicker(String description);

        void startYoutubeVideoActivity(ArrayList<String> videoIds);

        void onDescriptionTextChanged(String text);

        void showDialogMoveToGM(@StringRes int message);

        void onHasVideoChange(boolean hasVideo);

    }
}
