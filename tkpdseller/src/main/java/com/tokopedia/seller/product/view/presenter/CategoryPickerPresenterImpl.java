package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.FetchAllCategoryDataUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryChildUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryFromSelectedUseCase;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;
import com.tokopedia.seller.product.domain.model.CategoryLevelDomainModel;
import com.tokopedia.seller.product.view.mapper.CategoryViewMapper;
import com.tokopedia.seller.product.view.model.CategoryLevelViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerPresenterImpl extends CategoryPickerPresenter {
    private final FetchCategoryChildUseCase fetchCategoryChildUseCase;
    private final FetchAllCategoryDataUseCase fetchAllCategoryDataUseCase;
    private final FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase;

    public CategoryPickerPresenterImpl(
            FetchCategoryChildUseCase fetchCategoryChildUseCase,
            FetchAllCategoryDataUseCase fetchAllCategoryDataUseCase, FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase) {
        this.fetchCategoryChildUseCase = fetchCategoryChildUseCase;
        this.fetchAllCategoryDataUseCase = fetchAllCategoryDataUseCase;
        this.fetchCategoryFromSelectedUseCase = fetchCategoryFromSelectedUseCase;
    }

    @Override
    public void fetchCategoryLevelOne() {
        checkViewAttached();
        RequestParams requestParam = FetchCategoryChildUseCase.generateLevelOne();
        fetchCategoryChildUseCase.execute(requestParam, new FetchCategoryChildSubscriber());
    }

    @Override
    public void fetchCategoryChild(long categoryId) {
        checkViewAttached();
        RequestParams requestParam = FetchCategoryChildUseCase.generateFromParent(categoryId);
        fetchCategoryChildUseCase.execute(requestParam, new FetchCategoryChildSubscriber());
    }

    @Override
    public void fetchAllCategoryData() {
        checkViewAttached();
        getView().showLoadingDialog();
        fetchAllCategoryDataUseCase.execute(RequestParams.EMPTY, new FetchAllCategoryDataSubscriber());
    }

    @Override
    public void fetchCategoryFromSelected(long initSelected) {
        checkViewAttached();
        RequestParams requestParam = FetchCategoryFromSelectedUseCase.generateParam(initSelected);
        fetchCategoryFromSelectedUseCase.execute(requestParam, new FetchCategoryFromSelectedSubscriber());
    }

    @Override
    public void unsubscribe() {
        fetchCategoryChildUseCase.unsubscribe();
        fetchAllCategoryDataUseCase.unsubscribe();
        fetchCategoryFromSelectedUseCase.unsubscribe();
    }

    private class FetchCategoryChildSubscriber extends Subscriber<List<CategoryDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
        }

        @Override
        public void onNext(List<CategoryDomainModel> domainModel) {
            checkViewAttached();
            getView().renderCategory(CategoryViewMapper.mapList(domainModel));

        }
    }

    private class FetchAllCategoryDataSubscriber extends Subscriber<Boolean> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().dismissLoadingDialog();
        }

        @Override
        public void onNext(Boolean aBoolean) {
            checkViewAttached();
            getView().dismissLoadingDialog();
            getView().onSuccessFetchAllCategoryData();
        }
    }

    private class FetchCategoryFromSelectedSubscriber extends Subscriber<List<CategoryLevelDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
        }

        @Override
        public void onNext(List<CategoryLevelDomainModel> categoryLevelDomainModels) {
            checkViewAttached();
            List<CategoryLevelViewModel> categoryLevelViewModels = CategoryViewMapper.mapLevel(categoryLevelDomainModels);
            getView().renderCategoryFromSelected(categoryLevelViewModels);
        }
    }
}
