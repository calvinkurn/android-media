package com.tokopedia.seller.shopscore.data.mapper;

import com.tokopedia.seller.shopscore.data.source.cloud.model.ShopScoreDetailServiceModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailMapper implements Func1<ShopScoreDetailServiceModel, ShopScoreDetailDomainModel> {
    @Override
    public ShopScoreDetailDomainModel call(ShopScoreDetailServiceModel serviceModel) {
        return null;
    }
}
