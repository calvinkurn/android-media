package com.tokopedia.seller.goldmerchant.statistic.data.repository;

import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.GMStatDataSource;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatRepositoryImpl implements GMStatRepository {

    private GMStatDataSource gmStatDataSource;

    @Inject
    public GMStatRepositoryImpl(GMStatDataSource gmStatDataSource) {
        this.gmStatDataSource = gmStatDataSource;
    }

    @Override
    public Observable<GetTransactionGraph> getTransactionGraph(long startDate, long endDate) {
        return gmStatDataSource.getTransactionGraph(startDate, endDate);
    }

    @Override
    public Observable<GetTransactionTable> getTransactionTable(long startDate, long endDate) {
        return gmStatDataSource.getTransactionTable(startDate, endDate);
    }

    @Override
    public Observable<GetProductGraph> getProductGraph(long startDate, long endDate) {
        return gmStatDataSource.getProductGraph(startDate, endDate);
    }

    @Override
    public Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate) {
        return gmStatDataSource.getPopularProduct(startDate, endDate);
    }

    @Override
    public Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate) {
        return gmStatDataSource.getBuyerGraph(startDate, endDate);
    }

    @Override
    public Observable<GetKeyword> getKeywordModel(String categoryId) {
        return gmStatDataSource.getKeywordModel(categoryId);
    }

    @Override
    public Observable<GetShopCategory> getShopCategory(long startDate, long endDate) {
        return gmStatDataSource.getShopCategory(startDate, endDate);
    }

    @Override
    public Observable<Boolean> clearCache() {
        return gmStatDataSource.clearAllCache();
    }


}
