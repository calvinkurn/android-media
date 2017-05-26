package com.tokopedia.ride.history.view;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;

/**
 * Created by alvarisi on 4/20/17.
 */

public class RideHistoryNeedHelpPresenter extends BaseDaggerPresenter<RideHistoryNeedHelpContract.View> implements RideHistoryNeedHelpContract.Presenter {

    public RideHistoryNeedHelpPresenter() {
    }

    @Override
    public void initialize() {
        getView().renderUi();
    }

    @Override
    public void copyToClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }

        getView().showToast(R.string.message_copied);
    }
}