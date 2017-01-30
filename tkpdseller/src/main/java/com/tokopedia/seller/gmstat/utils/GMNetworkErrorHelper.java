package com.tokopedia.seller.gmstat.utils;

import android.graphics.Color;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.v4.BaseNetworkErrorHandlerImpl;
import com.tokopedia.seller.gmstat.views.OnActionClickListener;
import com.tokopedia.seller.gmstat.views.SnackBar;

/**
 * Created by normansyahputa on 1/19/17.
 */

public class GMNetworkErrorHelper extends BaseNetworkErrorHandlerImpl {

    SnackBar snackBar;

    public GMNetworkErrorHelper(NetworkErrorHelper.RetryClickedListener listener, View rootView) {
        super(listener);
        snackBar = new SnackBar().view(rootView);
    }

    public void onResume(View rootView) {
        snackBar = new SnackBar().view(rootView);
    }

    public void onPause() {
        snackBar = null;
    }

    public void showSnackbar(String titleText, String actionText, OnActionClickListener onActionClickListener) {
        snackBar
                .duration(SnackBar.SnackBarDuration.INDEFINITE)
                .text(titleText, actionText)
                .textColors(Color.WHITE, Color.GREEN)
                .backgroundColor(Color.BLACK)
                .duration(SnackBar.SnackBarDuration.INDEFINITE)
                .setOnClickListener(true, onActionClickListener)
                .show();
    }
}
