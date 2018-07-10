package com.tokopedia.transaction.orders.orderlist.data;

public class ConditionalInfo {
        private String text;
        private Color color;

        ConditionalInfo(String text, Color color) {
            this.text = text;
            this.color = color;
        }

        public String text() {
            return text;
        }

        public Color color(){
            return color;
        }
        @Override
        public String toString() {
            return "ConditionalInfo{"
                    + "text=" + text + ", "
                    + "color=" + color
                    + "}";
        }
    }