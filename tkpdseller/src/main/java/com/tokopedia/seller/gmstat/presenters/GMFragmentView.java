package com.tokopedia.seller.gmstat.presenters;

import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.gmstat.models.GetTransactionGraph;

import java.util.List;

/**
 * Created by normansyahputa on 1/2/17.
 */

public interface GMFragmentView {
    void onSuccessGetShopCategory(GetShopCategory getShopCategory);
    void onSuccessTransactionGraph(GetTransactionGraph getTransactionGraph, long sDate, long eDate, int lastSelectionPeriod, int selectionType);
    void onSuccessProductnGraph(GetProductGraph getProductGraph, boolean isFirstTime);
    void onSuccessPopularProduct(GetPopularProduct getPopularProduct);
    void onSuccessBuyerData(GetBuyerData getBuyerData);
    void onSuccessGetKeyword(List<GetKeyword> getKeywords);
    void onSuccessGetCategory(List<HadesV1Model> hadesV1Models);
    void onComplete();
    void onError(Throwable e);
    void onFailure();
    void fetchData();
    void fetchData(long sDate, long eDate, int lastSelectionPeriod, int selectionType);
    void resetToLoading();
    void bindHeader(long sDate, long eDate, int lastSelectionPeriod, int selectionType);
}
