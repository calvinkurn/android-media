package com.tokopedia.seller.gmsubscribe.view.home;

/**
 * Created by sebastianuskh on 1/19/17.
 */

public interface GMHomeFragmentCallback {
    void goToGMProductFristTime();

    void changeActionBarTitle(String title);

    void setDrawer(boolean isShown);
}
