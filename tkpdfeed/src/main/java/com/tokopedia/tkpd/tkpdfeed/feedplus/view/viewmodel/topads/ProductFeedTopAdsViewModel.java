package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.topads.AdsTypeFactory;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductFeedTopAdsViewModel implements Visitable<AdsTypeFactory> {

    private Data data;

    public ProductFeedTopAdsViewModel(Data data) {
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
