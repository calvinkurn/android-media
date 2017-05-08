package com.tokopedia.seller.product.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.expandable.ExpandableOptionSwitch;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelSwitch;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.listener.YoutubeAddVideoView;
import com.tokopedia.seller.product.view.widget.SpinnerCounterInputView;
import com.tokopedia.seller.util.NumberTextWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductAdditionalInfoViewHolder extends ProductViewHolder {

    private TextInputLayout descriptionTextInputLayout;
    private EditText descriptionEditText;
    private LabelView labelAddVideoView;
    private ExpandableOptionSwitch preOrderExpandableOptionSwitch;
    private SpinnerCounterInputView preOrderSpinnerCounterInputView;
    private LabelSwitch shareLabelSwitch;
    private Listener listener;
    /**
     * this prevent duplication at videoIdList;
     */
    private List<String> videoIdList;

    public ProductAdditionalInfoViewHolder(View view) {
        videoIdList = new ArrayList<>();
        descriptionTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_description);
        descriptionEditText = (EditText) view.findViewById(R.id.edit_text_description);
        labelAddVideoView = (LabelView) view.findViewById(R.id.label_add_video_view);
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
                    listener.startYoutubeVideoActivity(new ArrayList<>(videoIdList));
                }
            }
        });
        preOrderSpinnerCounterInputView.addTextChangedListener(new NumberTextWatcher(preOrderSpinnerCounterInputView.getCounterEditText(), preOrderSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)) {
            @Override
            public void onNumberChanged(float number) {
                if (isPreOrderValid()) {
                    preOrderSpinnerCounterInputView.setCounterError(null);
                }
            }
        });
        preOrderSpinnerCounterInputView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                preOrderSpinnerCounterInputView.setCounterValue(Float.parseFloat(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                preOrderSpinnerCounterInputView.post(new Runnable() {
                    @Override
                    public void run() {
                        isPreOrderValid();
                    }
                });
            }
        });
        preOrderExpandableOptionSwitch.setExpandableListener(new BaseExpandableOption.ExpandableListener() {
            @Override
            public void onExpandViewChange(boolean isExpand) {
                if (!isExpand) {
                    preOrderSpinnerCounterInputView.setCounterValue(Float.parseFloat(preOrderSpinnerCounterInputView.getContext().getString(R.string.product_default_counter_text)));
                }
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void updateViewGoldMerchant(boolean isShown) {
        if (isShown) {
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
            case YoutubeAddVideoView.REQUEST_CODE_GET_VIDEO:
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
            labelAddVideoView.setContent(labelAddVideoView.getContext().getString(R.string.etalase_picker_add_etalase_add_button_dialog));
        }
    }

    public List<String> getVideoIdList() {
        return videoIdList;
    }

    public void setVideoIdList(List<String> videoIdList) {
        this.videoIdList.addAll(videoIdList);
        setLabelViewText(videoIdList);
    }

    public void expandPreOrder(boolean expand) {
        preOrderExpandableOptionSwitch.setExpand(expand);
    }

    public int getPreOrderUnit() {
        return Integer.parseInt(preOrderSpinnerCounterInputView.getSpinnerValue());
    }

    public void setPreOrderUnit(int unit) {
        preOrderSpinnerCounterInputView.setSpinnerValue(String.valueOf(unit));
    }

    public int getPreOrderValue() {
        return (int) preOrderSpinnerCounterInputView.getCounterValue();
    }

    public void setPreOrderValue(float value) {
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
        float minValue = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(minPreOrderString));
        float maxValue = Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(maxPreOrderString));
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
            return false;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList(YoutubeAddVideoView.KEY_VIDEOS_LINK, new ArrayList<String>(videoIdList));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        ArrayList<String> stringArrayList = savedInstanceState.getStringArrayList(YoutubeAddVideoView.KEY_VIDEOS_LINK);
        if (stringArrayList != null) setVideoIdList(stringArrayList);

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

        void onDescriptionTextChanged(String text);

    }
}
