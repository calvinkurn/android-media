package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
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

public class FetchCategoryFromSelectedUseCase extends UseCase<List<CategoryLevelDomainModel>>{

    public static final String INIT_SELECTED = "INIT_SELECTED";
    public static final int INIT_UNSELECTED = -1;
    private final CategoryRepository categoryRepository;

    @Inject
    public FetchCategoryFromSelectedUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CategoryRepository categoryRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Observable<List<CategoryLevelDomainModel>> createObservable(RequestParams requestParams) {
        int initSelected = requestParams.getInt(INIT_SELECTED, INIT_UNSELECTED);
        if (initSelected == INIT_UNSELECTED){
            throw new RuntimeException("Init category is unselected");
        }
        return categoryRepository.fetchCategoryFromSelected(initSelected);
    }

    public static RequestParams generateParam(int initSelected) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putInt(INIT_SELECTED, initSelected);
        return requestParam;
    }
}
