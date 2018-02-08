package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

/**
 * @author by nisie on 10/30/17.
 */

public class FeedTopAdsViewModel implements Visitable<FeedPlusTypeFactory> {

    private final int page;

    public FeedTopAdsViewModel(int page) {
        this.page = page;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public int getPage() {
        return page;
    }

}
