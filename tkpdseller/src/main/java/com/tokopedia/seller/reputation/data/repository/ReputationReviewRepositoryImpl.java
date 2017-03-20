package com.tokopedia.seller.reputation.data.repository;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.reputation.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.seller.reputation.domain.ReputationReviewRepository;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;
import com.tokopedia.seller.util.ShopNetworkController;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 3/16/17.
 */

public class ReputationReviewRepositoryImpl implements ReputationReviewRepository {
    private CloudReputationReviewDataSource cloudReputationReviewDataSource;
    private ShopNetworkController shopNetworkController;

    public ReputationReviewRepositoryImpl(
            CloudReputationReviewDataSource cloudReputationReviewDataSource,
            ShopNetworkController shopNetworkController) {
        this.cloudReputationReviewDataSource = cloudReputationReviewDataSource;
        this.shopNetworkController = shopNetworkController;
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param) {
        return cloudReputationReviewDataSource.getReputationHistory(shopId, param);
    }

    @Override
    public Observable<ShopModel> getShopInfo(String userid, String deviceId, ShopNetworkController.ShopInfoParam shopInfoParam) {
        return shopNetworkController.getShopInfo2(userid, deviceId, shopInfoParam);
    }
}
