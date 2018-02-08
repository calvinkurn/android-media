package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

import javax.annotation.Nullable;

/**
 * @author by nisie on 8/19/17.
 */

public class TopPicksDomain {

    @Nullable
    private final String name;

    @Nullable
    private final String url;

    @Nullable
    private final String imageUrl;

    @Nullable
    private final String imageLandscapeUrl;

    @Nullable
    private final Boolean isParent;

    public TopPicksDomain(String name,
                          String url,
                          String imageUrl,
                          String imageLandscapeUrl,
                          Boolean isParent) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.imageLandscapeUrl = imageLandscapeUrl;
        this.isParent = isParent;
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
    public String getImageUrl() {
        return imageUrl;
    }

    @Nullable
    public String getImageLandscapeUrl() {
        return imageLandscapeUrl;
    }

    @Nullable
    public Boolean getParent() {
        return isParent;
    }
}
