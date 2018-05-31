package com.tokopedia.transaction.orders.orderlist.data;

public class Color {
        private String border;
        private String background;

        public Color(String border, String background) {
            this.border = border;
            this.background = background;
        }
        public String border(){
            return border;
        }

        public String background(){
            return background;
        }
        @Override
        public String toString() {
            return "Color{"
                    + "border=" + border + ", "
                    + "background=" + background

                    + "}";
        }
    }