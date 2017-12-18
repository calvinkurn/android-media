package com.tokopedia.discovery.newdiscovery.category.presentation;

import android.content.Context;

import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryPresenter;
import com.tokopedia.discovery.newdiscovery.category.di.component.CategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.di.component.DaggerCategoryComponent;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.domain.usecase.GetCategoryHeaderUseCase;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.helper.CategoryModelHelper;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * @author by alifa on 10/25/17.
 */

public class CategoryPresenter extends DiscoveryPresenter<CategoryContract.View, CategoryActivity>
        implements CategoryContract.Presenter {

    @Inject
    GetCategoryHeaderUseCase getCategoryHeaderUseCase;
    GetProductUseCase getProductUseCase;
    SessionHandler sessionHandler;
    GCMHandler gcmHandler;

    public CategoryPresenter(Context context, GetProductUseCase getProductUseCase) {
        super(getProductUseCase);
        this.getProductUseCase = getProductUseCase;
        this.sessionHandler = new SessionHandler(context);
        this.gcmHandler = new GCMHandler(context);
        CategoryComponent categoryComponent = DaggerCategoryComponent.builder()
                .appComponent(getComponent(context))
                .build();
        categoryComponent.inject(this);
    }

    @Override
    public void getCategoryHeader(String categoryId, HashMap<String,String> filterParam) {
        getCategoryHeaderUseCase.setCategoryId(categoryId);
        getView().showLoading();
        getCategoryHeaderUseCase.execute(RequestParams.EMPTY,new CategoryHeaderSubscriber(filterParam));
    }

    @Override
    public void getCategoryPage1(CategoryHeaderModel categoryHeaderModel) {
        if (isViewAttached()) {
            getView().showLoading();
            SearchParameter searchParameter = new SearchParameter();
            searchParameter.setSource(BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
            searchParameter.setDepartmentId(categoryHeaderModel.getDepartementId());
            searchParameter.setUniqueID(
                    sessionHandler.isV4Login() ?
                            AuthUtil.md5(sessionHandler.getLoginID()) :
                            AuthUtil.md5(gcmHandler.getRegistrationId())
            );
            searchParameter.setUserID(
                    sessionHandler.isV4Login() ?
                            sessionHandler.getLoginID() :
                            null
            );
            searchParameter.setSource(BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
            getProductUseCase.execute(
                    GetProductUseCase.createInitializeSearchParam(searchParameter, false),
                    new CategoryProductSubscriber(categoryHeaderModel)
            );
        }
    }

    private class CategoryHeaderSubscriber extends DefaultSubscriber<CategoryHeaderModel> {

        private final  HashMap<String,String> filterParam;

        private CategoryHeaderSubscriber(HashMap<String, String> filterParam) {
            this.filterParam = filterParam;
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            ((DiscoveryActivity)getView()).onHandleResponseError();
        }

        @Override
        public void onNext(CategoryHeaderModel categoryHeaderModel) {
            if (isViewAttached()) {
                SearchParameter searchParameter = new SearchParameter();
                searchParameter.setSource(BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
                searchParameter.setDepartmentId(categoryHeaderModel.getDepartementId());
                searchParameter.setUniqueID(
                        sessionHandler.isV4Login() ?
                                AuthUtil.md5(sessionHandler.getLoginID()) :
                                AuthUtil.md5(gcmHandler.getRegistrationId())
                );
                searchParameter.setUserID(
                        sessionHandler.isV4Login() ?
                                sessionHandler.getLoginID() :
                                null
                );
                RequestParams requestParams = GetProductUseCase.createInitializeSearchParam(searchParameter, false);
                if (filterParam != null && filterParam.size()>0) {
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
            ((DiscoveryActivity)getView()).onHandleResponseError();
        }

        @Override
        public void onNext(SearchResultModel searchResultModel) {
            ProductViewModel productViewModel
                    = CategoryModelHelper.convertToProductViewModel(searchResultModel,categoryHeaderModel);
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

