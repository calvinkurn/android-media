package com.tokopedia.seller.transaction.neworder.view.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction;
import com.tokopedia.seller.transaction.neworder.view.model.DataOrderViewWidget;
import com.tokopedia.seller.transaction.neworder.view.presenter.GetOrderService;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class NewOrderWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetIds, DataOrderViewWidget dataOrderViewWidget) {
        if (dataOrderViewWidget != null) {
            for (int appWidgetId : appWidgetIds) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_order_widget);

                Intent intent = new Intent(context, ListOrderViewServiceWidget.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ListOrderViewServiceWidget.DATA_LIST_ORDER, new ArrayList<>(dataOrderViewWidget.getDataOrderDetailViews()));
                intent.putExtra(ListOrderViewServiceWidget.BUNDLE_ORDER, bundle);
                views.setRemoteAdapter(R.id.list_order, intent);
                views.setEmptyView(R.id.list_order, R.id.image_no_result);
                views.setTextViewText(R.id.count_order, String.valueOf(dataOrderViewWidget.getDataOrderCount()));
                Intent intentOrder = ActivitySellingTransaction.createIntent(context, SellerRouter.TAB_POSITION_SELLING_NEW_ORDER);
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
        Intent intent = new Intent(context, GetOrderService.class);
        intent.setAction(GetOrderService.GET_ORDER_WIDGET_ACTION);
        context.startService(intent);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
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
}

