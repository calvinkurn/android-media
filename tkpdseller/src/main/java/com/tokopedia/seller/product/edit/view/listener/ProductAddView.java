package com.tokopedia.seller.product.edit.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.edit.view.model.categoryrecomm.ProductCategoryPredictionViewModel;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;

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

    void onSuccessStoreProductToDraft(long productId, boolean isUploading);

    void onErrorStoreProductToDraftWhenUpload(String errorMessage);

    void onErrorStoreProductToDraftWhenBackPressed(String errorMessage);

    void onSuccessLoadShopInfo(boolean goldMerchant, boolean freeReturn, boolean officialStore);

    void onErrorLoadShopInfo(String errorMessage);

    void populateCategory(List<String> categorys);

    void onSuccessStoreProductAndAddToDraft(Long productId);

    void onErrorStoreProductAndAddToDraft(String errorMessage);

    long getProductDraftId();

    void onSuccessGetProductVariant(List<ProductVariantByCatModel> productVariantByCatModelList);

    void onErrorGetProductVariantByCat(Throwable throwable);
}