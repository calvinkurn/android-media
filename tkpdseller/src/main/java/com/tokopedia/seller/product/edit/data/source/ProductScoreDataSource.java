package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.seller.product.edit.data.source.cache.ProductScoreDataSourceCache;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoreDataSource {

    private final ProductScoreDataSourceCache productScoreDataSourceCache;

    public ProductScoreDataSource(ProductScoreDataSourceCache productScoreDataSourceCache) {
        this.productScoreDataSourceCache = productScoreDataSourceCache;
    }

    public Observable<DataScoringProductView> getValidationScore(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        return productScoreDataSourceCache.getValidationScore(valueIndicatorScoreModel);
    }
}
