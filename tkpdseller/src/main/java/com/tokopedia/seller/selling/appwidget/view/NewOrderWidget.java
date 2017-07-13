package com.tokopedia.seller.selling.appwidget.view;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.tokopedia.seller.R;
import com.tokopedia.seller.selling.appwidget.presenter.GetOrderService;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;

import org.parceler.Parcels;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NewOrderWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetIds, List<DataOrder> dataOrders) {
        for(int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_order_widget);
            views.setTextViewText(R.id.count_order, String.valueOf(dataOrders.size()));

            Intent intent = new Intent(context, ListOrderViewServiceWidget.class);
            intent.putExtra(ListOrderViewServiceWidget.DATA_LIST_ORDER, Parcels.wrap(dataOrders));
            views.setRemoteAdapter(R.id.list_order, intent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_order);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        startActionGetOrder(context);
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
}

