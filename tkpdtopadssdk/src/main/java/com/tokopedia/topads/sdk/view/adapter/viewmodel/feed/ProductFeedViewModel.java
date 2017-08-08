package com.tokopedia.topads.sdk.view.adapter.viewmodel.feed;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.view.adapter.factory.AdsTypeFactory;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductFeedViewModel implements Item<AdsTypeFactory> {

    private Data data;

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

    @Override
    public int originalPos() {
        return 0;
    }
}
