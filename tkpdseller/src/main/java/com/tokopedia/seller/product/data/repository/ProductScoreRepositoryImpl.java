package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.source.ProductScoreDataSource;
import com.tokopedia.seller.product.domain.ProductScoreRepository;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoreRepositoryImpl implements ProductScoreRepository {

    private final ProductScoreDataSource productScoreDataSource;

    public ProductScoreRepositoryImpl(ProductScoreDataSource productScoreDataSource) {
        this.productScoreDataSource = productScoreDataSource;
    }

    @Override
    public Observable<DataScoringProductView> getValidationScore(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        return productScoreDataSource.getValidationScore(valueIndicatorScoreModel);
    }
}
