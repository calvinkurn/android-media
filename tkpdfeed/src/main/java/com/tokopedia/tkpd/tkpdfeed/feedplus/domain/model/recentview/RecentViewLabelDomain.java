package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewLabelDomain {

    String title;
    String color;

    public RecentViewLabelDomain(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }
}
