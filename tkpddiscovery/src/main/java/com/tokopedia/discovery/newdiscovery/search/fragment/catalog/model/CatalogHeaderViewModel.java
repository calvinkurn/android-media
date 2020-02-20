package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.CatalogAdapterTypeFactory;

/**
 * @author by errysuprayogi on 11/7/17.
 */

public class CatalogHeaderViewModel implements Visitable<CatalogAdapterTypeFactory> {


    @Override
    public int type(CatalogAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public CatalogHeaderViewModel() {
    }

}
