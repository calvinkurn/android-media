package com.tokopedia.topads.sdk.view.adapter.viewmodel;

import com.tokopedia.topads.sdk.base.adapter.Visitable;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.view.adapter.factory.TypeFactory;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductGridViewModel implements Visitable<TypeFactory> {

    private Data data;

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
