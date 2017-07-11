package com.tokopedia.seller.goldmerchant.statistic.data.repository;

import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public interface GMStatRepository {
    Observable<GetProductGraph> getProductGraph(long startDate, long endDate);
    Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate);
    Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate);
    Observable<GetKeyword> getKeywordModel(String categoryId);
    Observable<GetShopCategory> getShopCategory(long startDate, long endDate);
}
