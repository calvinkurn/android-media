package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.di.scope.CategoryPickerViewScope;
import com.tokopedia.seller.product.domain.CategoryRepository;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerViewScope
public class FetchCategoryLevelOneUseCase extends UseCase<List<CategoryDomainModel>>{

    private final CategoryRepository categoryRepository;

    @Inject
    public FetchCategoryLevelOneUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CategoryRepository categoryRepository) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Observable<List<CategoryDomainModel>> createObservable(RequestParams requestParams) {
        return Observable.just(true)
                .flatMap(new CheckVersion())
                .flatMap(new CheckCategoryAvailable())
                .flatMap(new FetchCategoryLevelOne());
    }

    private class CheckVersion implements Func1<Boolean, Observable<Boolean>> {
        @Override
        public Observable<Boolean> call(Boolean aBoolean) {
            return categoryRepository.checkVersion();
        }
    }

    private class CheckCategoryAvailable implements Func1<Boolean, Observable<Boolean>> {
        @Override
        public Observable<Boolean> call(Boolean aBoolean) {
            return categoryRepository.checkCategoryAvailable();
        }
    }

    private class FetchCategoryLevelOne implements Func1<Boolean, Observable<List<CategoryDomainModel>>> {
        @Override
        public Observable<List<CategoryDomainModel>> call(Boolean aBoolean) {
            return categoryRepository.fetchCategoryLevelOne();
        }
    }
}
