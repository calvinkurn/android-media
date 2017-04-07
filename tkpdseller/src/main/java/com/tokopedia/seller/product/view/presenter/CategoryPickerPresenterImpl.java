package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryFromParentUseCase;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;
import com.tokopedia.seller.product.view.mapper.CategoryViewMapper;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerPresenterImpl extends CategoryPickerPresenter {
    private final FetchCategoryFromParentUseCase fetchCategoryFromParentUseCase;

    public CategoryPickerPresenterImpl(FetchCategoryFromParentUseCase fetchCategoryFromParentUseCase) {
        this.fetchCategoryFromParentUseCase = fetchCategoryFromParentUseCase;
    }

    @Override
    public void fetchCategoryLevelOne() {
        checkViewAttached();
        getView().showLoadingDialog();
        RequestParams requestParam = FetchCategoryFromParentUseCase.generateLevelOne();
        fetchCategoryFromParentUseCase.execute(requestParam, new FetchCategoryFromParentSubscriber());

    }

    @Override
    public void fetchCategoryWithParent(int categoryId) {
        checkViewAttached();
        getView().showLoadingDialog();
        RequestParams requestParam = FetchCategoryFromParentUseCase.generateFromParent(categoryId);
        fetchCategoryFromParentUseCase.execute(requestParam, new FetchCategoryFromParentSubscriber());
    }

    private class FetchCategoryFromParentSubscriber extends Subscriber<List<CategoryDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().dismissLoadingDialog();
        }

        @Override
        public void onNext(List<CategoryDomainModel> domainModel) {
            checkViewAttached();
            getView().dismissLoadingDialog();
            getView().renderCategory(CategoryViewMapper.map(domainModel));

        }
    }
}
