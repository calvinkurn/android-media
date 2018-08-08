package com.tokopedia.seller.product.edit.data.repository;

import com.tokopedia.seller.product.edit.data.source.ProductScoreDataSource;
import com.tokopedia.seller.product.edit.domain.ProductScoreRepository;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;

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
