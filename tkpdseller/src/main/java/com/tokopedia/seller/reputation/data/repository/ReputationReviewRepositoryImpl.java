package com.tokopedia.seller.reputation.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.reputation.data.source.cloud.CloudReputationReviewDataSource;
import com.tokopedia.seller.reputation.domain.ReputationReviewRepository;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.util.ShopNetworkController;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 3/16/17.
 */

public class ReputationReviewRepositoryImpl implements ReputationReviewRepository {
    private CloudReputationReviewDataSource cloudReputationReviewDataSource;
    private ShopInfoRepository shopInfoRepository;
    private ShopNetworkController shopNetworkController;

    @Deprecated
    public ReputationReviewRepositoryImpl(
            CloudReputationReviewDataSource cloudReputationReviewDataSource,
            ShopNetworkController shopNetworkController) {
        this.cloudReputationReviewDataSource = cloudReputationReviewDataSource;
        this.shopNetworkController = shopNetworkController;
    }

    @Inject
    public ReputationReviewRepositoryImpl(CloudReputationReviewDataSource cloudReputationReviewDataSource, ShopInfoRepositoryImpl shopInfoRepository) {
        this.cloudReputationReviewDataSource = cloudReputationReviewDataSource;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param) {
        return cloudReputationReviewDataSource.getReputationHistory(shopId, param);
    }

    @Override
    public Observable<ShopModel> getShopInfo(String userid, String deviceId, ShopNetworkController.ShopInfoParam shopInfoParam) {
        return shopInfoRepository.getShopInfo();
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams) {
        return cloudReputationReviewDataSource.getReputationHistory(requestParams);
    }

    @Override
    public Observable<ShopModel> getShopInfo(RequestParams requestParams) {
        return shopInfoRepository.getShopInfo();
    }
}
