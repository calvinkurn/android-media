package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.edit.view.listener.YoutubeAddVideoView;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductDescriptionViewHolder extends ProductViewHolder {

    public static final int REQUEST_CODE_GET_VIDEO = 1;

    private EditText descriptionEditText;
    private LabelView labelAddVideoView;

    private Listener listener;

    private boolean goldMerchant;

    /**
     * this prevent duplication at videoIdList;
     */
    private List<String> videoIdList;
    private SpinnerTextView conditionSpinnerTextView;

    public ProductDescriptionViewHolder(View view, Listener listener) {

        conditionSpinnerTextView = (SpinnerTextView) view.findViewById(R.id.spinner_text_view_condition);

        videoIdList = new ArrayList<>();
        descriptionEditText = (EditText) view.findViewById(R.id.edit_text_description);
        FrameLayout infoIconProductDescription = view.findViewById(R.id.info_icon_add_product_container);
        infoIconProductDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDescriptionViewHolder.this.listener.startInfoAddProduct();
            }
        });
        labelAddVideoView = (LabelView) view.findViewById(R.id.label_add_video_view);

        descriptionEditText.addTextChangedListener(new AfterTextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                ProductDescriptionViewHolder.this.listener.onDescriptionTextChanged(editable.toString().trim());
            }
        });
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


        setListener(listener);

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
        return descriptionEditText.getText().toString();
    }

    public void setDescription(String description) {
        descriptionEditText.setText(MethodChecker.fromHtmlPreserveLineBreak(description));
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
            listener.onHasVideoChange(true);
            labelAddVideoView.setContent(labelAddVideoView.getContext().getString(R.string.product_video_count, videoIdList.size()));
        } else {
            listener.onHasVideoChange(false);
            labelAddVideoView.setContent(labelAddVideoView.getContext().getString(R.string.product_etalase_picker_add_etalase_add_button_dialog));
        }
    }

    public List<String> getVideoIdList() {
        return videoIdList;
    }

    public List<ProductVideoViewModel> getVideoList() {
        List<ProductVideoViewModel> productVideoViewModelList = new ArrayList<>();
        for(String videoId : getVideoIdList()) {
            ProductVideoViewModel productVideoViewModel = new ProductVideoViewModel(videoId);
            productVideoViewModelList.add(productVideoViewModel);
        }
        return productVideoViewModelList;
    }

    public void setVideoIdList(List<String> videoIdList) {
        this.videoIdList.addAll(videoIdList);
        setLabelViewText(videoIdList);
    }

    @Override
    public Pair<Boolean, String> isDataValid() {
        return new Pair<>(true, "");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList(YoutubeAddVideoView.KEY_VIDEOS_LINK, new ArrayList<>(videoIdList));

    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {
        ArrayList<String> stringArrayList = savedInstanceState.getStringArrayList(YoutubeAddVideoView.KEY_VIDEOS_LINK);
        if (stringArrayList != null) {
            setVideoIdList(stringArrayList);
        }
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

        void startInfoAddProduct();

        void startYoutubeVideoActivity(ArrayList<String> videoIds);

        void onDescriptionTextChanged(String text);

        void showDialogMoveToGM(@StringRes int message);

        void onHasVideoChange(boolean hasVideo);

    }
}
