package com.tokopedia.transaction.purchase.utils;

import android.content.Context;

import com.tokopedia.core2.R;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.transaction.purchase.model.TxFilterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angga.Prasetiyo on 25/04/2016.
 */
public class FilterUtils {

    public static List<TxFilterItem> filterTxAllItems(Context context) {
        List<TxFilterItem> list = new ArrayList<>();
        list.add(new TxFilterItem(
                TransactionPurchaseRouter.ALL_STATUS_FILTER_ID,
                context.getString(R.string.item_all_stats)
        ));
        list.add(new TxFilterItem(
                TransactionPurchaseRouter.PAYMENT_CONFIRMATION_FILTER_ID,
                context.getString(R.string.item_payment_confirmation)
        ));
        list.add(new TxFilterItem(
                TransactionPurchaseRouter.PAYMENT_VERIFICATION_FILTER_ID,
                context.getString(R.string.item_payment_verification)
        ));
        list.add(new TxFilterItem(
                TransactionPurchaseRouter.TRANSACTION_CONFIRMED_FILTER_ID,
                context.getString(com.tokopedia.transaction.R.string.tkpdtransaction_label_tx_confirmed)
        ));
        list.add(new TxFilterItem(
                TransactionPurchaseRouter.TRANSACTION_PROCESSED_FILTER_ID,
                context.getString(com.tokopedia.transaction.R.string.tkpdtransaction_label_tx_processed)
        ));
        list.add(new TxFilterItem(
                TransactionPurchaseRouter.ONGOING_DELIVERY_FILTER_ID,
                context.getString(com.tokopedia.transaction.R.string.tkpdtransaction_label_tx_shipped)
        ));
        list.add(new TxFilterItem(
                TransactionPurchaseRouter.TRANSACTION_DELIVERED_FILTER_ID,
                context.getString(com.tokopedia.transaction.R.string.tkpdtransaction_label_tx_delivered)
        ));
        list.add(new TxFilterItem(
                TransactionPurchaseRouter.TRANSACTION_DONE_FILTER_ID,
                context.getString(R.string.item_finished)
        ));
        list.add(new TxFilterItem(
                TransactionPurchaseRouter.TRANSACTION_CANCELED_FILTER_ID,
                context.getString(R.string.item_cancel)
        ));
        return list;
    }

    public static boolean isChangedFilter(String txFilterBefore, String txFilterID) {
        return !txFilterBefore.equalsIgnoreCase(txFilterID);
    }
}
