package com.tokopedia.seller.product.edit.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.edit.domain.CatalogRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class FetchCatalogDataUseCase extends UseCase<CatalogDataModel>{

    private final CatalogRepository catalogRepository;

    @Inject
    public FetchCatalogDataUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   CatalogRepository catalogRepository) {
        super(threadExecutor, postExecutionThread);
        this.catalogRepository = catalogRepository;
    }

    public static RequestParams createRequestParams(String keyword, long departementId, int start, int rows){
        RequestParams params = RequestParams.create();
        params.putString("q", keyword);
        params.putLong("sc", departementId);
        params.putInt("start", start);
        params.putInt("rows", rows);

        return params;
    }

    @Override
    public Observable<CatalogDataModel> createObservable(RequestParams requestParams) {
        String q = requestParams.getString("q", "");
        long sc = requestParams.getLong("sc", 0);
        int start = requestParams.getInt("start", 0);
        int rows = requestParams.getInt("rows", 20);
        return catalogRepository.fetchCatalog(q, sc, start, rows);
    }

}
