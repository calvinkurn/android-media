package com.tokopedia.seller.goldmerchant.statistic.domain;

import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.seller.goldmerchant.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.seller.goldmerchant.statistic.domain.model.transaction.table.GetTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public interface GMStatRepository {
    Observable<GMTransactionGraphMergeModel> getTransactionGraph(long startDate, long endDate);

    Observable<GetTransactionTableModel> getTransactionTable(long startDate, long endDate,
                                                             int page, int pageSize,
                                                             @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy int sortBy);

    Observable<GetProductGraph> getProductGraph(long startDate, long endDate);

    Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate);

    Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate);

    Observable<GetKeyword> getKeywordModel(String categoryId);

    Observable<GetShopCategory> getShopCategory(long startDate, long endDate);

    Observable<Boolean> clearCache();
}
