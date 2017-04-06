package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryLevelOneUseCase;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;
import com.tokopedia.seller.product.view.mapper.CategoryViewMapper;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerPresenterImpl extends CategoryPickerPresenter {
    private final FetchCategoryLevelOneUseCase fetchCategoryLevelOneUseCase;

    public CategoryPickerPresenterImpl(FetchCategoryLevelOneUseCase fetchCategoryLevelOneUseCase) {
        this.fetchCategoryLevelOneUseCase = fetchCategoryLevelOneUseCase;
    }

    @Override
    public void fetchCategoryLevelOne() {
        checkViewAttached();
        getView().showLoadingDialog();
        fetchCategoryLevelOneUseCase.execute(RequestParams.EMPTY, new FetchCategoryLevelOneSubscriber());

    }

    private class FetchCategoryLevelOneSubscriber extends Subscriber<List<CategoryDomainModel>> {
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
