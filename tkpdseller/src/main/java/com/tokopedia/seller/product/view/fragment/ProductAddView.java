package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.Catalog;
import com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata.ProductCategoryPrediction;

import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductAddView extends CustomerView {
    void showCatalogError(Throwable e);
    void successFetchCatalogData(List<Catalog> catalogViewModelList, int maxRows);

    void showCatRecommError(Throwable e);
    void successGetCategoryRecommData(List<ProductCategoryPrediction> categoryPredictionList);

}
