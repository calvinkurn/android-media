package com.tokopedia.seller.transaction.neworder.view.appwidget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.tokopedia.seller.R;
import com.tokopedia.seller.transaction.neworder.common.UtilsWidget;
import com.tokopedia.seller.transaction.neworder.view.model.DataOrderDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public class ListOrderViewServiceWidget extends RemoteViewsService {
    public static final String DATA_LIST_ORDER = "data_list_order";
    public static final String BUNDLE_ORDER = "bundle_order";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListOrderViewFactoryWidget(getApplicationContext(), intent);
    }

    class ListOrderViewFactoryWidget implements RemoteViewsService.RemoteViewsFactory {
        List<DataOrderDetailView> dataOrders;
        private final Context context;

        public ListOrderViewFactoryWidget(Context applicationContext, Intent intent) {
            this.context = applicationContext;
            Bundle bundle = intent.getBundleExtra(BUNDLE_ORDER);
            this.dataOrders = bundle.getParcelableArrayList(DATA_LIST_ORDER);
            if(dataOrders == null){
                dataOrders = new ArrayList<>();
            }
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
            DataOrderDetailView dataOrder = dataOrders.get(i);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_new_order_detail_list);
            remoteViews.setTextViewText(R.id.new_order_remaining_days, UtilsWidget.getDaysLeftDeadlineOrder(dataOrder.getPaymentProcessDayLeft(), context));
            remoteViews.setInt(R.id.new_order_remaining_days, "setBackgroundResource", UtilsWidget.getColorLeftDeadLineOrder(dataOrder.getPaymentProcessDayLeft()));
            remoteViews.setTextViewText(R.id.new_order_date, dataOrder.getDetailOrderDate());
            remoteViews.setTextViewText(R.id.new_order_name, dataOrder.getCustomerName());

            remoteViews.setOnClickFillInIntent(R.id.container_view, new Intent());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}

