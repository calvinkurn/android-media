package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.FetchAllCategoryDataUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryFromParentUseCase;
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
    private final FetchCategoryFromParentUseCase fetchCategoryFromParentUseCase;
    private final FetchAllCategoryDataUseCase fetchAllCategoryDataUseCase;
    private final FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase;

    public CategoryPickerPresenterImpl(
            FetchCategoryFromParentUseCase fetchCategoryFromParentUseCase,
            FetchAllCategoryDataUseCase fetchAllCategoryDataUseCase, FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase) {
        this.fetchCategoryFromParentUseCase = fetchCategoryFromParentUseCase;
        this.fetchAllCategoryDataUseCase = fetchAllCategoryDataUseCase;
        this.fetchCategoryFromSelectedUseCase = fetchCategoryFromSelectedUseCase;
    }

    @Override
    public void fetchCategoryLevelOne() {
        checkViewAttached();
        RequestParams requestParam = FetchCategoryFromParentUseCase.generateLevelOne();
        fetchCategoryFromParentUseCase.execute(requestParam, new FetchCategoryFromParentSubscriber());

    }

    @Override
    public void fetchCategoryWithParent(int categoryId) {
        checkViewAttached();
        RequestParams requestParam = FetchCategoryFromParentUseCase.generateFromParent(categoryId);
        fetchCategoryFromParentUseCase.execute(requestParam, new FetchCategoryFromParentSubscriber());
    }

    @Override
    public void fetchAllCategoryData() {
        checkViewAttached();
        getView().showLoadingDialog();
        fetchAllCategoryDataUseCase.execute(RequestParams.EMPTY, new FetchAllCategoryDataSubscriber());
    }

    @Override
    public void fetchCategoryFromSelected(int initSelected) {
        checkViewAttached();
        RequestParams requestParam = FetchCategoryFromSelectedUseCase.generateParam(initSelected);
        fetchCategoryFromSelectedUseCase.execute(requestParam, new FetchCategoryFromSelectedSubscriber());
    }

    @Override
    public void unsubscribe() {
        fetchCategoryFromParentUseCase.unsubscribe();
        fetchAllCategoryDataUseCase.unsubscribe();
        fetchCategoryFromSelectedUseCase.unsubscribe();
    }

    private class FetchCategoryFromParentSubscriber extends Subscriber<List<CategoryDomainModel>> {
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
            List<CategoryLevelViewModel> categoryLevelViewModels =
                    CategoryViewMapper.mapLevel(categoryLevelDomainModels);
            getView().renderCategoryFromSelected(categoryLevelViewModels);
        }
    }
}
