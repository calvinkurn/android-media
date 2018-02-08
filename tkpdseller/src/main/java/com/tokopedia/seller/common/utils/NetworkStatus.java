package com.tokopedia.seller.common.utils;

import com.tokopedia.seller.common.constant.NetworkConstant;

/**
 * Created by nathan on 9/15/17.
 */

@Deprecated
public enum NetworkStatus {

    SEARCHVIEW(NetworkConstant.SEARCHVIEW_NETWORK_CALL),
    LOADMORE(NetworkConstant.LOAD_MORE_NETWORK_CALL),
    PULLTOREFRESH(NetworkConstant.PULL_TO_REFRESH_NETWORK_CALL),
    NONETWORKCALL(NetworkConstant.NO_NETWORK_CALL),
    RETRYNETWORKCALL(NetworkConstant.RETRY_NETWORK_CALL),
    ONACTIVITYFORRESULT(NetworkConstant.ON_ACTIVITY_FOR_RESULT);

    private int type;

    NetworkStatus(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
