package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import javax.annotation.Nonnull;

/**
 * @author by nisie on 7/31/17.
 */

public class LabelsViewModel {

    private final
    @Nonnull
    String title;

    private final
    @Nonnull
    String color;

    public LabelsViewModel(@Nonnull String title, @Nonnull String color) {
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
