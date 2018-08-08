package com.tokopedia.tkpdpdp.estimasiongkir;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RatesModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("texts")
    @Expose
    private RatesTextModel texts;

    @SerializedName("attributes")
    @Expose
    private List<ShippingServiceModel> attributes;

    public List<ShippingServiceModel> getAttributes() {
        return attributes;
    }

    public RatesModel() {
        attributes = new ArrayList<>();
    }

    public RatesTextModel getTexts() {
        return texts;
    }

    public class RatesTextModel {
        @SerializedName("text_min_price")
        @Expose
        private String textMinPrice;

        @SerializedName("text_destination")
        @Expose
        private String textDestination;

        public String getTextMinPrice() {
            return textMinPrice;
        }

        public String getTextDestination() {
            return textDestination;
        }
    }
}
