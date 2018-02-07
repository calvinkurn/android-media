package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Data;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.topads.AdsTypeFactory;

/**
 * @author by errysuprayogi on 3/30/17.
 */

public class ShopFeedTopAdsViewModel implements Visitable<AdsTypeFactory> {

    private Data data;

    public ShopFeedTopAdsViewModel(Data data) {
        this.data = data;
    }

    @Override
    public int type(AdsTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
