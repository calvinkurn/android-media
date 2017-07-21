package com.tokopedia.seller.gmstat.presenters;

import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.util.List;

/**
 * Created by normansyahputa on 1/2/17.
 */

public interface GMFragmentView {
    void onGetShopCategoryEmpty();

    void onSuccessTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph, long sDate, long eDate, int lastSelectionPeriod, int selectionType);

    void onSuccessProductnGraph(GetProductGraph getProductGraph, boolean isFirstTime);

    void onSuccessPopularProduct(GetPopularProduct getPopularProduct);

    void onSuccessGetKeyword(List<GetKeyword> getKeywords);

    void onSuccessGetCategory(String categoryName);

    void onComplete();

    void onError(Throwable e);

    void onFailure();

    void fetchData();

    void fetchData(long sDate, long eDate, int lastSelectionPeriod, int selectionType);

    void resetToLoading();

    void bindHeader(long sDate, long eDate, int lastSelectionPeriod, int selectionType);

    void onLoadGMStatTracking();

    void onScrollGMStatTracking();

    void onSuccessBuyerGraph(GetBuyerGraph getBuyerGraph);
}
