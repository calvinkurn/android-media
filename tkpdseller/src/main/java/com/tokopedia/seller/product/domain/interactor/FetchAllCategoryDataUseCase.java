package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.CategoryRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class FetchAllCategoryDataUseCase extends UseCase<Boolean>{
    private final CategoryRepository categoryRepository;

    @Inject
    public FetchAllCategoryDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CategoryRepository categoryRepository) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return Observable.just(true)
                .flatMap(new CheckVersion())
                .flatMap(new CheckCategoryAvailable());
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
}
