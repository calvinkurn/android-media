package com.tokopedia.home.beranda.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeData {
    @Expose
    @SerializedName("dynamicHomeChannel")
    private DynamicHomeChannel dynamicHomeChannel;

    @Expose
    @SerializedName("slides")
    private BannerDataModel slides;

    @Expose
    @SerializedName("ticker")
    private Ticker ticker;

    @Expose
    @SerializedName("dynamicHomeIcon")
    private DynamicHomeIcon dynamicHomeIcon;

    public DynamicHomeChannel getDynamicHomeChannel() {
        return dynamicHomeChannel;
    }

    public void setDynamicHomeChannel(DynamicHomeChannel dynamicHomeChannel) {
        this.dynamicHomeChannel = dynamicHomeChannel;
    }

    public BannerDataModel getSlides() {
        return slides;
    }

    public void setSlides(BannerDataModel slides) {
        this.slides = slides;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public DynamicHomeIcon getDynamicHomeIcon() {
        return dynamicHomeIcon;
    }

    public void setDynamicHomeIcon(DynamicHomeIcon dynamicHomeIcon) {
        this.dynamicHomeIcon = dynamicHomeIcon;
    }
}
