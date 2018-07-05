package com.tokopedia.transaction.orders.orderlist.data;

import java.util.List;

public class Data {
        List<Order> orders;
        List<String> orderLabelList;

        public Data(List<Order> orders, List<String> orderLabelList) {
            this.orders = orders;
            this.orderLabelList = orderLabelList;
        }

        @Override
        public String toString() {
            return "[ orders : { " + orders + " } ]";
        }

        public List<Order> orders() {
            return orders;
        }

    public List<String> orderLabelList() {
        return orderLabelList;
    }
}