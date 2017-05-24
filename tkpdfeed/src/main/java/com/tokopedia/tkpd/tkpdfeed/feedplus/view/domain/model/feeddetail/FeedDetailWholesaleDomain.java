package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail;

import javax.annotation.Nullable;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailWholesaleDomain {
    private final
    @Nullable
    String qty_min_fmt;

    public FeedDetailWholesaleDomain(String qty_min_fmt) {
        this.qty_min_fmt = qty_min_fmt;
    }
}
