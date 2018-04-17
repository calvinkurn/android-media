package com.tokopedia.topads.sdk.view.adapter.viewmodel.banner;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 4/16/18.
 */

public class BannerShopProductViewModel implements Item<BannerAdsTypeFactory> {

    private List<Product> productList;

    public BannerShopProductViewModel(CpmData.Cpm cpm) {
        if (cpm != null && cpm.getCpmShop() != null) {
            productList = cpm.getCpmShop().getProducts();
        } else {
            productList = new ArrayList<>();
        }
    }

    public List<Product> getProductList() {
        return productList;
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
