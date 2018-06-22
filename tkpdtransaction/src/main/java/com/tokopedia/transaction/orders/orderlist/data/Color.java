package com.tokopedia.transaction.orders.orderlist.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Color {
    @SerializedName("border")
    @Expose
        private String border;
    @SerializedName("background")
    @Expose
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