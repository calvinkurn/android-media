package com.tokopedia.session.domain.pojo.discover;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverPojo {

    @SerializedName("providers")
    @Expose
    private List<DiscoverItemPojo> providers;

    @SerializedName("url_background_seller")
    @Expose
    private String urlBackground;

    public List<DiscoverItemPojo> getProviders() {
        return providers;
    }

    public void setProviders(List<DiscoverItemPojo> providers) {
        this.providers = providers;
    }

    public String getUrlBackground() {
        return urlBackground;
    }

    public void setUrlBackground(String urlBackground) {
        this.urlBackground = urlBackground;
    }

}
