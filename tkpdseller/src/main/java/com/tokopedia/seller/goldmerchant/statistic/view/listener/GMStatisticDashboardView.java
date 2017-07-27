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

    void onTransactionGraphLoaded(GMTransactionGraphMergeModel getTransactionGraph);

    void onSuccessProductnGraph(GetProductGraph getProductGraph);

    void onSuccessBuyerGraph(GetBuyerGraph getBuyerGraph);

    void onGetShopCategoryEmpty();

    void onSuccessGetCategory(String categoryName);

    void onSuccessGetKeyword(List<GetKeyword> getKeywords);

    void onSuccessPopularProduct(GetPopularProduct getPopularProduct);

    void onError(Throwable e);

    void showSnackbarRetry();
}
