package com.tokopedia.gm.statistic.domain.model.empty;

import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.gm.statistic.domain.KeywordModel;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;

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
