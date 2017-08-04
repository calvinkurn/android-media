package com.tokopedia.ride.bookingride.domain.model;

/**
 * Created by alvarisi on 3/31/17.
 */

public class Promo {
    private String code;
    private String offer;
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
