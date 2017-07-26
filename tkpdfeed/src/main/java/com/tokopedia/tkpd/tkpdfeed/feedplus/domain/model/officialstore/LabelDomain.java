package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore;

import javax.annotation.Nonnull;

/**
 * @author by nisie on 7/26/17.
 */

public class LabelDomain {

    private final
    @Nonnull
    String title;

    private final
    @Nonnull
    String color;

    public LabelDomain(@Nonnull String title,
                       @Nonnull String color) {
        this.title = title;
        this.color = color;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nonnull
    public String getColor() {
        return color;
    }
}
