package com.tokopedia.seller.common.adapter.model;


import com.tokopedia.seller.common.adapter.Visitable;
import com.tokopedia.seller.common.adapter.AdapterTypeFactory;

/**
 * @author Kulomady on 1/25/17.
 */

public class EmptyModel implements Visitable<AdapterTypeFactory> {

    @Override
    public int type(AdapterTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }
}
