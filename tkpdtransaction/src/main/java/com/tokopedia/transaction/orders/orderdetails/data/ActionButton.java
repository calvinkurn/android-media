package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionButton {
    @SerializedName("label")
    @Expose
        private String label;
    @SerializedName("buttonType")
    @Expose
        private String buttonType;
    @SerializedName("uri")
    @Expose
        private String uri;
    @SerializedName("mappingUri")
    @Expose
        private String mappingUri;
    @SerializedName("weight")
    @Expose
        private String weight;

        public ActionButton(String label, String buttonType, String uri, String mappingUri, String weight) {
            this.label = label;
            this.buttonType = buttonType;
            this.uri = uri;
            this.mappingUri = mappingUri;
            this.weight = weight;
        }

        public String label() {
            return label;
        }

        public String buttonType() {
            return buttonType;
        }

        public String uri() {
            return uri;
        }

        public String mappingUri() {
            return mappingUri;
        }

        public String weight() {
            return weight;
        }

        @Override
        public String toString() {
            return "ActionButton{"
                    + "label=" + label + ", "
                    + "buttonType=" + buttonType + ", "
                    + "uri=" + uri + ", "
                    + "mappingUri=" + mappingUri + ", "
                    + "weight=" + weight + ", "
                    + "}";
        }
    }