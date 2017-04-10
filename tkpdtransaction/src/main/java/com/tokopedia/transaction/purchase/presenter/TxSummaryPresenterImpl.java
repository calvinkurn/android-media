package com.tokopedia.transaction.purchase.presenter;

import android.app.Activity;
import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.drawer.interactor.NetworkInteractor;
import com.tokopedia.core.drawer.interactor.NetworkInteractorImpl;
import com.tokopedia.core.drawer.var.NotificationItem;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.var.NotificationVariable;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.transaction.purchase.listener.TxSummaryViewListener;
import com.tokopedia.transaction.purchase.model.TxSummaryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angga.Prasetiyo on 07/04/2016.
 */
public class TxSummaryPresenterImpl implements TxSummaryPresenter {
    private static final String TAG = TxSummaryPresenterImpl.class.getSimpleName();
    private final TxSummaryViewListener viewListener;

    public TxSummaryPresenterImpl(TxSummaryViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void getNotificationPurcase(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
        List<Integer> countList = cache.getArrayListInteger(TkpdCache.Key.PURCHASE_COUNT);
        List<TxSummaryItem> summaryItemList = new ArrayList<>();

        if(countList.size() < 1) viewListener.showLoadingError();
        else setSummaryData(context, countList, summaryItemList);
    }

    private void setSummaryData(Context context, List<Integer> countList, List<TxSummaryItem> summaryItemList) {
        summaryItemList.add(new TxSummaryItem(
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_VERIFICATION,
                context.getString(R.string.payment_status),
                context.getString(R.string.payment_status_desc),
                countList.get(1)
        ));
        summaryItemList.add(new TxSummaryItem(
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER,
                context.getString(R.string.order_status),
                context.getString(R.string.order_status_desc),
                countList.get(2)
        ));
        summaryItemList.add(new TxSummaryItem(
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVER_ORDER,
                context.getString(R.string.delivery_confirm),
                context.getString(R.string.title_receive_confirmation_dashboard_desc),
                countList.get(3)
        ));
        summaryItemList.add(new TxSummaryItem(
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER,
                context.getString(R.string.reorder),
                context.getString(R.string.title_transaction_list_desc),
                countList.get(4)
        ));
        viewListener.renderPurchaseSummary(summaryItemList);
    }

    @Override
    public void getNotificationFromNetwork(final Context context) {
        NetworkInteractor networkInteractor = new NetworkInteractorImpl();
        networkInteractor.getNotification(context, new NetworkInteractor.NotificationListener() {
            @Override
            public void onSuccess(NotificationItem data) {
                getNotificationPurcase(context);
            }

            @Override
            public void onError(String message) {
                viewListener.showLoadingError();
            }

        });
    }

    @Override
    public void onDestroyView() {

    }
}
