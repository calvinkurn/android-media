package com.tokopedia.transaction.orders.orderlist.data;

public class Item {
        private String imageUrl;
        private String name;

        public Item(String imageUrl, String name) {
            this.imageUrl = imageUrl;
            this.name = name;
        }

        public String imageUrl() {
            return imageUrl;
        }

        public String name() {
            return name;
        }
    }
