
package com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataNewOrder {

    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("list")
    @Expose
    private List<DataOrder> dataOrders = null;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<DataOrder> getDataOrder() {
        return dataOrders;
    }

    public void setList(List<DataOrder> dataOrders) {
        this.dataOrders = dataOrders;
    }

}
