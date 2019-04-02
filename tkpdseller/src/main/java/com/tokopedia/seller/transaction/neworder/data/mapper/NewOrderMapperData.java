package com.tokopedia.seller.transaction.neworder.data.mapper;

import com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder.DataOrder;
import com.tokopedia.seller.transaction.neworder.domain.model.neworder.DataOrderDetailDomain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 7/14/17.
 */

public class NewOrderMapperData implements Func1<List<DataOrder>, List<DataOrderDetailDomain>> {
    @Override
    public List<DataOrderDetailDomain> call(List<DataOrder> dataOrders) {
        List<DataOrderDetailDomain> dataOrderDetailDomains;
        if(dataOrders != null) {
            dataOrderDetailDomains = new ArrayList<>();
            for(DataOrder dataOrder : dataOrders){
                dataOrderDetailDomains.add(transform(dataOrder));
            }
        }else{
            dataOrderDetailDomains = Collections.emptyList();
        }
        return dataOrderDetailDomains;
    }

    private DataOrderDetailDomain transform(DataOrder dataOrder) {
        if (dataOrder == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        final DataOrderDetailDomain dataOrderDetailDomain = new DataOrderDetailDomain();
        dataOrderDetailDomain.setPaymentProcessDayLeft(dataOrder.getOrderPayment().getPaymentProcessDayLeft());
        dataOrderDetailDomain.setCustomerName(dataOrder.getOrderCustomer().getCustomerName());
        dataOrderDetailDomain.setDetailOrderDate(dataOrder.getOrderDetail().getDetailOrderDate());
        dataOrderDetailDomain.setOrderId(dataOrder.getOrderDetail().getDetailOrderId());
        return dataOrderDetailDomain;
    }
}
