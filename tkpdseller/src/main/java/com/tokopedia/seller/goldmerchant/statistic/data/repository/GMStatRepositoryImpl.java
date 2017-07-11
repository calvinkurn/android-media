package com.tokopedia.seller.goldmerchant.statistic.data.repository;

import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.GMStatDataSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatRepositoryImpl implements GMStatRepository {

    private GMStatDataSource gMStatDataSource;

    @Inject
    public GMStatRepositoryImpl(GMStatDataSource gMStatDataSource) {
        this.gMStatDataSource = gMStatDataSource;
    }

    @Override
    public Observable<GetProductGraph> getProductGraph(long startDate, long endDate) {
        return gMStatDataSource.getProductGraph(startDate, endDate);
    }

    @Override
    public Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate) {
        return gMStatDataSource.getPopularProduct(startDate, endDate);
    }

    @Override
    public Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate) {
        return gMStatDataSource.getBuyerGraph(startDate, endDate);
    }

    @Override
    public Observable<GetKeyword> getKeywordModel(String categoryId) {
        return gMStatDataSource.getKeywordModel(categoryId);
    }

    @Override
    public Observable<GetShopCategory> getShopCategory(long startDate, long endDate) {
        return gMStatDataSource.getShopCategory(startDate, endDate);
    }

}
