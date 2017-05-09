package com.tokopedia.inbox.rescenter.historyawb.view.listener;

import android.app.Fragment;

/**
 * Created by hangnadi on 3/23/17.
 */

@SuppressWarnings("ALL")
public interface HistoryShippingViewListener {
    void setResolutionID(String resolutionID);

    void inflateFragment();

    String getResolutionID();

    void setHistoryShippingFragment(Fragment fragment);

    Fragment getFragment();

    void setAllowInputNewShippingAwb(boolean allowInputNewShippingAwb);

    boolean isAllowInputNewShippingAwb();
}
