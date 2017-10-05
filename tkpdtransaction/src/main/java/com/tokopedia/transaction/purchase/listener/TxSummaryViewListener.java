package com.tokopedia.transaction.purchase.listener;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.transaction.purchase.model.TxSummaryItem;

import java.util.List;

/**
 * @author Angga.Prasetiyo on 07/04/2016.
 */
public interface TxSummaryViewListener {
    void renderPurchaseSummary(List<TxSummaryItem> summaryItemList);

    void showLoadingError();

    Activity getActivity();
}
