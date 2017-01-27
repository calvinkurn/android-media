package com.tokopedia.seller.gmstat.views.listeners;

/**
 * Created by normansyahputa on 1/18/17.
 */

public interface PeriodListener {
    void updateCheck(boolean checked, int index);

    /**
     * determine if options get selected at least one, true if none of options get selected, otherwise
     * @param checked  value of next selection
     * @param index index of selection
     * @return true if none of options not get selected, false if one of options get selected
     */
    boolean isAllNone(boolean checked, int index);
}
