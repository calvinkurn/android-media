package com.tokopedia.seller.transaction.neworder.view.appwidget;

import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction;
import com.tokopedia.seller.transaction.neworder.view.model.DataOrderViewWidget;
import com.tokopedia.seller.transaction.neworder.view.presenter.GetOrderService;
import com.tokopedia.seller.transaction.neworder.view.presenter.OrderWidgetJobService;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class NewOrderWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetIds, DataOrderViewWidget dataOrderViewWidget) {
        if (dataOrderViewWidget != null) {
            for (int appWidgetId : appWidgetIds) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_new_order);

                int randomNumber=(int)(Math.random()*1000);

                Intent intent = new Intent(context, ListOrderViewServiceWidget.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId + randomNumber);
                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ListOrderViewServiceWidget.DATA_LIST_ORDER, new ArrayList<>(dataOrderViewWidget.getDataOrderDetailViews()));
                intent.putExtra(ListOrderViewServiceWidget.BUNDLE_ORDER, bundle);
                views.setRemoteAdapter(R.id.list_order, intent);
                views.setEmptyView(R.id.list_order, R.id.view_no_result);
                views.setTextViewText(R.id.count_order, String.valueOf(dataOrderViewWidget.getDataOrderCount()));
                Intent intentOrder = ActivitySellingTransaction.createIntent(context, ActivitySellingTransaction.TAB_POSITION_SELLING_NEW_ORDER);
                intentOrder.putExtra(ActivitySellingTransaction.FROM_WIDGET_TAG, true);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentOrder, 0);
                views.setOnClickPendingIntent(R.id.container_order_count, pendingIntent);

                PendingIntent pendingIntentTemplate = PendingIntent.getActivity(context, 0, intentOrder, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setPendingIntentTemplate(R.id.list_order, pendingIntentTemplate);

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        startActionGetOrder(context);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void startActionGetOrder(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ComponentName serviceComponent = new ComponentName(context, OrderWidgetJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            builder.setMinimumLatency(1 * 1000); // wait at least
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        }else {
            Intent intent = new Intent(context, GetOrderService.class);
            intent.setAction(GetOrderService.GET_ORDER_WIDGET_ACTION);
            context.startService(intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        UnifyTracking.eventWidgetInstalled(context);
    }

    @Override
    public void onDisabled(Context context) {
        UnifyTracking.eventWidgetRemoved(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager gm = AppWidgetManager.getInstance(context);
            int[] ids = gm.getAppWidgetIds(new ComponentName(context, NewOrderWidget.class));
            if(ids != null && ids.length > 0) {
                this.onUpdate(context, gm, ids);
            }
        } else {
            super.onReceive(context, intent);
        }
    }

    public static void updateAppWidgetNoLogin(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_new_order_setup_page);

            Intent intent = SellerRouter.getActivitySplashScreenActivity(context);
            PendingIntent pendingIntentSplashScreen = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.button_sign_in, pendingIntentSplashScreen);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

