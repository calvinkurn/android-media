package com.tokopedia.seller.product.view.service;

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

import com.crashlytics.android.Crashlytics;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseService;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.myproduct.ManageProduct;
import com.tokopedia.seller.product.di.component.DaggerAddProductServiceComponent;
import com.tokopedia.seller.product.di.module.AddProductserviceModule;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.utils.ViewUtils;
import com.tokopedia.seller.product.view.activity.ProductDraftAddActivity;
import com.tokopedia.seller.product.view.activity.ProductDraftEditActivity;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.view.presenter.AddProductServiceListener;
import com.tokopedia.seller.product.view.presenter.AddProductServicePresenter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class UploadProductService extends BaseService implements AddProductServiceListener {
    public static final String TAG = "upload_product";
    ;
    public static final String PRODUCT_DRAFT_ID = "PRODUCT_DRAFT_ID";
    private NotificationManager notificationManager;

    ArrayList<String> productDraftIdList = new ArrayList<>();
    HashMap<String, NotificationCompat.Builder> notificationBuilderMap = new HashMap<>();
    HashMap<String, Integer> progressMap = new HashMap<>();

    public static Intent getIntent(Context context, long productId) {
        Intent intent = new Intent(context, UploadProductService.class);
        intent.putExtra(PRODUCT_DRAFT_ID, productId);
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
                .appComponent(getApplicationComponent())
                .addProductserviceModule(new AddProductserviceModule())
                .build().inject(this);
        presenter.attachView(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long productDraftId = intent.getLongExtra(PRODUCT_DRAFT_ID, -1);
        presenter.uploadProduct(productDraftId);
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
    public void notificationFailed(Throwable error, String productDraftId, @ProductStatus int productStatus) {
        String errorMessage = ViewUtils.getGeneralErrorMessage(getApplicationContext(), error);
        Notification notification = buildFailedNotification(errorMessage, productDraftId, productStatus);
        notificationManager.notify(TAG, getNotifIdByDraft(productDraftId), notification);
        removeNotifFromList(productDraftId);
    }

    private void removeNotifFromList(String productDraftId) {
        productDraftIdList.remove(productDraftId);
        notificationBuilderMap.remove(productDraftId);
        progressMap.remove(productDraftId);
        if (productDraftIdList.size() == 0) {
            stopSelf();
        }
    }

    private int getNotifIdByDraft(String productDraftId) {
        return Integer.parseInt(productDraftId);
    }

    @Override
    public void createNotification(String productDraftId, String productName) {
        NotificationCompat.Builder builder = buildBaseNotification(productName);
        Notification notification = buildStartNotification(builder);

        notificationManager.notify(TAG, getNotifIdByDraft(productDraftId), notification);

        productDraftIdList.add(productDraftId);
        notificationBuilderMap.put(productDraftId, builder);
        progressMap.put(productDraftId, 0);
    }

    @Override
    public void notificationUpdate(String productDraftId) {
        //get progress from list and update the progress
        int stepNotification = progressMap.get(productDraftId);
        progressMap.put(productDraftId, stepNotification++);
        Notification notification = buildProgressNotification(productDraftId, stepNotification);
        notificationManager.notify(TAG, getNotifIdByDraft(productDraftId), notification);
    }

    @Override
    public void notificationComplete(String productDraftId) {
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
        String errorMessage = ViewUtils.getGeneralErrorMessage(getApplicationContext(), error);
        UnifyTracking.eventAddProductErrorServer(errorMessage);
        Intent result = new Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        Bundle bundle = new Bundle();
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR);
        bundle.putString(TkpdState.ProductService.MESSAGE_ERROR_FLAG, errorMessage);
        result.putExtras(bundle);
        sendBroadcast(result);
    }

    @Override
    public void sendSuccessBroadcast(AddProductDomainModel domainModel) {
        Intent result = new Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT);
        Bundle bundle = new Bundle();
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_DONE);
        bundle.putString(TkpdState.ProductService.PRODUCT_NAME, domainModel.getProductName());
        bundle.putString(TkpdState.ProductService.IMAGE_URI, domainModel.getProductPrimaryPic());
        bundle.putString(TkpdState.ProductService.PRODUCT_URI, domainModel.getProductUrl());
        bundle.putString(TkpdState.ProductService.PRODUCT_DESCRIPTION, domainModel.getProductDesc());
        result.putExtras(bundle);
        sendBroadcast(result);
    }

    private NotificationCompat.Builder buildBaseNotification(String productName) {
        String title = getString(R.string.product_title_notification_upload_product) + " " + productName;
        Intent pendingIntent = new Intent(this, ManageProduct.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, 0);
        return new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(getDrawableLargeIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), getDrawableIcon()))
                .setContentIntent(pIntent)
                .setGroup(getString(R.string.product_group_notification));
    }

    private int getDrawableLargeIcon() {
        if (GlobalConfig.isSellerApp()) {
            return com.tokopedia.core.R.drawable.qc_launcher2;
        } else {
            return com.tokopedia.core.R.drawable.qc_launcher;
        }
    }

    private int getDrawableIcon() {
        if (GlobalConfig.isSellerApp()) {
            return R.drawable.ic_stat_notify2;
        } else {
            return R.drawable.ic_stat_notify;
        }
    }

    private Notification buildFailedNotification(String errorMessage, String productDraftId, @ProductStatus int productStatus) {
        Intent pendingIntent = ProductDraftAddActivity.createInstance(this, productDraftId);
        if (productStatus == ProductStatus.EDIT) {
            pendingIntent = ProductDraftEditActivity.createInstance(this, productDraftId);
        }
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    private Notification buildProgressNotification(String productDraftId, int stepNotification) {
        return notificationBuilderMap.get(productDraftId)
                .setContentText(getString(R.string.product_notification_progress_upload_product))
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(getString(R.string.product_notification_progress_upload_product)))
                .setProgress(4, stepNotification, false)
                .build();
    }

    private Notification buildCompleteNotification(String productDraftId) {
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
