package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.domain.CatalogRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/3/17.
 */
@ActivityScope
public class FetchCatalogDataUseCase extends UseCase<CatalogDataModel>{

    private final CatalogRepository catalogRepository;

    @Inject
    public FetchCatalogDataUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   CatalogRepository catalogRepository) {
        super(threadExecutor, postExecutionThread);
        this.catalogRepository = catalogRepository;
    }

    @Override
    public Observable<CatalogDataModel> createObservable(RequestParams requestParams) {
        String q = requestParams.getString("q", "");
        int sc = requestParams.getInt("sc", 0);
        int start = requestParams.getInt("start", 0);
        int rows = requestParams.getInt("rows", 20);
        return catalogRepository.fetchCatalog(q, sc, start, rows);
    }

    public static RequestParams createRequestParams(String keyword,
                                                    int departementId,
                                                    int start,
                                                    int rows){
        RequestParams params = RequestParams.create();
        params.putString("q", keyword);
        params.putInt("sc", departementId);
        params.putInt("start", start);
        params.putInt("rows", rows);

        return params;
    }

}
