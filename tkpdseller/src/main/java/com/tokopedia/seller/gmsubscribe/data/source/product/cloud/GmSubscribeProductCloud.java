package com.tokopedia.seller.gmsubscribe.data.source.product.cloud;

import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.api.GoldMerchantApi;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GmServiceModel;
import com.tokopedia.seller.gmsubscribe.data.tools.GetResponse;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmSubscribeProductCloud {

    private final GoldMerchantApi api;

    public GmSubscribeProductCloud(GoldMerchantApi api) {
        this.api = api;
    }

    public Observable<GmServiceModel> getProduct() {
        return api
                .getGoldMerchantProductList()
                .map(new GetResponse<GmServiceModel>());
    }

}
