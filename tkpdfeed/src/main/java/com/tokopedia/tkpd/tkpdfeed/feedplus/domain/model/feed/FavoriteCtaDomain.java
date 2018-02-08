package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

/**
 * Created by henrypriyono on 11/30/17.
 */

public class FavoriteCtaDomain {
    private final String title;
    private final String subtitle;

    public FavoriteCtaDomain(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
