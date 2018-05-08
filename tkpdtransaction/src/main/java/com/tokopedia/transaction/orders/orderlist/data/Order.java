package com.tokopedia.transaction.orders.orderlist.data;

import java.util.List;

public class Order {
        private ConditionalInfo conditionalInfo;
        private PaymentData paymentData;
        private String paymentID;
        private String categoryName;
        private String category;
        private String id;
        private String createdAt;
        private String status;
        private String statusStr;
        private String statusColor;
        private String invoiceRefNum;
        private String title;
        private List<MetaData> metaData;
        private List<DotMenuList> dotMenuList;
        private List<ActionButton> actionButtons;
        private String totalInvoices;
        private String itemCount;
        private List<Item> items;


        public Order(ConditionalInfo conditionalInfo, PaymentData paymentData, String paymentID,
                     String categoryName, String category, String id,
                     String createdAt, String status, String statusStr, String statusColor, String invoiceRefNum,
                     String title, List<MetaData> metaData, List<DotMenuList> dotMenuList,
                     List<ActionButton> actionButtons, String totalInvoices, String itemCount, List<Item> items) {
            this.conditionalInfo = conditionalInfo;
            this.paymentData = paymentData;
            this.paymentID = paymentID;
            this.categoryName = categoryName;
            this.category = category;
            this.id = id;
            this.createdAt = createdAt;
            this.status = status;
            this.statusStr = statusStr;
            this.statusColor = statusColor;
            this.invoiceRefNum = invoiceRefNum;
            this.title = title;
            this.metaData = metaData;
            this.dotMenuList = dotMenuList;
            this.actionButtons = actionButtons;
            this.totalInvoices = totalInvoices;
            this.itemCount = itemCount;
            this.items = items;
        }

        public ConditionalInfo conditionalInfo() {
            return conditionalInfo;
        }

        public PaymentData paymentData() {
            return paymentData;
        }

        public String paymentID() {
            return paymentID;
        }

        public String categoryName() {
            return categoryName;
        }

        public String category() {
            return category;
        }

        public String id() {
            return id;
        }

        public String createdAt() {
            return createdAt;
        }

        public String status() {
            return status;
        }

        public String statusStr() {
            return statusStr;
        }

        public String statusColor() {
            return statusColor;
        }

        public String invoiceRefNum() {
            return invoiceRefNum;
        }

        public String title() {
            return title;
        }

        public List<MetaData> metaData() {
            return metaData;
        }

        public List<DotMenuList> dotMenuList() {
            return dotMenuList;
        }

        public List<ActionButton> actionButtons() {
            return actionButtons;
        }

        public String totalInvoices() {
            return totalInvoices;
        }

        public List<Item> items() {
            return items;
        }

        @Override
        public String toString() {

            return "[ conditionalInfo : { " + conditionalInfo + " } " +
                    ", paymentData : " + paymentData +
                    ", paymentID : " + paymentID +
                    ", status : " + status +
                    ", statusColor : " + statusColor +
                    ", statusStr : " + statusStr +
                    ", metaData : " + metaData +
                    ", actionButtons : " + actionButtons +
                    "]";
        }

    }