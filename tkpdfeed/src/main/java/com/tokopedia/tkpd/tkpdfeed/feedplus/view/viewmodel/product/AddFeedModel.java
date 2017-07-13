package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

/**
 * Created by stevenfredian on 5/31/17.
 */

public class AddFeedModel implements Visitable<FeedPlusTypeFactory> {

    @Override
    public int type(FeedPlusTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }
}

