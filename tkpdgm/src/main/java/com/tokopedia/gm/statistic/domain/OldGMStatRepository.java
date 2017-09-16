package com.tokopedia.gm.statistic.domain;

import com.tkpd.library.utils.network.CommonListener;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;

import java.util.List;

/**
 * Created by normansyahputa on 7/20/17.
 */

public interface OldGMStatRepository extends CommonListener {
    void onSuccessGetShopCategory(GetShopCategory getShopCategory);

    void onSuccessTransactionGraph(GMTransactionGraphMergeModel getTransactionGraph);

    void onSuccessProductGraph(GetProductGraph getTransactionGraph);

    void onSuccessPopularProduct(GetPopularProduct getPopularProduct);

    void onSuccessBuyerGraph(GetBuyerGraph getBuyerGraph);

    void onSuccessGetKeyword(List<GetKeyword> getKeywords);

    void onSuccessGetCategory(List<String> categoryNameList);

    void onComplete();
}
