package com.tokopedia.inbox.rescenter.historyaction.view.listener;

import android.app.Fragment;

/**
 * Created by hangnadi on 3/23/17.
 */

@SuppressWarnings("ALL")
public interface HistoryActionViewListener {
    void setResolutionID(String resolutionID);

    void inflateFragment();

    String getResolutionID();

    void setHistoryShippingFragment(Fragment fragment);

    Fragment getFragment();
}
