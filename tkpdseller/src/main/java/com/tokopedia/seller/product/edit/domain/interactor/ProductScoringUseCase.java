package com.tokopedia.seller.product.edit.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.edit.domain.ProductScoreRepository;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

public class ProductScoringUseCase extends UseCase<DataScoringProductView> {
    private static final String VALUE_INDICATOR_SCORE_MODEL = "value_indicator_score_model";

    private ProductScoreRepository productScoreRepository;

    public ProductScoringUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                 ProductScoreRepository productScoreRepository) {
        super(threadExecutor, postExecutionThread);
        this.productScoreRepository = productScoreRepository;
    }

    @Override
    public Observable<DataScoringProductView> createObservable(RequestParams requestParams) {
        return productScoreRepository.getValidationScore((ValueIndicatorScoreModel)requestParams.getObject(VALUE_INDICATOR_SCORE_MODEL));
    }

    public static RequestParams createRequestParams(ValueIndicatorScoreModel valueIndicatorScoreModel){
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(VALUE_INDICATOR_SCORE_MODEL, valueIndicatorScoreModel);
        return requestParams;
    }
}
