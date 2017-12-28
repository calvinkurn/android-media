package com.tokopedia.flight.banner.data.source.cloud.model;

/**
 * Created by nakama on 27/12/17.
 */

public class BannerDetail {
    private String type;
    private int id;
    private BannerAttribute attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BannerAttribute getAttributes() {
        return attributes;
    }

    public void setAttributes(BannerAttribute attributes) {
        this.attributes = attributes;
    }
}
