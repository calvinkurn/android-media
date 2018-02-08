package com.tokopedia.seller.product.edit.data.mapper;

import com.tokopedia.core.shopinfo.models.shopmodel.Info;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductShopInfoDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class ShopInfoDataToDomainMapper implements Func1<ShopModel, AddProductShopInfoDomainModel> {
    /** there is calculation to determine free return status
      please take at look to this {@link Info#isFreeReturns()}
      */
    public static AddProductShopInfoDomainModel mapDomainModel(ShopModel shopModel) {
        AddProductShopInfoDomainModel domainModel = new AddProductShopInfoDomainModel();

        domainModel.setShopId(shopModel.info.shopId);
        domainModel.setGoldMerchant( shopModel.info.shopIsGold == 1 );
        // !! REQUIRED
        domainModel.setFreeReturn( shopModel.info.isFreeReturns() );
        domainModel.setOfficialStore(shopModel.info.isOfficialStore());
        return domainModel;
    }

    @Override
    public AddProductShopInfoDomainModel call(ShopModel shopModel) {
        return mapDomainModel(shopModel);
    }

}
