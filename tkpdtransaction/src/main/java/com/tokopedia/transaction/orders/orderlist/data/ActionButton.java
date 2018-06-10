package com.tokopedia.transaction.orders.orderlist.data;

public class ActionButton {
        private String label;
        private String buttonType;
        private String uri;
        private String mappingUri;
        private String weight;
        private Color color;

        public ActionButton(String label, String buttonType, String uri, String mappingUri, String weight, Color color) {
            this.label = label;
            this.buttonType = buttonType;
            this.uri = uri;
            this.mappingUri = mappingUri;
            this.weight = weight;
            this.color = color;
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

        public Color color(){
            return color;
        }

        @Override
        public String toString() {
            return "ActionButton{"
                    + "label=" + label + ", "
                    + "buttonType=" + buttonType + ", "
                    + "uri=" + uri + ", "
                    + "mappingUri=" + mappingUri + ", "
                    + "weight=" + weight + ", "
                    + "color=" + color
                    + "}";
        }
    }