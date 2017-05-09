package com.tokopedia.discovery.intermediary.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.intermediary.domain.IntermediaryRepository;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

import rx.Observable;

/**
 * Created by alifa on 3/24/17.
 */

public class GetIntermediaryCategoryUseCase extends UseCase<IntermediaryCategoryDomainModel> {

    private final IntermediaryRepository repository;
    private String categoryId = "";

    public GetIntermediaryCategoryUseCase(ThreadExecutor threadExecutor,
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
    public Observable<IntermediaryCategoryDomainModel> createObservable(RequestParams requestParams) {
        return repository.getCategoryIntermediary(categoryId);
    }
}
