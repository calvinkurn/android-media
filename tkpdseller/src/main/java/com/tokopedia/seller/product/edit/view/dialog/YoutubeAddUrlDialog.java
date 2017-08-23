package com.tokopedia.seller.product.edit.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.dialog.BaseTextPickerDialogFragment;
import com.tokopedia.seller.product.edit.utils.YoutubeVideoLinkUtils;

/**
 * @author normansyahputa on 4/13/17.
 */

public class YoutubeAddUrlDialog extends BaseTextPickerDialogFragment {

    public static final String TAG = "YoutubeAddUrlDialog";

    private YoutubeVideoLinkUtils youtubeLinkUtils;
    private boolean isErrorReturn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // init youtube link checker
        youtubeLinkUtils = new YoutubeVideoLinkUtils();
        youtubeLinkUtils.fillExceptionString(getActivity());

        textInput.setMaxLines(1);
        textInput.setSingleLine(true);
        stringPickerTitle.setText(R.string.product_youtube_add_url_title);
        textInputLayout.setHint(getString(R.string.product_url_video));
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateUrl(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    protected void validateUrl(CharSequence s) {
        if (CommonUtils.checkNotNull(youtubeLinkUtils)) {
            try {
                youtubeLinkUtils.setYoutubeUrl(s.toString());
            } catch (IllegalArgumentException iae) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(iae.getMessage());

                isErrorReturn = true;
                return;
            }

            try {
                String videoID = youtubeLinkUtils.saveVideoID();
                CommonUtils.dumper(TAG + " : " + videoID);
            } catch (IllegalArgumentException iae) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(iae.getMessage());

                isErrorReturn = true;
                return;
            }

            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
            isErrorReturn = false;
        }
    }

    @Override
    protected void onTextSubmitted(String text) {
        validateFirstTime();

        if (isErrorReturn) {
            return;
        }

        super.onTextSubmitted(text);
    }

    /**
     * there is cases when validation not working
     * when user tap add button for the first time.
     */
    private void validateFirstTime() {
        validateUrl(textInput.getText().toString());
    }
}
