package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;
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
public class FetchCategoryChildUseCase extends UseCase<List<CategoryDomainModel>> {

    private static final int UNSELECTED = -2;
    public static final String CATEGORY_PARENT = "CATEGORY_PARENT";
    private final CategoryRepository categoryRepository;

    @Inject
    public FetchCategoryChildUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CategoryRepository categoryRepository) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Observable<List<CategoryDomainModel>> createObservable(RequestParams requestParams) {
        long categoryId = requestParams.getLong(CATEGORY_PARENT, UNSELECTED);
        return Observable.just(categoryId).flatMap(new FetchCategoryLevelOne());
    }

    public static RequestParams generateLevelOne() {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(CATEGORY_PARENT, CategoryDataBase.LEVEL_ONE_PARENT);
        return requestParam;
    }

    public static RequestParams generateFromParent(long categoryId) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(CATEGORY_PARENT, categoryId);
        return requestParam;
    }

    private class FetchCategoryLevelOne implements Func1<Long, Observable<List<CategoryDomainModel>>> {
        @Override
        public Observable<List<CategoryDomainModel>> call(Long categoryId) {
            return categoryRepository.fetchCategoryLevelOne(categoryId);
        }
    }
}