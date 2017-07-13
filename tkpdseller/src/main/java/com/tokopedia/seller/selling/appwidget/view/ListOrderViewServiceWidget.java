package com.tokopedia.seller.selling.appwidget.view;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.tokopedia.seller.selling.appwidget.data.source.cloud.model.neworder.DataOrder;

import java.util.List;

/**
 * Created by zulfikarrahman on 7/12/17.
 */

public class ListOrderViewServiceWidget extends RemoteViewsService {
    public static final String DATA_LIST_ORDER = "data_list_order";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        List<DataOrder> dataOrders = intent.getParcelableExtra(DATA_LIST_ORDER);
        return new ListOrderViewFactoryWidget(getApplicationContext(), dataOrders);
    }
}
