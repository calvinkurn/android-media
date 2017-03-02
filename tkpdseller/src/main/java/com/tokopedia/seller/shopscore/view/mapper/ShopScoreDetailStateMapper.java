package com.tokopedia.seller.shopscore.view.mapper;

import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailStateEnum;

/**
 * Created by sebastianuskh on 3/2/17.
 */
public class ShopScoreDetailStateMapper {
    public static ShopScoreDetailStateEnum map(ShopScoreDetailDomainModel domainModels) {
        switch (domainModels.getState()) {
            case ShopScoreDetailDomainModel.GOLD_MERCHANT_QUALIFIED_BADGE:
                return ShopScoreDetailStateEnum.GOLD_MERCHANT_QUALIFIED_BADGE;
            case ShopScoreDetailDomainModel.GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                return ShopScoreDetailStateEnum.GOLD_MERCHANT_NOT_QUALIFIED_BADGE;
            case ShopScoreDetailDomainModel.NOT_GOLD_MERCHANT_QUALIFIED_BADGE:
                return ShopScoreDetailStateEnum.NOT_GOLD_MERCHANT_QUALIFIED_BADGE;
            case ShopScoreDetailDomainModel.NOT_GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                return ShopScoreDetailStateEnum.NOT_GOLD_MERCHANT_NOT_QUALIFIED_BADGE;
            default:
                return ShopScoreDetailStateEnum.GOLD_MERCHANT_QUALIFIED_BADGE;
        }
    }
}
