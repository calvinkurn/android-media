package com.tokopedia.seller.product.domain.interactor.categorypicker;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.CategoryRepository;
import com.tokopedia.seller.product.domain.model.CategoryLevelDomainModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class FetchCategoryFromSelectedUseCase extends BaseCategoryUseCase<List<CategoryLevelDomainModel>> {

    public static final String INIT_SELECTED = "INIT_SELECTED";
    public static final int INIT_UNSELECTED = -1;

    @Inject
    public FetchCategoryFromSelectedUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread, categoryRepository);
    }

    @Override
    protected Observable<List<CategoryLevelDomainModel>> createObservableCategory(RequestParams requestParams) {
        long initSelected = requestParams.getLong(INIT_SELECTED, INIT_UNSELECTED);
        if (initSelected == INIT_UNSELECTED) {
            throw new RuntimeException("Init category is unselected");
        }
        return categoryRepository.fetchCategoryFromSelected(initSelected);
    }

    public static RequestParams generateParam(long initSelected) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(INIT_SELECTED, initSelected);
        return requestParam;
    }
}
