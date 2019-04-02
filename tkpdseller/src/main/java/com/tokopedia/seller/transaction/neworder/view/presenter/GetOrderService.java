package com.tokopedia.seller.transaction.neworder.view.presenter;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tokopedia.core.app.BaseService;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.transaction.neworder.di.DaggerNewOrderWidgetComponent;
import com.tokopedia.seller.transaction.neworder.di.NewOrderWidgetModule;
import com.tokopedia.seller.transaction.neworder.view.model.DataOrderViewWidget;
import com.tokopedia.seller.transaction.neworder.view.appwidget.GetNewOrderView;
import com.tokopedia.seller.transaction.neworder.view.appwidget.NewOrderWidget;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public class GetOrderService extends IntentService implements GetNewOrderView {
    public static final String GET_ORDER_WIDGET_ACTION = "com.tokopedia.seller.selling.appwidget.get_order";

    @Inject
    GetNewOrderPresenter presenter;

    public GetOrderService() {
        super(GetOrderService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerNewOrderWidgetComponent
                .builder()
                .appComponent(((MainApplication) getApplication())
                        .getApplicationComponent())
                .newOrderWidgetModule(new NewOrderWidgetModule())
                .build().inject(this);
        presenter.attachView(this);
    }


    public void getNewOrder() {
        if(SessionHandler.isV4Login(this) && SessionHandler.isUserHasShop(this)) {
            presenter.getNewOrderAndCount();
        }else{
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds =  appWidgetManager.getAppWidgetIds(new ComponentName(this, NewOrderWidget.class));

            NewOrderWidget.updateAppWidgetNoLogin(this, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    @Override
    public void onSuccessGetDataOrders(DataOrderViewWidget dataOrderViewWidget) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds =  appWidgetManager.getAppWidgetIds(new ComponentName(this, NewOrderWidget.class));

        NewOrderWidget.updateAppWidget(this, appWidgetManager, appWidgetIds, dataOrderViewWidget);
    }

    @Override
    public void onErrorGetDataOrders() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds =  appWidgetManager.getAppWidgetIds(new ComponentName(this, NewOrderWidget.class));

        NewOrderWidget.updateAppWidget(this, appWidgetManager, appWidgetIds, null);
    }
}
