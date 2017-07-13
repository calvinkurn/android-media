package com.tokopedia.seller.selling.appwidget.presenter;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.seller.product.di.module.AddProductserviceModule;
import com.tokopedia.seller.selling.appwidget.di.DaggerNewOrderWidgetComponent;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;
import com.tokopedia.seller.selling.appwidget.di.NewOrderWidgetModule;
import com.tokopedia.seller.selling.appwidget.view.GetNewOrderView;
import com.tokopedia.seller.selling.appwidget.view.NewOrderWidget;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public class GetOrderService extends IntentService implements GetNewOrderView {
    public static final String GET_ORDER_WIDGET_ACTION = "com.tokopedia.seller.selling.appwidget.get_order";

    @Inject
    GetNewOrderPresenter presenter;

    public GetOrderService() {
        super("GetOrderService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerNewOrderWidgetComponent
                .builder()
                .appComponent(getApplicationComponent())
                .newOrderWidgetModule(new NewOrderWidgetModule())
                .build().inject(this);
        presenter.attachView(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (GET_ORDER_WIDGET_ACTION.equals(action)) {
                getNewOrder();
            }
        }
    }

    public void getNewOrder() {
        presenter.getNewOrder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onSuccessGetDataOrders(List<DataOrder> dataOrders) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds =  appWidgetManager.getAppWidgetIds(new ComponentName(this, NewOrderWidget.class));
        NewOrderWidget.updateAppWidget(this, appWidgetManager, appWidgetIds, dataOrders);
    }

    @Override
    public void onErrorGetDataOrders() {

    }

    private AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication())
                .getApplicationComponent(getServiceModule());
    }

    private ActivityModule getServiceModule() {
        return new ActivityModule(this);
    }
}
