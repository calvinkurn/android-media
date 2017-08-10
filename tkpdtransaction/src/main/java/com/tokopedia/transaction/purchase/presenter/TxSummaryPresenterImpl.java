package com.tokopedia.transaction.purchase.presenter;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.NotificationMapper;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.transaction.purchase.listener.TxSummaryViewListener;
import com.tokopedia.transaction.purchase.model.TxSummaryItem;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author Angga.Prasetiyo on 07/04/2016.
 */
public class TxSummaryPresenterImpl implements TxSummaryPresenter {
    private final TxSummaryViewListener viewListener;
    private NotificationUseCase notificationUseCase;
    private LocalCacheHandler cacheHandler;

    public TxSummaryPresenterImpl(TxSummaryViewListener viewListener) {
        this.viewListener = viewListener;

        cacheHandler =
                new LocalCacheHandler(viewListener.getActivity(), DrawerHelper.DRAWER_CACHE);

        NotificationService notificationService = new NotificationService();
        NotificationSourceFactory notificationSourceFactory =
                new NotificationSourceFactory(
                        viewListener.getActivity(),
                        notificationService,
                        new NotificationMapper(),
                        cacheHandler
                );
        NotificationRepository notificationRepository =
                new NotificationRepositoryImpl(notificationSourceFactory);
        this.notificationUseCase =
                new NotificationUseCase(
                        new JobExecutor(),
                        new UIThread(),
                        notificationRepository);
    }

    @Override
    public void getNotificationPurcase(Context context) {
        List<Integer> countList = new ArrayList<>();
        countList.add(cacheHandler.getInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONF, 0));
        countList.add(cacheHandler.getInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONFIRM, 0));
        countList.add(cacheHandler.getInt(DrawerNotification.CACHE_PURCHASE_ORDER_STATUS, 0));
        countList.add(cacheHandler.getInt(DrawerNotification.CACHE_PURCHASE_DELIVERY_CONFIRM, 0));
        countList.add(cacheHandler.getInt(DrawerNotification.CACHE_PURCHASE_REORDER, 0));

        List<TxSummaryItem> summaryItemList = new ArrayList<>();

        if (countList.size() < 1) viewListener.showLoadingError();
        else setSummaryData(context, countList, summaryItemList);

    }

    private Subscriber<NotificationModel> onGetNotification(final Context context) {
        return new Subscriber<NotificationModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewListener.showLoadingError();
            }

            @Override
            public void onNext(NotificationModel notificationModel) {
                NotificationData data = notificationModel.getNotificationData();
                List<Integer> countList = new ArrayList<>();
                countList.add(data.getPurchase().getPurchasePaymentConf());
                countList.add(data.getPurchase().getPurchasePaymentConfirm());
                countList.add(data.getPurchase().getPurchaseOrderStatus());
                countList.add(data.getPurchase().getPurchaseDeliveryConfirm());
                countList.add(data.getPurchase().getPurchaseReorder());

                List<TxSummaryItem> summaryItemList = new ArrayList<>();

                if (countList.size() < 1) viewListener.showLoadingError();
                else setSummaryData(context, countList, summaryItemList);
            }
        };
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
        notificationUseCase.execute(
                NotificationUseCase.getRequestParam(
                        GlobalConfig.isSellerApp()), onGetNotification(context));
    }

    @Override
    public void onDestroyView() {
        notificationUseCase.unsubscribe();
    }
}
