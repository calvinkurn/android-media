package com.tokopedia.topads.dashboard.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.topads.R;

/**
 * Created by normansyahputa on 2/25/17.
 */

@Deprecated
public class TopAdsNetworkErrorHelper extends GMNetworkErrorHelper {

    SnackbarRetry snackbarRetry;

    public TopAdsNetworkErrorHelper(NetworkErrorHelper.RetryClickedListener listener, View rootView) {
        super(listener, rootView);
    }

    public void showSnackbar(String titleText, String actionText
            , NetworkErrorHelper.RetryClickedListener onActionClickListener, Activity activity) {
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(activity, titleText, onActionClickListener);
        snackbarRetry.setColorActionRetry(ContextCompat.getColor(activity, R.color.tkpd_main_green));
        snackbarRetry.showRetrySnackbar();
    }

    public void dismissSnackbar() {
        if (snackbarRetry != null)
            snackbarRetry.hideRetrySnackbar();
    }
}
