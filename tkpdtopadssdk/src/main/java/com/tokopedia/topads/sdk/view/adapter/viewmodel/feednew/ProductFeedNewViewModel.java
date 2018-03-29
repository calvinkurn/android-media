package com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.view.adapter.factory.FeedNewTypeFactory;

/**
 * @author by milhamj on 29/03/18.
 */

public class ProductFeedNewViewModel implements Item<FeedNewTypeFactory> {

    private Data data;

    @Override
    public int type(FeedNewTypeFactory typeFactory) {
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
