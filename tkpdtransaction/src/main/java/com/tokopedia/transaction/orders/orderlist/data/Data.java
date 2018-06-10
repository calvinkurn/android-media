package com.tokopedia.transaction.orders.orderlist.data;

import java.util.List;

public class Data {
        List<Order> orders;

        public Data(List<Order> orders) {
            this.orders = orders;
        }

        @Override
        public String toString() {
            return "[ orders : { " + orders + " } ]";
        }

        public List<Order> orders() {
            return orders;
        }
    }