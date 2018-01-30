package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 * Copied to feed by milhamj 1/18/17.
 */
public class WholesalePrice {
    private String quantityMinFormat;
    private String quantityMaxFormat;
    private String priceFormat;

    public WholesalePrice(String quantityMinFormat, String quantityMaxFormat, String priceFormat) {
        this.quantityMinFormat = quantityMinFormat;
        this.quantityMaxFormat = quantityMaxFormat;
        this.priceFormat = priceFormat;
    }

    public String getQuantityMinFormat() {
        return quantityMinFormat;
    }

    public void setQuantityMinFormat(String quantityMinFormat) {
        this.quantityMinFormat = quantityMinFormat;
    }

    public String getQuantityMaxFormat() {
        return quantityMaxFormat;
    }

    public void setQuantityMaxFormat(String quantityMaxFormat) {
        this.quantityMaxFormat = quantityMaxFormat;
    }

    public String getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(String priceFormat) {
        this.priceFormat = priceFormat;
    }
}
