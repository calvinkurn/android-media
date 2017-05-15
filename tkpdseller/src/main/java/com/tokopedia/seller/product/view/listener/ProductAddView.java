package com.tokopedia.seller.product.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.view.model.categoryrecomm.ProductCategoryPredictionViewModel;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;

import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductAddView extends CustomerView {

    void onSuccessLoadScoringProduct(DataScoringProductView dataScoringProductView);

    void onErrorLoadScoringProduct(String errorMessage);

    void onSuccessLoadCatalog(List<Catalog> catalogViewModelList);

    void onErrorLoadCatalog(String errorMessage);

    void onSuccessLoadRecommendationCategory(List<ProductCategoryPredictionViewModel> categoryPredictionList);

    void onErrorLoadRecommendationCategory(String errorMessage);

    void onSuccessStoreProductToDraft(long productId);

    void onErrorStoreProductToDraft(String errorMessage);

    void onSuccessLoadShopInfo(boolean goldMerchant, boolean freeReturn);

    void onErrorLoadShopInfo(String errorMessage);

    void populateCategory(List<String> categorys);

    void onSuccessStoreProductAndAddToDraft(Long productId);

    void onErrorStoreProductAndAddToDraft(String errorMessage);
}