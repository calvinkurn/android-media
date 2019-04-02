package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;


import com.google.gson.Gson;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

public class TopAdsViewModel implements Visitable<ProductListTypeFactory> {


    private Data topadsData = new Data();
    private String query;

    public TopAdsViewModel(Data data, String query) {
        this.topadsData = data;
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public Data getTopadsData() {
        return topadsData;
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
