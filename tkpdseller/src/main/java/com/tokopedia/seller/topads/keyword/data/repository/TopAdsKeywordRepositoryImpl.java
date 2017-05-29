package com.tokopedia.seller.topads.keyword.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.ShopInfoRepository;
import com.tokopedia.seller.product.domain.model.AddProductShopInfoDomainModel;
import com.tokopedia.seller.topads.keyword.data.source.KeywordDashboardDataSouce;
import com.tokopedia.seller.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.seller.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.seller.topads.keyword.domain.model.KeywordDashboardDomain;
import com.tokopedia.seller.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class TopAdsKeywordRepositoryImpl implements TopAdsKeywordRepository {

    private KeywordDashboardDataSouce keywordDashboardDataSouce;
    private ShopInfoRepository shopInfoRepository;

    @Inject
    public TopAdsKeywordRepositoryImpl(KeywordDashboardDataSouce keywordDashboardDataSouce, ShopInfoRepository shopInfoRepository) {
        this.keywordDashboardDataSouce = keywordDashboardDataSouce;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<KeywordDashboardDomain> getDashboardKeyword(final RequestParams requestParams) {
        return shopInfoRepository.getAddProductShopInfo().flatMap(new Func1<AddProductShopInfoDomainModel, Observable<KeywordDashboardDomain>>() {
            @Override
            public Observable<KeywordDashboardDomain> call(AddProductShopInfoDomainModel addProductShopInfoDomainModel) {
                requestParams.putString("shop_id", addProductShopInfoDomainModel.getShopId());
                return keywordDashboardDataSouce.getKeywordDashboard(requestParams);
            }
        });
    }

    @Override
    public Observable<EditTopAdsKeywordDetailDomainModel> editTopAdsKeywordDetail(TopAdsKeywordEditDetailInputDomainModel modelInput) {
        return keywordDashboardDataSouce.editTopAdsKeywordDetail(modelInput);
    }
}
