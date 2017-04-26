package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.var.Badge;
import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductShopInfoDomainModel;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class ShopInfoDataToDomainMapper implements Func1<ShopModel, AddProductShopInfoDomainModel> {
    @Override
    public AddProductShopInfoDomainModel call(ShopModel shopModel) {
        return mapDomainModel(shopModel);
    }

    public static AddProductShopInfoDomainModel mapDomainModel(ShopModel shopModel) {
        AddProductShopInfoDomainModel domainModel = new AddProductShopInfoDomainModel();

        domainModel.setGoldMerchant( shopModel.info.shopIsGold == 1 );
        domainModel.setFreeReturn( shopModel.info.isFreeReturns() );
        return domainModel;
    }

}
