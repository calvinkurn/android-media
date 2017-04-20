package com.tokopedia.seller.product.view.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.core.app.BaseService;
import com.tokopedia.seller.product.di.component.DaggerAddProductServiceComponent;
import com.tokopedia.seller.product.di.module.AddProductserviceModule;
import com.tokopedia.seller.product.view.presenter.AddProductServiceListener;
import com.tokopedia.seller.product.view.presenter.AddProductServicePresenter;

import javax.inject.Inject;

public class AddProductService extends BaseService implements AddProductServiceListener {

    public static final String PRODUCT_DRAFT_ID = "PRODUCT_DRAFT_ID";
    private static final int NOTIFICATION_ID = 100;

    public static Intent getIntent(Context context, long productId) {
        Intent intent = new Intent(context, AddProductService.class);
        intent.putExtra(PRODUCT_DRAFT_ID, productId);
        return intent;
    }

    @Inject
    AddProductServicePresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
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
        Notification notification =
                new NotificationCompat.Builder(this)
                .setContentTitle("Uploading Product")
                .setOngoing(true)
                .build();
        startForeground(NOTIFICATION_ID, notification);
        presenter.addProduct(productDraftId);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSuccessAddProduct() {
        stopForeground(true);
        stopSelf();
    }
}
