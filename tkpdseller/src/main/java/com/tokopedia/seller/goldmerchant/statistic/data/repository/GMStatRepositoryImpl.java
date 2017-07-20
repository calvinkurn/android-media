package com.tokopedia.seller.goldmerchant.statistic.data.repository;

import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.data.source.GMStatDataSource;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetBuyerTable;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetProductTable;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionStatDomainMapper;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionTableMapper;
import com.tokopedia.seller.goldmerchant.statistic.domain.model.transaction.table.GetTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.product.domain.ShopInfoRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatRepositoryImpl implements GMStatRepository {

    private GMStatDataSource gmStatDataSource;
    private GMTransactionStatDomainMapper gmTransactionStatDomainMapper;
    private GMTransactionTableMapper gmTransactionTableMapper;

    @Inject
    public GMStatRepositoryImpl(GMTransactionStatDomainMapper gmTransactionStatDomainMapper,
                                GMStatDataSource gmStatDataSource,
                                GMTransactionTableMapper gmTransactionTableMapper) {
        this.gmStatDataSource = gmStatDataSource;
        this.gmTransactionStatDomainMapper = gmTransactionStatDomainMapper;
        this.gmTransactionTableMapper = gmTransactionTableMapper;
    }

    @Override
    public Observable<GMTransactionGraphMergeModel> getTransactionGraph(long startDate, long endDate) {
        return gmStatDataSource.getTransactionGraph(startDate, endDate).map(gmTransactionStatDomainMapper);
    }

    public GMTransactionGraphMergeModel getTransactionGraph(GetTransactionGraph getTransactionGraph) {
        return Observable.just(getTransactionGraph)
                .map(gmTransactionStatDomainMapper).toBlocking().first();
    }

    @Override
    public Observable<GetTransactionTableModel> getTransactionTable(long startDate, long endDate,
                                                                    int page, int pageSize,
                                                                    @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy int sortBy) {
        return gmStatDataSource.getTransactionTable(startDate, endDate, page, pageSize,
                sortType, sortBy).map(gmTransactionTableMapper);
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
    public Observable<GetBuyerGraph> getBuyerGraph(long startDate, long endDate) {
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

    @Override
    public Observable<GetProductTable> getProductTable(long startDate, long endDate) {
        return gmStatDataSource.getProductTable(startDate, endDate);
    }

    @Override
    public Observable<GetBuyerTable> getBuyerTable(long startDate, long endDate) {
        return gmStatDataSource.getBuyerTable(startDate, endDate);
    }


}
