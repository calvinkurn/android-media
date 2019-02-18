package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;


import com.google.gson.Gson;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

public class TopAdsViewModel implements Visitable<ProductListTypeFactory> {


    private List<Data> dataList = new ArrayList<>();
    private String query;

    public TopAdsViewModel(TopAdsModel model, String query) {
        if (model.getData() != null && model.getData().size() > 0) {
            this.dataList = model.getData();
            this.query = query;
        }
    }

    public String getQuery() {
        return query;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
