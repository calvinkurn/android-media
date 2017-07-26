package com.tokopedia.seller.goldmerchant.statistic.domain.model.empty;

import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

/**
 * Created by normansyahputa on 7/26/17.
 */

public class GMEmptyModel {
    public GetProductGraph productGraph;
    public GMTransactionGraphMergeModel transactionGraph;
    public GetPopularProduct popularProduct;
    public GetBuyerGraph buyerGraph;
    public KeywordModel keywordModel;
}
