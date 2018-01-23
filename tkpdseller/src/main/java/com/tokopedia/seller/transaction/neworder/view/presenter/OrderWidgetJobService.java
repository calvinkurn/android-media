package com.tokopedia.seller.transaction.neworder.view.presenter;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.transaction.neworder.di.NewOrderWidgetModule;
import com.tokopedia.seller.transaction.neworder.view.appwidget.GetNewOrderView;
import com.tokopedia.seller.transaction.neworder.view.appwidget.NewOrderWidget;
import com.tokopedia.seller.transaction.neworder.view.model.DataOrderViewWidget;
import com.tokopedia.seller.transaction.neworder.di.DaggerNewOrderWidgetComponent;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 1/3/18.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OrderWidgetJobService extends JobService implements GetNewOrderView {
    @Inject
    GetNewOrderPresenter presenter;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        DaggerNewOrderWidgetComponent
                .builder()
                .appComponent(((MainApplication) getApplication())
                        .getApplicationComponent())
                .newOrderWidgetModule(new NewOrderWidgetModule())
                .build().inject(this);
        presenter.attachView(this);
        getNewOrder();

        return true;
    }

    public void getNewOrder() {
        if(SessionHandler.isV4Login(this) && SessionHandler.isUserHasShop(this)) {
            presenter.getNewOrderAndCountAsync();
        }else{
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds =  appWidgetManager.getAppWidgetIds(new ComponentName(this, NewOrderWidget.class));

            NewOrderWidget.updateAppWidgetNoLogin(this, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        presenter.unSubscribe();
        presenter.detachView();
        return true;
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
