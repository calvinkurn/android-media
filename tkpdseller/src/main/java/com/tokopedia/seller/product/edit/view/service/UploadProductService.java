package com.tokopedia.seller.product.edit.view.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseService;
import com.tokopedia.core.gcm.utils.NotificationChannelId;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.edit.di.component.DaggerAddProductServiceComponent;
import com.tokopedia.seller.product.edit.di.module.AddProductserviceModule;
import com.tokopedia.seller.product.edit.domain.listener.NotificationCountListener;
import com.tokopedia.seller.product.edit.view.activity.ProductDraftAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductDraftEditActivity;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.AddProductServiceListener;
import com.tokopedia.seller.product.edit.view.presenter.AddProductServicePresenter;
import com.tokopedia.seller.product.manage.view.activity.ProductManageActivity;

import java.util.HashMap;

import javax.inject.Inject;

public class UploadProductService extends BaseService implements AddProductServiceListener {
    public static final String TAG = "upload_product";

    public static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";
    public static final String IS_ADD = "IS_ADD";

    public static final String ACTION_DRAFT_CHANGED = "com.tokopedia.draft.changed";

    private static final int MAX_NOTIFICATION_PROGRESS = 6;

    private NotificationManager notificationManager;

    HashMap<Long, NotificationCompat.Builder> notificationBuilderMap = new HashMap<>();

    public static Intent getIntent(Context context, long draftProductId, boolean isAdd) {
        Intent intent = new Intent(context, UploadProductService.class);
        intent.putExtra(DRAFT_PRODUCT_ID, draftProductId);
        intent.putExtra(IS_ADD, isAdd);
        return intent;
    }

    @Inject
    AddProductServicePresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        DaggerAddProductServiceComponent
                .builder()
                .productComponent(((SellerModuleRouter) getApplication()).getProductComponent())
                .addProductserviceModule(new AddProductserviceModule())
                .build().inject(this);
        presenter.attachView(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final long draftProductId = intent.getLongExtra(DRAFT_PRODUCT_ID, Long.MIN_VALUE);
        boolean isAdd = intent.getBooleanExtra(IS_ADD, true);
        presenter.uploadProduct(draftProductId, isAdd, new NotificationCountListener((int) draftProductId, MAX_NOTIFICATION_PROGRESS) {
            @Override
            public void addProgress() {
                super.addProgress();
                notificationUpdate(draftProductId, getCurrentCount());
            }

            @Override
            public void setProductName(String productName) {
                super.addProgress();
                createNotification(draftProductId, productName);
            }
        });
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSuccessAddProduct() {
        /*stopForeground(false);
        stopSelf();*/
    }

    @Override
    public void onFailedAddProduct() {
        /*stopForeground(false);
        stopSelf();*/
    }

    @Override
    public void notificationFailed(Throwable error, long draftProductId, @ProductStatus int productStatus) {
        String errorMessage = ErrorHandler.getErrorMessage(getApplicationContext(), error);
        Notification notification = buildFailedNotification(errorMessage, draftProductId, productStatus);
        notificationManager.notify(TAG, getNotificationIdByDraft(draftProductId), notification);
        removeNotificationFromList(draftProductId);
    }

    private void removeNotificationFromList(long draftProductId) {
        notificationBuilderMap.remove(draftProductId);
        if (notificationBuilderMap.size() <= 0) {
            stopSelf();
        }
    }

    private int getNotificationIdByDraft(long draftProductId) {
        return (int) draftProductId;
    }

    public void createNotification(long draftProductId, String productName) {
        NotificationCompat.Builder builder = buildBaseNotification(productName);
        Notification notification = buildStartNotification(builder);

        notificationManager.notify(TAG, getNotificationIdByDraft(draftProductId), notification);
        notificationBuilderMap.put(draftProductId, builder);
    }

    public void notificationUpdate(long draftProductId, int currentCount) {
        Notification notification = buildProgressNotification(draftProductId, currentCount);
        notificationManager.notify(TAG, getNotificationIdByDraft(draftProductId), notification);
    }

    @Override
    public void notificationComplete(long draftProductId) {
        Notification notification = buildCompleteNotification(draftProductId);
        if (notification == null) {
            return;
        }
        notificationManager.notify(TAG, getNotificationIdByDraft(draftProductId), notification);
        removeNotificationFromList(draftProductId);
    }

    @Override
    public void sendFailedBroadcast(Throwable error) {
        Crashlytics.logException(error);
        String errorMessage = ErrorHandler.getErrorMessage(getApplicationContext(), error);
        UnifyTracking.eventAddProductErrorServer(errorMessage);
        Intent result = new Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        Bundle bundle = new Bundle();
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR);
        bundle.putString(TkpdState.ProductService.MESSAGE_ERROR_FLAG, errorMessage);
        result.putExtras(bundle);
        sendBroadcast(result);

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.sendBroadcast(new Intent(ACTION_DRAFT_CHANGED));
    }

    //TODO no need to send bundle, only need the String action to refresh page
    @Override
    public void sendSuccessBroadcast(ProductViewModel productViewModel) {
        Intent result = new Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        Bundle bundle = new Bundle();
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_DONE);
        bundle.putString(TkpdState.ProductService.PRODUCT_NAME, productViewModel.getProductName());
//        bundle.putString(TkpdState.ProductService.IMAGE_URI, productViewModel.getProductPrimaryPic());
        bundle.putString(TkpdState.ProductService.PRODUCT_URI, productViewModel.getProductUrl());
//        bundle.putString(TkpdState.ProductService.PRODUCT_DESCRIPTION, productViewModel.getProductDesc());
        bundle.putString(TkpdState.ProductService.PRODUCT_ID, productViewModel.getProductId() + "");
        result.putExtras(bundle);
        sendBroadcast(result);
    }

    private NotificationCompat.Builder buildBaseNotification(String productName) {
        String title = getString(R.string.product_title_notification_upload_product) + " " + productName;
        Intent pendingIntent = new Intent(this, ProductManageActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, 0);
        return new NotificationCompat.Builder(this, NotificationChannelId.GENERAL)
                .setContentTitle(title)
                .setSmallIcon(getSmallDrawableIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableIcon()))
                .setContentIntent(pIntent)
                .setGroup(getString(R.string.product_group_notification));
    }

    private int getDrawableIcon() {
        if (GlobalConfig.isSellerApp()) {
            return R.drawable.ic_stat_notify2;
        } else {
            return R.drawable.ic_stat_notify;
        }
    }

    private int getSmallDrawableIcon() {
        return R.drawable.ic_stat_notify_white;
    }

    private Notification buildFailedNotification(String errorMessage, long draftProductId, @ProductStatus int productStatus) {
        Intent pendingIntent = ProductDraftAddActivity.createInstance(this, draftProductId);
        if (productStatus == ProductStatus.EDIT) {
            pendingIntent = ProductDraftEditActivity.createInstance(this, draftProductId);
        }
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (notificationBuilderMap.get(draftProductId) == null) {
            createNotification(draftProductId, "");
        }
        return notificationBuilderMap.get(draftProductId)
                .setContentText(errorMessage)
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(errorMessage)
                )
                .setContentIntent(pIntent)
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .build();
    }

    private Notification buildStartNotification(NotificationCompat.Builder builder) {
        return builder
                .setContentText(getString(R.string.product_notification_start_upload_product))
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(getString(R.string.product_notification_start_upload_product))
                )
                .build();
    }

    private Notification buildProgressNotification(long draftProductId, int stepNotification) {
        return notificationBuilderMap.get(draftProductId)
                .setContentText(getString(R.string.product_notification_progress_upload_product))
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(getString(R.string.product_notification_progress_upload_product)))
                .setProgress(MAX_NOTIFICATION_PROGRESS, stepNotification, false)
                .build();
    }

    private Notification buildCompleteNotification(long draftProductId) {
        return notificationBuilderMap.get(draftProductId)
                .setContentText(getString(R.string.product_notification_complete_upload_product))
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(getString(R.string.product_notification_complete_upload_product))
                )
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .build();
    }


}
