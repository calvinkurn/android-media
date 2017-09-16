package com.tokopedia.gm.subscribe.view.widget.checkout;

/**
 * Created by sebastianuskh on 1/30/17.
 */

public interface AutoSubscribeViewHolderCallback {
    void selectAutoSubscribePackageFirstTime();

    void changeAutoSubscribePackage();

    boolean isAutoSubscribeUnselected();

    void clearAutoSubscribePackage();
}
