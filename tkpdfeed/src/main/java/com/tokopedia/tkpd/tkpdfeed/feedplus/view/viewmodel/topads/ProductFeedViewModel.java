package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Data;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.topads.AdsTypeFactory;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductFeedViewModel implements Visitable<AdsTypeFactory> {

    private Data data;

    public ProductFeedViewModel(Data data) {
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
