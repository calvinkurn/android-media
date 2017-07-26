package com.tokopedia.seller.goldmerchant.statistic.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.util.List;

/**
 * Created by normansyahputa on 1/2/17.
 */

public interface GMStatisticDashboardView extends CustomerView {
    void onGetShopCategoryEmpty();

    void onSuccessTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph);

    void onSuccessProductnGraph(GetProductGraph getProductGraph);

    void onSuccessPopularProduct(GetPopularProduct getPopularProduct);

    void onSuccessGetKeyword(List<GetKeyword> getKeywords);

    void onSuccessGetCategory(String categoryName);

    void onSuccessBuyerGraph(GetBuyerGraph getBuyerGraph);

    void onComplete();

    void onError(Throwable e);

    void onLoadGMStatTracking();

    void onScrollGMStatTracking();
}
