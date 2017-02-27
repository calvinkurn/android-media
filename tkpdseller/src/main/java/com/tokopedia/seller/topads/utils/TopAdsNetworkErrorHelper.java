package com.tokopedia.seller.topads.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.gmstat.utils.GMNetworkErrorHelper;

/**
 * Created by normansyahputa on 2/25/17.
 */

public class TopAdsNetworkErrorHelper extends GMNetworkErrorHelper {

    public TopAdsNetworkErrorHelper(NetworkErrorHelper.RetryClickedListener listener, View rootView) {
        super(listener, rootView);
    }

    public void showSnackbar(String titleText, String actionText
            , ActionClickListener onActionClickListener, Activity activity) {
        SnackbarManager.show(
                Snackbar.with(activity.getApplicationContext()) // context
                        .type(SnackbarType.MULTI_LINE)
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                        .textColor(Color.WHITE)
                        .actionColor(Color.GREEN)
                        .color(Color.BLACK)
                        .text(titleText) // text to display
                        .actionLabel(actionText) // action button label
                        .actionListener(onActionClickListener) // action button's ActionClickListener
                , activity); // activity where it is displayed
    }

    public void dismissSnackbar() {
        if (SnackbarManager.getCurrentSnackbar() != null)
            SnackbarManager.dismiss();
    }
}
