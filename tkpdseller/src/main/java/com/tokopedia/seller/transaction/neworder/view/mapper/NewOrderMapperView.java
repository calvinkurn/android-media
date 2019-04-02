package com.tokopedia.seller.transaction.neworder.view.mapper;

import com.tokopedia.seller.transaction.neworder.domain.model.neworder.DataOrderDetailDomain;
import com.tokopedia.seller.transaction.neworder.domain.model.neworder.DataOrderDomainWidget;
import com.tokopedia.seller.transaction.neworder.view.model.DataOrderDetailView;
import com.tokopedia.seller.transaction.neworder.view.model.DataOrderViewWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zulfikarrahman on 7/14/17.
 */

public class NewOrderMapperView {
    public static List<DataOrderDetailView> convertToListDetailView(List<DataOrderDetailDomain> dataOrders) {
        List<DataOrderDetailView> dataOrderViewWidgets;
        if(dataOrders != null) {
            dataOrderViewWidgets = new ArrayList<>();
            for(DataOrderDetailDomain dataOrder : dataOrders){
                dataOrderViewWidgets.add(transform(dataOrder));
            }
        }else{
            dataOrderViewWidgets = Collections.emptyList();
        }
        return dataOrderViewWidgets;
    }

    private static DataOrderDetailView transform(DataOrderDetailDomain dataOrder) {
        if (dataOrder == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final DataOrderDetailView dataOrderDetailView = new DataOrderDetailView();
        dataOrderDetailView.setPaymentProcessDayLeft(dataOrder.getPaymentProcessDayLeft());
        dataOrderDetailView.setCustomerName(dataOrder.getCustomerName());
        dataOrderDetailView.setDetailOrderDate(dataOrder.getDetailOrderDate());
        dataOrderDetailView.setOrderId(dataOrder.getOrderId());
        return dataOrderDetailView;
    }

    public static DataOrderViewWidget converToModelView(DataOrderDomainWidget dataOrderDomainWidget) {
        if (dataOrderDomainWidget == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final DataOrderViewWidget dataOrderViewWidget = new DataOrderViewWidget();
        dataOrderViewWidget.setDataOrderCount(dataOrderDomainWidget.getDataOrderCount());
        dataOrderViewWidget.setDataOrderDetailViews(convertToListDetailView(dataOrderDomainWidget.getDataOrderDetailDomains()));
        return dataOrderViewWidget;
    }
}
