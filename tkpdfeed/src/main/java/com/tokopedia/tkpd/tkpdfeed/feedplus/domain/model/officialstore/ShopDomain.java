package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore;

import javax.annotation.Nullable;

/**
 * @author by nisie on 7/26/17.
 */

public class ShopDomain {
    private final
    @Nullable
    String name;

    private final
    @Nullable
    String url;

    private final
    @Nullable
    String url_app;


    private final
    @Nullable
    String location;


    public ShopDomain(String name, String url, String url_app, String location) {
        this.name = name;
        this.url = url;
        this.url_app = url_app;
        this.location = location;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getLocation() {
        return location;
    }

    @Nullable
    public String getUrl_app() {
        return url_app;
    }
}
