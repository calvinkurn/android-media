package com.tokopedia.seller.goldmerchant.statistic.domain;

import com.tkpd.library.utils.network.CommonListener;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.util.List;

/**
 * Created by normansyahputa on 7/20/17.
 */

public interface OldGMStatRepository extends CommonListener {
    void onSuccessGetShopCategory(GetShopCategory getShopCategory);

    void onSuccessTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph);

    void onSuccessProductnGraph(GetProductGraph getTransactionGraph);

    void onSuccessPopularProduct(GetPopularProduct getPopularProduct);

    void onSuccessBuyerGraph(GetBuyerGraph getBuyerGraph);

    void onSuccessGetKeyword(List<GetKeyword> getKeywords);

    void onSuccessGetCategory(List<HadesV1Model> hadesV1Models);

    void onComplete();
}
