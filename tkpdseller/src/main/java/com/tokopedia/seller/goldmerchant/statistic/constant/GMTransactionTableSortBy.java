package com.tokopedia.seller.goldmerchant.statistic.constant;

/**
 * Created by normansyahputa on 7/14/17.
 */

public @interface GMTransactionTableSortBy {
    int ORDER_SUM = 5;// total transaksi
    int TRANS_SUM = 3;// transaksi sukses
    int DELIVERED_AMT = 13; // pendapatan bersih

    String param = "sort_by";
}
