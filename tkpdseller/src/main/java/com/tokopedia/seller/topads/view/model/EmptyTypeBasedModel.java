package com.tokopedia.seller.topads.view.model;

/**
 * @author normansyahputa on 3/9/17.
 */

public class EmptyTypeBasedModel extends TypeBasedModel implements BaseTopAdsProductModel {
    public static final int TYPE = 12921381;

    public EmptyTypeBasedModel() {
        super(TYPE);
    }

    @Override
    public TopAdsProductViewModel getTopAdsProductViewModel() {
        return null;
    }
}
