package com.tokopedia.discovery.intermediary.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.discovery.intermediary.domain.IntermediaryRepository;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by alifa on 11/6/17.
 */

public class GetCategoryHeaderUseCase extends UseCase<Response<CategoryHadesModel>> {

    private final IntermediaryRepository repository;
    private String categoryId = "";

    public GetCategoryHeaderUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          IntermediaryRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public Observable<Response<CategoryHadesModel>> createObservable(RequestParams requestParams) {
        return repository.getCategoryHeader(categoryId);
    }
}
