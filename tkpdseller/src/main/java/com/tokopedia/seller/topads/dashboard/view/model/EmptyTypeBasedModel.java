package com.tokopedia.seller.topads.dashboard.view.model;

/**
 * @author normansyahputa on 3/9/17.
 */

public class EmptyTypeBasedModel implements BaseTopAdsProductModel, TypeBasedModel {
    public static final int TYPE = 12921381;

    @Override
    public TopAdsProductViewModel getTopAdsProductViewModel() {
        return null;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
