package com.tokopedia.seller.product.view.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.core.app.BaseService;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.myproduct.ManageProduct;
import com.tokopedia.seller.product.di.component.DaggerAddProductServiceComponent;
import com.tokopedia.seller.product.di.module.AddProductserviceModule;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.view.activity.ProductDraftActivity;
import com.tokopedia.seller.product.view.presenter.AddProductServiceListener;
import com.tokopedia.seller.product.view.presenter.AddProductServicePresenter;

import javax.inject.Inject;

public class UploadProductService extends BaseService implements AddProductServiceListener {

    public static final String PRODUCT_DRAFT_ID = "PRODUCT_DRAFT_ID";
    private static final int NOTIFICATION_ID = 100;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

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
        stopForeground(false);
        stopSelf();
    }

    @Override
    public void onFailedAddProduct() {
        stopForeground(false);
        stopSelf();
    }

    @Override
    public void notificationFailed(String errorMessage, String productDraftId) {
        Notification notification = buildFailedNotification(errorMessage, productDraftId);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void createNotification(String productName){
        buildBaseNotification(productName);
        Notification notification = buildStartNotification();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public void notificationUpdate(int stepNotification) {
        Notification notification = buildProgressNotification(stepNotification);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void notificationComplete() {
        Notification notification = buildCompleteNotification();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void sendFailedBroadcast(String errorMessage) {
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

    private void buildBaseNotification(String productName) {
        String title = getString(R.string.title_notification_upload_product) + " " + productName;
        Intent pendingIntent = new Intent(this, ManageProduct.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, 0);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.qc_launcher2)
                .setContentIntent(pIntent)
                .setGroup(getString(R.string.group_notification));
    }

    private Notification buildFailedNotification(String errorMessage, String productDraftId) {
        Intent pendingIntent = ProductDraftActivity.createInstance(this, productDraftId);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, pendingIntent, 0);
        return notificationBuilder
                .setContentText(errorMessage)
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(errorMessage)
                )
                .setContentIntent(pIntent)
                .setProgress(0, 0, false)
                .setOngoing(false)
                .build();
    }

    private Notification buildStartNotification() {
        return notificationBuilder
                .setContentText(getString(R.string.notification_start_upload_product))
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(getString(R.string.notification_start_upload_product))
                )
                .build();
    }

    private Notification buildProgressNotification(int stepNotification) {
        return notificationBuilder
                .setContentText(getString(R.string.notification_progress_upload_product))
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(getString(R.string.notification_progress_upload_product)))
                .setProgress(4, stepNotification, false)
                .build();
    }

    private Notification buildCompleteNotification() {
        return notificationBuilder
                .setContentText(getString(R.string.notification_complete_upload_product))
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(getString(R.string.notification_complete_upload_product))
                )
                .setProgress(0, 0, false)
                .setOngoing(false)
                .build();
    }


}
