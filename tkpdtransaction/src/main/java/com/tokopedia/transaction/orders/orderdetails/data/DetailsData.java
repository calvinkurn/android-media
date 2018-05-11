package com.tokopedia.transaction.orders.orderdetails.data;


import java.util.List;

public class DetailsData {
        OrderDetails orderDetails;

        public DetailsData(OrderDetails orderDetails) {
            this.orderDetails = orderDetails;
        }

        @Override
        public String toString() {
            return "[DetailsData:{" + orderDetails + "}]";
        }

        public OrderDetails details() {
            return orderDetails;
        }
    }