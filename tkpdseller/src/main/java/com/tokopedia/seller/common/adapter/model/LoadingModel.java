package com.tokopedia.seller.common.adapter.model;


import com.tokopedia.seller.common.adapter.AdapterTypeFactory;
import com.tokopedia.seller.common.adapter.Visitable;

/**
 * @author Kulomady on 1/25/17.
 */

public class LoadingModel implements Visitable<AdapterTypeFactory> {

    @Override
    public int type(AdapterTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

}
