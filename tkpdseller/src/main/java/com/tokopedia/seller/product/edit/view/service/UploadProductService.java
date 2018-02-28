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
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.view.activity.ProductDraftAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductDraftEditActivity;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.AddProductServiceListener;
import com.tokopedia.seller.product.edit.view.presenter.AddProductServicePresenter;
import com.tokopedia.seller.product.manage.view.activity.ProductManageActivity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class UploadProductService extends BaseService implements AddProductServiceListener {
    public static final String TAG = "upload_product";
    ;
    public static final String PRODUCT_DRAFT_ID = "PRODUCT_DRAFT_ID";
    public static final String IS_ADD = "IS_ADD";

    public static final String ACTION_DRAFT_CHANGED = "com.tokopedia.draft.changed";

    private NotificationManager notificationManager;

    ArrayList<Long> productDraftIdList = new ArrayList<>();
    HashMap<Long, NotificationCompat.Builder> notificationBuilderMap = new HashMap<>();
    HashMap<Long, Integer> progressMap = new HashMap<>();

    public static Intent getIntent(Context context, long productId, boolean isAdd) {
        Intent intent = new Intent(context, UploadProductService.class);
        intent.putExtra(PRODUCT_DRAFT_ID, productId);
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
        long productDraftId = intent.getLongExtra(PRODUCT_DRAFT_ID, -1);
        boolean isAdd = intent.getBooleanExtra(IS_ADD, true);
        presenter.uploadProduct(productDraftId, isAdd);
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
    public void notificationFailed(Throwable error, long productDraftId, @ProductStatus int productStatus) {
        String errorMessage = ErrorHandler.getErrorMessage(getApplicationContext(), error);
        Notification notification = buildFailedNotification(errorMessage, productDraftId, productStatus);
        notificationManager.notify(TAG, getNotifIdByDraft(productDraftId), notification);
        removeNotifFromList(productDraftId);
    }

    private void removeNotifFromList(long productDraftId) {
        productDraftIdList.remove(productDraftId);
        notificationBuilderMap.remove(productDraftId);
        progressMap.remove(productDraftId);
        if (productDraftIdList.size() == 0) {
            stopSelf();
        }
    }

    private int getNotifIdByDraft(long productDraftId) {
        return (int) productDraftId;
    }

    @Override
    public void createNotification(long productDraftId, String productName) {
        NotificationCompat.Builder builder = buildBaseNotification(productName);
        Notification notification = buildStartNotification(builder);

        notificationManager.notify(TAG, getNotifIdByDraft(productDraftId), notification);

        productDraftIdList.add(productDraftId);
        notificationBuilderMap.put(productDraftId, builder);
        progressMap.put(productDraftId, 0);
    }

    @Override
    public void notificationUpdate(long productDraftId) {
        //get progress from list and update the progress
        int stepNotification = progressMap.get(productDraftId);
        progressMap.put(productDraftId, stepNotification++);
        Notification notification = buildProgressNotification(productDraftId, stepNotification);
        notificationManager.notify(TAG, getNotifIdByDraft(productDraftId), notification);
    }

    @Override
    public void notificationComplete(long productDraftId) {
        Notification notification = buildCompleteNotification(productDraftId);
        if (notification == null) {
            return;
        }
        notificationManager.notify(TAG, getNotifIdByDraft(productDraftId), notification);
        removeNotifFromList(productDraftId);
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
    public void sendSuccessBroadcast(AddProductDomainModel domainModel) {
        Intent result = new Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        Bundle bundle = new Bundle();
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_DONE);
        bundle.putString(TkpdState.ProductService.PRODUCT_NAME, domainModel.getProductName());
        bundle.putString(TkpdState.ProductService.IMAGE_URI, domainModel.getProductPrimaryPic());
        bundle.putString(TkpdState.ProductService.PRODUCT_URI, domainModel.getProductUrl());
        bundle.putString(TkpdState.ProductService.PRODUCT_DESCRIPTION, domainModel.getProductDesc());
        bundle.putString(TkpdState.ProductService.PRODUCT_ID, domainModel.getProductId() + "");
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

    private Notification buildFailedNotification(String errorMessage, long productDraftId, @ProductStatus int productStatus) {
        Intent pendingIntent = ProductDraftAddActivity.createInstance(this, productDraftId);
        if (productStatus == ProductStatus.EDIT) {
            pendingIntent = ProductDraftEditActivity.createInstance(this, productDraftId);
        }
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (notificationBuilderMap.get(productDraftId) == null) {
            createNotification(productDraftId, "");
        }
        return notificationBuilderMap.get(productDraftId)
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

    private Notification buildProgressNotification(long productDraftId, int stepNotification) {
        return notificationBuilderMap.get(productDraftId)
                .setContentText(getString(R.string.product_notification_progress_upload_product))
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(getString(R.string.product_notification_progress_upload_product)))
                .setProgress(3, stepNotification, false)
                .build();
    }

    private Notification buildCompleteNotification(long productDraftId) {
        return notificationBuilderMap.get(productDraftId)
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
