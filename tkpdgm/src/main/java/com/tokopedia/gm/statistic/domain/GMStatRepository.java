package com.tokopedia.gm.statistic.domain;

import com.tokopedia.gm.statistic.constant.GMTransactionTableSortBy;
import com.tokopedia.gm.statistic.constant.GMTransactionTableSortType;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetBuyerTable;
import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetProductTable;
import com.tokopedia.gm.statistic.domain.model.transaction.table.GetTransactionTableModel;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public interface GMStatRepository {
    Observable<GMTransactionGraphMergeModel> getTransactionGraph(long startDate, long endDate);

    GMTransactionGraphMergeModel getTransactionGraph(GetTransactionGraph getTransactionGraph);

    Observable<GetTransactionTableModel> getTransactionTable(long startDate, long endDate,
                                                             int page, int pageSize,
                                                             @GMTransactionTableSortType int sortType, @GMTransactionTableSortBy int sortBy);

    Observable<GetProductGraph> getProductGraph(long startDate, long endDate);

    Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate);

    Observable<GetBuyerGraph> getBuyerGraph(long startDate, long endDate);

    Observable<GetKeyword> getKeywordModel(String categoryId);

    Observable<GetShopCategory> getShopCategory(long startDate, long endDate);

    Observable<Boolean> clearCache();

    Observable<GetProductTable> getProductTable(long startDate, long endDate);

    Observable<GetBuyerTable> getBuyerTable(long startDate, long endDate);
}
