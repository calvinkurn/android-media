package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore;

import javax.annotation.Nonnull;

/**
 * @author by nisie on 7/26/17.
 */

public class BadgeDomain {

    private final
    @Nonnull
    String title;

    private final
    @Nonnull
    String image_url;

    public BadgeDomain(@Nonnull String title,
                       @Nonnull String image_url) {
        this.title = title;
        this.image_url = image_url;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nonnull
    public String getImage_url() {
        return image_url;
    }
}
