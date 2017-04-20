package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;
import com.tokopedia.seller.product.domain.CatalogRepository;
import com.tokopedia.seller.product.domain.CategoryRecommRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class GetCategoryRecommUseCase extends UseCase<CategoryRecommDataModel>{

    public static final String TITLE = "title";
    public static final String ROW = "row";
    private final CategoryRecommRepository categoryRecommRepository;

    @Inject
    public GetCategoryRecommUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    CategoryRecommRepository categoryRecommRepository) {
        super(threadExecutor, postExecutionThread);
        this.categoryRecommRepository = categoryRecommRepository;
    }

    @Override
    public Observable<CategoryRecommDataModel> createObservable(RequestParams requestParams) {
        String title = requestParams.getString(TITLE, "");
        int row = requestParams.getInt(ROW, 0);
        return categoryRecommRepository.fetchCategoryRecomm(title, row);
    }

    public static RequestParams createRequestParams(String productTitle, int expectRow){
        RequestParams params = RequestParams.create();
        params.putString(TITLE, productTitle);
        params.putInt(ROW, expectRow);

        return params;
    }

}
