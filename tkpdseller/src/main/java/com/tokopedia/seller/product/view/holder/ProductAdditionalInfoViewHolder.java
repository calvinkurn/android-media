package com.tokopedia.seller.product.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.lib.widget.LabelSwitch;
import com.tokopedia.seller.product.view.widget.SpinnerCounterInputView;

import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.listener.YoutubeAddVideoView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductAdditionalInfoViewHolder extends ProductViewHolder {

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
    private TextInputLayout descriptionTextInputLayout;

    private EditText descriptionEditText;
    private LabelView labelAddVideoView;
    private SpinnerCounterInputView preOrderSpinnerCounterInputView;
    private LabelSwitch shareLabelSwitch;
    private Listener listener;

    /**
     * this prevent duplication at videoIds;
     */
    private Set<String> videoIds;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ProductAdditionalInfoViewHolder(View view) {
        descriptionTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_description);
        descriptionEditText = (EditText) view.findViewById(R.id.edit_text_description);
        labelAddVideoView = (LabelView) view.findViewById(R.id.label_add_video_view);
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

        videoIds = new HashSet<>();

        labelAddVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.checkNotNull(listener)) {
                    listener.startYoutubeVideoActivity(new ArrayList<>(videoIds));
                }
            }
        });
    }

    public void updateViewGoldMerchant(boolean isShown){
        if (isShown) {
            labelAddVideoView.setVisibility(View.VISIBLE);
        } else {
            this.videoIds.clear();
            labelAddVideoView.setVisibility(View.GONE);
        }
    }

    public String getDescription() {
        return descriptionEditText.getText().toString();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case YoutubeAddVideoView.REQUEST_CODE_GET_VIDEO:
                if (resultCode == Activity.RESULT_OK) {
                    processVideos(data);
                } else {
                    // means that no data at all.
                    this.videoIds.clear();
                    setLabelViewText(new ArrayList<String>(videoIds));
                }
                break;
        }
    }

    private void processVideos(Intent data) {
        ArrayList<String> videoIds = data.getStringArrayListExtra(YoutubeAddVideoView.KEY_VIDEOS_LINK);
        this.videoIds.clear();
        this.videoIds.addAll(videoIds);

        setLabelViewText(videoIds);
    }

    private void setLabelViewText(ArrayList<String> videoIds) {
        if (CommonUtils.checkCollectionNotNull(videoIds)) {
            labelAddVideoView.setContent(labelAddVideoView.getContext().getString(R.string.product_video_count, videoIds.size()));
        } else {
            labelAddVideoView.setContent(labelAddVideoView.getContext().getString(R.string.etalase_picker_add_etalase_add_button_dialog));
        }
    }

    public int getPreOrderUnit() {
        return Integer.parseInt(preOrderSpinnerCounterInputView.getSpinnerValue());
    }

    public int getPreOrderDay() {
        return (int) preOrderSpinnerCounterInputView.getCounterValue();
    }

    public boolean isShare() {
        return shareLabelSwitch.isChecked();
    }

    public void setDescription(String description) {
        descriptionEditText.setText(description);
    }

    @Override
    public boolean isDataValid() {
        return true;
    }
}
