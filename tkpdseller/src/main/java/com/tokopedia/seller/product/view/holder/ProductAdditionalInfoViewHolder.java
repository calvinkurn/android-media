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
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.fragment.YoutubeAddVideoView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.tokopedia.seller.product.view.fragment.ProductAddView;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductAdditionalInfoViewHolder {

    private final String textVideo;
    private TextInputLayout descriptionTextInputLayout;
    private EditText descriptionEditText;
    private LabelView labelAddVideoView;

    private Listener listener;

    /**
     * this prevent duplication at videoIds;
     */
    private Set<String> videoIds;

    private String textAdd;
    private ProductAddView productAddView;

    public ProductAdditionalInfoViewHolder(View view, final ProductAddView productAddView) {
        this.productAddView = productAddView;
        descriptionTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_description);
        descriptionEditText = (EditText) view.findViewById(R.id.edit_text_description);
        labelAddVideoView = (LabelView) view.findViewById(R.id.label_add_video_view);

        textAdd = view.getContext().getString(R.string.etalase_picker_add_etalase_add_button_dialog);
        textVideo = view.getContext().getString(R.string.video);
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                productAddView.updateProductScoring();
            }
        });

        addUrlContainerViewHolder = new AddUrlContainerViewHolder(view);
    }

        videoIds = new HashSet<>();

        labelAddVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.checkNotNull(listener)) {
                    listener.startYoutubeVideoActivity(
                            new ArrayList<>(videoIds)
                    );
                }
            }
        });
    }

    public String getDescription() {
        return descriptionEditText.getText().toString();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
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
            String size = videoIds.size() + " " + textVideo;
            labelAddVideoView.setContent(size);
        } else {
            labelAddVideoView.setContent(textAdd);
        }
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
    }
}
