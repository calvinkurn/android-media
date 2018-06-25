package com.tokopedia.transaction.orders.orderlist.data;

public class MetaData {
        private String label;
        private String value;
        private String textColor;
        private String backgroundColor;

        public MetaData(String label, String value, String textColor, String backgroundColor) {
            this.label = label;
            this.value = value;
            this.textColor = textColor;
            this.backgroundColor = backgroundColor;
        }

        public String label() {
            return label;
        }

        public String value() {
            return value;
        }

        public String textColor() {
            return textColor;
        }

        public String backgroundColor() {
            return backgroundColor;
        }

        @Override
        public String toString() {
            return "MetaData{"
                    + "label=" + label + ", "
                    + "value=" + value + ", "
                    + "textColor=" + textColor + ", "
                    + "backgroundColor=" + backgroundColor
                    + "}";
        }
    }