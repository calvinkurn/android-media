package com.tokopedia.seller.product.category.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.category.data.source.db.model.CategoryDataBase;
import com.tokopedia.seller.product.category.domain.CategoryRepository;
import com.tokopedia.seller.product.category.domain.model.CategoryDomainModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/3/17.
 */
public class FetchCategoryWithParentChildUseCase extends BaseCategoryUseCase<List<CategoryDomainModel>> {

    public static final int UNSELECTED = -2;
    public static final String CATEGORY_PARENT = "CATEGORY_PARENT";

    @Inject
    public FetchCategoryWithParentChildUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CategoryRepository categoryRepository) {
        super(threadExecutor, postExecutionThread, categoryRepository);
    }

    @Override
    protected Observable<List<CategoryDomainModel>> createObservableCategory(RequestParams requestParams) {
        long categoryId = requestParams.getLong(CATEGORY_PARENT, UNSELECTED);
        return categoryRepository.fetchCategoryWithParent(categoryId);
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
}