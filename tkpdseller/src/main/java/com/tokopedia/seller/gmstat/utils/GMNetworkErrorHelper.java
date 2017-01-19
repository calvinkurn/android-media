package com.tokopedia.seller.gmstat.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.network.BaseNetworkErrorHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
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

    public void onResume(View rootView){
        snackBar = new SnackBar().view(rootView);
    }

    public void onPause(){
        snackBar= null;
    }

    public void showSnackbar(String titleText, String actionText, OnActionClickListener onActionClickListener){
        snackBar
                .duration(SnackBar.SnackBarDuration.INDEFINITE)
                .text(titleText, actionText)
                .textColors(Color.WHITE,Color.GREEN)
                .backgroundColor(Color.BLACK)
                .duration(SnackBar.SnackBarDuration.INDEFINITE)
                .setOnClickListener(true, onActionClickListener)
                .show();
    }
}
