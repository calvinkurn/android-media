package com.tokopedia.seller.goldmerchant.statistic.data.source;

import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatCloud;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 5/18/17.
 */

public class GMStatDataSource {

    private GMStatCloud gmStatCloud;
    @Inject
    public GMStatDataSource(
            GMStatCloud gmStatCloud) {
        this.gmStatCloud = gmStatCloud;
    }

    public Observable<GetTransactionGraph> getTransactionGraph(long startDate, long endDate) {
        return gmStatCloud.getTransactionGraph(startDate, endDate)
                .map(new SimpleDataResponseMapper<GetTransactionGraph>());
    }

    public Observable<GetTransactionTable> getTransactionTable(long startDate, long endDate) {
        return gmStatCloud.getTransactionTable(startDate, endDate)
                .map(new SimpleDataResponseMapper<GetTransactionTable>());
    }

    public Observable<GetProductGraph> getProductGraph(long startDate, long endDate) {
        return gmStatCloud.getProductGraph(startDate, endDate)
                .map(new SimpleDataResponseMapper<GetProductGraph>());
    }

    public Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate) {
        return gmStatCloud.getPopularProduct(startDate, endDate)
                .map(new SimpleDataResponseMapper<GetPopularProduct>());
    }

    public Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate) {
        return gmStatCloud.getBuyerGraph(startDate, endDate)
                .map(new SimpleDataResponseMapper<GetBuyerData>());
    }

    public Observable<GetKeyword> getKeywordModel(String categoryId) {
        return gmStatCloud.getKeywordModel(categoryId)
                .map(new SimpleDataResponseMapper<GetKeyword>());
    }

    public Observable<GetShopCategory> getShopCategory(long startDate, long endDate) {
        return gmStatCloud.getShopCategory(startDate, endDate)
                .map(new SimpleDataResponseMapper<GetShopCategory>());
    }
}
