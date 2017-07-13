package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;

import java.util.List;

/**
 * Created by normansyahputa on 7/13/17.
 */

public interface GMStatisticTransactionTableView extends CustomerView {

    String START_DATE = "START_DATE";
    String END_DATE = "END_DATE";

    void onSearchLoaded(@NonNull List list, int totalItem);
}
