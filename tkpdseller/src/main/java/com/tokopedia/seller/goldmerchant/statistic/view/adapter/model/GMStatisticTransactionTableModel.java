package com.tokopedia.seller.goldmerchant.statistic.view.adapter.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableModel implements ItemType {
    public static final int TYPE = 199349;

    public String rightText;
    public String leftText;

    @Override
    public int getType() {
        return TYPE;
    }
}
