package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.di.scope.CategoryPickerViewScope;
import com.tokopedia.seller.product.domain.CategoryRepository;
import com.tokopedia.seller.product.domain.model.CategoryGroupDomainModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerViewScope
public class FetchCategoryDataUseCase extends UseCase<List<CategoryGroupDomainModel>>{

    private final CategoryRepository categoryRepository;

    @Inject
    public FetchCategoryDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CategoryRepository categoryRepository) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Observable<List<CategoryGroupDomainModel>> createObservable(RequestParams requestParams) {
        categoryRepository.checkVersion();
        return categoryRepository.fetchCategory();
    }
}
