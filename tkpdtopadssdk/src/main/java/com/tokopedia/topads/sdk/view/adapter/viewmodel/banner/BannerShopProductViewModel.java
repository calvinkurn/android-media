package com.tokopedia.topads.sdk.view.adapter.viewmodel.banner;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopProductViewModel implements Item<BannerAdsTypeFactory> {

    private final CpmData.Cpm cpm;

    public BannerShopProductViewModel(CpmData.Cpm cpm) {
        this.cpm = cpm;
    }

    public CpmData.Cpm getCpm() {
        return cpm;
    }

    @Override
    public int type(BannerAdsTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int originalPos() {
        return 0;
    }
}
