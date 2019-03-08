package com.tokopedia.discovery.newdiscovery.category.presentation;

import android.content.Context;

import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryPresenter;
import com.tokopedia.discovery.newdiscovery.category.di.component.CategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.di.component.DaggerCategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.domain.usecase.GetCategoryHeaderUseCase;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.helper.CategoryModelHelper;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * @author by alifa on 10/25/17.
 */

public class CategoryPresenter extends DiscoveryPresenter<CategoryContract.View, CategoryActivity>
        implements CategoryContract.Presenter {

    @Inject
    GetCategoryHeaderUseCase getCategoryHeaderUseCase;
    @Inject
    UserSessionInterface userSession;
    GetProductUseCase getProductUseCase;
    GCMHandler gcmHandler;

    public CategoryPresenter(Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        super(context, getProductUseCase, getImageSearchUseCase);
        this.getProductUseCase = getProductUseCase;
        this.gcmHandler = new GCMHandler(context);
        CategoryComponent categoryComponent = DaggerCategoryComponent.builder()
                .appComponent(getComponent(context))
                .build();
        categoryComponent.inject(this);
    }

    @Override
    public void getCategoryHeader(String categoryId, HashMap<String, String> filterParam) {
        getCategoryHeaderUseCase.setCategoryId(categoryId);
        getView().showLoading();
        getCategoryHeaderUseCase.execute(RequestParams.EMPTY, new CategoryHeaderSubscriber(filterParam));
    }

    @Override
    public void getCategoryPage1(CategoryHeaderModel categoryHeaderModel) {
        if (isViewAttached()) {
            getView().showLoading();

            SearchParameter searchParameter = generateSearchParameter(categoryHeaderModel.getDepartementId());

            getProductUseCase.execute(
                    GetProductUseCase.createInitializeSearchParam(searchParameter, false),
                    new CategoryProductSubscriber(categoryHeaderModel)
            );
        }
    }

    private SearchParameter generateSearchParameter(String departementID) {
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.set(SearchApiConst.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
        searchParameter.set(SearchApiConst.SC, departementID);
        searchParameter.set(SearchApiConst.UNIQUE_ID, generateUniqueID());
        searchParameter.set(SearchApiConst.USER_ID, generateUserID());

        return searchParameter;
    }

    private String generateUniqueID() {
        return userSession.isLoggedIn() ?
                AuthUtil.md5(userSession.getUserId()) :
                AuthUtil.md5(gcmHandler.getRegistrationId());
    }

    private String generateUserID() {
        return userSession.isLoggedIn() ?
                userSession.getUserId() :
                "";
    }

    private class CategoryHeaderSubscriber extends DefaultSubscriber<CategoryHeaderModel> {

        private final HashMap<String, String> filterParam;

        private CategoryHeaderSubscriber(HashMap<String, String> filterParam) {
            this.filterParam = filterParam;
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            ((DiscoveryActivity) getView()).onHandleResponseError();
        }

        @Override
        public void onNext(CategoryHeaderModel categoryHeaderModel) {
            if (isViewAttached()) {
                SearchParameter searchParameter = generateSearchParameter(categoryHeaderModel.getDepartementId());

                RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false);
                if (filterParam != null && filterParam.size() > 0) {
                    requestParams.putAll(filterParam);
                }
                getProductUseCase.execute(
                        requestParams,
                        new CategoryProductSubscriber(categoryHeaderModel)
                );
            }
        }
    }

    private class CategoryProductSubscriber extends DefaultSubscriber<SearchResultModel> {

        private final CategoryHeaderModel categoryHeaderModel;

        public CategoryProductSubscriber(CategoryHeaderModel categoryHeaderModel) {
            this.categoryHeaderModel = categoryHeaderModel;
        }

        @Override
        public void onCompleted() {
            getView().hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            ((DiscoveryActivity) getView()).onHandleResponseError();
        }

        @Override
        public void onNext(SearchResultModel searchResultModel) {
            ProductViewModel productViewModel
                    = CategoryModelHelper.convertToProductViewModel(searchResultModel, categoryHeaderModel);
            getView().prepareFragment(productViewModel);

        }

    }

    @Override
    public void detachView() {
        super.detachView();
        getCategoryHeaderUseCase.unsubscribe();
        getProductUseCase.unsubscribe();
    }
}

