package com.tokopedia.gm.featured.view.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.gm.R;

/**
 * Created by normansyahputa on 9/19/17.
 */

public class GMSnackbarRetry extends SnackbarRetry {
    public GMSnackbarRetry(Snackbar snackbar, final NetworkErrorHelper.RetryClickedListener listener) {
        super(snackbar, listener);
        snackBar.setAction(R.string.undo_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRetryClicked = true;
                listener.onRetryClicked();
            }
        });
    }
}
