package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.categorypicker.FetchCategoryUseCaseChildUseCase;
import com.tokopedia.seller.product.domain.interactor.categorypicker.FetchCategoryFromSelectedUseCase;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;
import com.tokopedia.seller.product.domain.model.CategoryLevelDomainModel;
import com.tokopedia.seller.product.view.mapper.CategoryViewMapper;
import com.tokopedia.seller.product.view.model.CategoryLevelViewModel;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryPickerPresenterImpl extends CategoryPickerPresenter {
    private final FetchCategoryUseCaseChildUseCase fetchCategoryChildUseCase;
    private final FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase;

    public CategoryPickerPresenterImpl(
            FetchCategoryUseCaseChildUseCase fetchCategoryChildUseCase,
            FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase) {
        this.fetchCategoryChildUseCase = fetchCategoryChildUseCase;
        this.fetchCategoryFromSelectedUseCase = fetchCategoryFromSelectedUseCase;
    }

    @Override
    public void fetchCategoryLevelOne() {
        checkViewAttached();
        getView().showLoadingDialog();
        RequestParams requestParam = FetchCategoryUseCaseChildUseCase.generateLevelOne();
        fetchCategoryChildUseCase.execute(requestParam, new FetchCategoryParentSubscriber());
    }

    @Override
    public void fetchCategoryChild(long categoryId) {
        checkViewAttached();
        RequestParams requestParam = FetchCategoryUseCaseChildUseCase.generateFromParent(categoryId);
        fetchCategoryChildUseCase.execute(requestParam, new FetchCategoryChildSubscriber());
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

    private class FetchCategoryFromSelectedSubscriber extends Subscriber<List<CategoryLevelDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            fetchCategoryLevelOne();
        }

        @Override
        public void onNext(List<CategoryLevelDomainModel> categoryLevelDomainModels) {
            checkViewAttached();
            List<CategoryLevelViewModel> categoryLevelViewModels = CategoryViewMapper.mapLevel(categoryLevelDomainModels);
            getView().renderCategoryFromSelected(categoryLevelViewModels);
        }
    }

    private class FetchCategoryParentSubscriber extends Subscriber<List<CategoryDomainModel>> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
            getView().dismissLoadingDialog();
            getView().showRetryEmpty();
        }

        @Override
        public void onNext(List<CategoryDomainModel> domainModels) {
            getView().dismissLoadingDialog();
            List<CategoryViewModel> map = CategoryViewMapper.mapList(domainModels);
            getView().renderCategory(map);
        }
    }
}
