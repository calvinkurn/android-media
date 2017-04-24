package com.tokopedia.seller.product.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata.ProductCategoryPrediction;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductAddView extends CustomerView {
    void onSuccessGetScoringProduct(DataScoringProductView dataScoringProductView);

    void updateProductScoring();

    void showCatalogError(Throwable e);

    void successFetchCatalogData(List<Catalog> catalogViewModelList, int maxRows);

    void showCatRecommError(Throwable e);

    void successGetCategoryRecommData(List<ProductCategoryPrediction> categoryPredictionList);

    void onSuccessStoreProductToDraft(long productId);

    void onSuccessGetShopInfo(boolean goldMerchant, boolean freeReturn);

    void showErrorGetShopInfo(Throwable e);
}