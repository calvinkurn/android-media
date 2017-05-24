package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail;

import javax.annotation.Nullable;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailMetaDomain {

    private final
    @Nullable
    Boolean has_next_page;

    public FeedDetailMetaDomain(Boolean has_next_page) {
        this.has_next_page = has_next_page;
    }
}
