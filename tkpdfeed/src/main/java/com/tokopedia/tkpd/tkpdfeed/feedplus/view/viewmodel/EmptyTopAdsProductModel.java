package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

/**
 * @author by nisie on 7/11/17.
 */

public class EmptyTopAdsProductModel implements Visitable<FeedPlusTypeFactory> {

    private String userId;

    public EmptyTopAdsProductModel(String userId) {
        this.userId = userId;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getUserId() {
        return userId;
    }

}
