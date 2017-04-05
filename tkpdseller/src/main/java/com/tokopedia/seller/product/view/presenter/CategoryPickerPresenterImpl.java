package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryDataUseCase;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;
import com.tokopedia.seller.product.view.mapper.CategoryViewMapper;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerPresenterImpl extends CategoryPickerPresenter {
    private final FetchCategoryDataUseCase fetchCategoryDataUseCase;

    public CategoryPickerPresenterImpl(FetchCategoryDataUseCase fetchCategoryDataUseCase) {
        this.fetchCategoryDataUseCase = fetchCategoryDataUseCase;
    }

    @Override
    public void fetchCategoryData() {
        checkViewAttached();
        getView().showLoadingDialog();
        fetchCategoryDataUseCase.execute(RequestParams.EMPTY, new FetchCategoryDataSubscriber());

    }

    private class FetchCategoryDataSubscriber extends Subscriber<List<CategoryDomainModel>> {
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
