package com.tokopedia.seller.gmstat.views.listeners;

/**
 * Created by normansyahputa on 1/18/17.
 */

public interface PeriodListener {
    void updateCheck(boolean checked, int index);
    boolean isAllNone(boolean checked, int index);
}
