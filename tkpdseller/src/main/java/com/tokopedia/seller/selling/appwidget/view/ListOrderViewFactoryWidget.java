package com.tokopedia.seller.selling.appwidget.view;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.tokopedia.seller.R;
import com.tokopedia.seller.selling.appwidget.UtilsWidget;
import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public class ListOrderViewFactoryWidget implements RemoteViewsService.RemoteViewsFactory {
    List<DataOrder> dataOrders;
    private final Context context;

    public ListOrderViewFactoryWidget(Context applicationContext, List<DataOrder> dataOrders) {
        this.context = applicationContext;
        this.dataOrders = dataOrders;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return dataOrders.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        DataOrder dataOrder = dataOrders.get(i);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_order_detail_list_widget);
        remoteViews.setTextViewText(R.id.new_order_remaining_days, UtilsWidget.getDaysLeftDeadlineOrder(dataOrder.getOrderPayment().getPaymentProcessDayLeft()));
        remoteViews.setInt(R.id.new_order_remaining_days, "setBackgroundResource", UtilsWidget.getColorLeftDeadLineOrder(dataOrder.getOrderPayment().getPaymentProcessDayLeft()));
        remoteViews.setTextViewText(R.id.new_order_date, dataOrder.getOrderDetail().getDetailOrderDate());
        remoteViews.setTextViewText(R.id.new_order_name, dataOrder.getOrderCustomer().getCustomerName());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
