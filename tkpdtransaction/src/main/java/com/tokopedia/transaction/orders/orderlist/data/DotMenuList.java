package com.tokopedia.transaction.orders.orderlist.data;

public class DotMenuList {
        private String name;
        private String uri;

        public DotMenuList(String name, String uri) {
            this.name = name;
            this.uri = uri;
        }

        public String name() {
            return name;
        }

        public String uri() {
            return uri;
        }
    }
