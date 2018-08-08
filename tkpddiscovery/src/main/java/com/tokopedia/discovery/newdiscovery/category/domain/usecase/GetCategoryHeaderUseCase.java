package com.tokopedia.discovery.newdiscovery.category.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.newdiscovery.category.domain.CategoryRepository;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;

import rx.Observable;

/**
 * @author by alifa on 10/30/17.
 */

public class GetCategoryHeaderUseCase extends UseCase<CategoryHeaderModel> {

    private final CategoryRepository repository;
    private String categoryId;

    public GetCategoryHeaderUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    CategoryRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<CategoryHeaderModel> createObservable(RequestParams requestParams) {
        return repository.getCategoryHeader(categoryId);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
