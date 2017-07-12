package com.tokopedia.seller.goldmerchant.statistic.data.repository;

import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public interface GMStatRepository {
    Observable<GetTransactionGraph> getTransactionGraph(long startDate, long endDate);

    Observable<GetTransactionTable> getTransactionTable(long startDate, long endDate);

    Observable<GetProductGraph> getProductGraph(long startDate, long endDate);

    Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate);

    Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate);

    Observable<GetKeyword> getKeywordModel(String categoryId);

    Observable<GetShopCategory> getShopCategory(long startDate, long endDate);
}
