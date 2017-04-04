package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryDataUseCase;
import com.tokopedia.seller.product.domain.model.CategoryGroupDomainModel;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerPresenterImpl implements CategoryPickerPresenter {
    private final FetchCategoryDataUseCase fetchCategoryDataUseCase;

    public CategoryPickerPresenterImpl(FetchCategoryDataUseCase fetchCategoryDataUseCase) {
        this.fetchCategoryDataUseCase = fetchCategoryDataUseCase;
    }

    @Override
    public void fetchCategoryData() {

        fetchCategoryDataUseCase.execute(RequestParams.EMPTY, new FetchCategoryDataSubscriber());

    }

    private class FetchCategoryDataSubscriber extends Subscriber<CategoryGroupDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(CategoryGroupDomainModel domainModel) {

        }
    }
}
