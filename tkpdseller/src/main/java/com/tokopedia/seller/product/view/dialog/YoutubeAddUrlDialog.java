package com.tokopedia.seller.product.view.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.utils.YoutubeVideoLinkUtils;

/**
 * @author normansyahputa on 4/13/17.
 */

public class YoutubeAddUrlDialog extends AddEtalaseDialog {

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

        etalaseTitle.setText(R.string.youtube_add_url_title);
        etalaseNameInputLayout.setHint(getString(R.string.url_video));
        etalaseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CommonUtils.checkNotNull(youtubeLinkUtils)) {
                    try {
                        youtubeLinkUtils.setYoutubeUrl(s.toString());
                    } catch (IllegalArgumentException iae) {
                        etalaseNameInputLayout.setErrorEnabled(true);
                        etalaseNameInputLayout.setError(iae.getMessage());

                        isErrorReturn = true;
                        return;
                    }

                    try {
                        String videoID = youtubeLinkUtils.saveVideoID();
                        CommonUtils.dumper(TAG + " : " + videoID);
                    } catch (IllegalArgumentException iae) {
                        etalaseNameInputLayout.setErrorEnabled(true);
                        etalaseNameInputLayout.setError(iae.getMessage());

                        isErrorReturn = true;
                        return;
                    }

                    etalaseNameInputLayout.setErrorEnabled(false);
                    etalaseNameInputLayout.setError(null);
                    isErrorReturn = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @NonNull
    @Override
    protected AddEtalaseButtonOnClick getSaveOnClickListener() {
        return new AddYoutubeButtonOnClick();
    }

    protected class AddYoutubeButtonOnClick extends AddEtalaseButtonOnClick {
        @Override
        public void onClick(View v) {
            if (isErrorReturn)
                return;

            String youtubeLink = etalaseName.getText().toString();
            if (listener instanceof YoutubeAddUrlDialogListener) {
                ((YoutubeAddUrlDialogListener) listener).addYoutubeUrl(youtubeLink);
            }
            dismiss();
        }
    }
}
