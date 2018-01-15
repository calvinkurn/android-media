package com.tokopedia.seller.shop.open.domain.model;

/**
 * Created by zulfikarrahman on 1/5/18.
 */

public class ShopOpenSaveInfoRequestDomainModel {
    private String picSrc;
    private String picUploaded;
    private String serverId;

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public void setPicUploaded(String picUploaded) {
        this.picUploaded = picUploaded;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public String getPicUploaded() {
        return picUploaded;
    }

    public String getServerId() {
        return serverId;
    }
}
