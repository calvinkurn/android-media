package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetBrowseCatalogLoadMoreUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetBrowseCatalogUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenterImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.subscriber.GetBrowseCatalogLoadMoreSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.subscriber.GetBrowseCatalogSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.GetDynamicFilterSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.subscriber.RefreshCatalogSubscriber;

import javax.inject.Inject;

/**
 * Created by hangnadi on 10/12/17.
 */

public class CatalogPresenter extends SearchSectionFragmentPresenterImpl<CatalogFragmentContract.View>
        implements CatalogFragmentContract.Presenter{

    @Inject
    GetBrowseCatalogUseCase getBrowseCatalogUseCase;

    @Inject
    GetBrowseCatalogLoadMoreUseCase getBrowseCatalogLoadMoreUseCase;

    @Inject
    GetDynamicFilterUseCase getDynamicFilterUseCase;

    private final Context context;

    public CatalogPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void requestCatalogList() {
        RequestParams requestParams = generateParamInitBrowseCatalog();
        getView().initTopAdsParamsByQuery(requestParams);
        getBrowseCatalogUseCase.execute(requestParams, new GetBrowseCatalogSubscriber(getView()));
    }

    @Override
    public void requestCatalogLoadMore() {
        getBrowseCatalogLoadMoreUseCase.execute(generateParamLoadMoreBrowseCatalog(), new GetBrowseCatalogLoadMoreSubscriber(getView()));
    }

    @Override
    protected void getFilterFromNetwork(RequestParams requestParams) {
        getDynamicFilterUseCase.execute(requestParams, new GetDynamicFilterSubscriber(getView()));
    }

    @Override
    protected RequestParams getDynamicFilterParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(AuthUtil.generateParamsNetwork2(context, requestParams.getParameters()));
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_CATALOG);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        if (getView().getDepartmentId() != null && !getView().getDepartmentId().isEmpty()) {
            requestParams.putString(BrowseApi.SC, getView().getDepartmentId());
        } else {
            requestParams.putString(BrowseApi.Q, getView().getQueryKey());
            requestParams.putString(BrowseApi.SC, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_SC);
        }
        return requestParams;
    }

    @Override
    public void refreshSort() {
        if (getView().getDepartmentId() != null && !getView().getDepartmentId().isEmpty()) {
            getBrowseCatalogUseCase.execute(
                    generateParamInitBrowseCatalog(getView().getDepartmentId()),
                    new RefreshCatalogSubscriber(getView())
            );
        } else {
            getBrowseCatalogUseCase.execute(
                    generateParamInitBrowseCatalog(),
                    new RefreshCatalogSubscriber(getView())
            );
        }
    }

    @Override
    public void requestCatalogList(String departmentId) {
        if (getView() == null) {
            return;
        }
        RequestParams requestParams = generateParamInitBrowseCatalog(departmentId);
        getView().initTopAdsParamsByCategory(requestParams);
        getBrowseCatalogUseCase.execute(requestParams, new GetBrowseCatalogSubscriber(getView()));
    }

    @Override
    public void requestCatalogLoadMore(String departmentId) {
        getBrowseCatalogLoadMoreUseCase.execute(generateParamLoadMoreBrowseCatalog(departmentId), new GetBrowseCatalogLoadMoreSubscriber(getView()));
    }

    private RequestParams generateParamInitBrowseCatalog() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(getView().getSearchParameter().getSearchParameterMap());
        requestParams.putString(SearchApiConst.Q, getView().getQueryKey());

        setRequestParamsDefaultValues(requestParams);
        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);
        return requestParams;
    }

    private RequestParams generateParamLoadMoreBrowseCatalog() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(getView().getSearchParameter().getSearchParameterMap());
        requestParams.putString(SearchApiConst.Q, getView().getQueryKey());

        setRequestParamsDefaultValues(requestParams);
        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);
        return requestParams;
    }

    private RequestParams generateParamInitBrowseCatalog(String departmentId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(getView().getSearchParameter().getSearchParameterMap());
        requestParams.putString(SearchApiConst.SC, departmentId);

        setRequestParamsDefaultValues(requestParams);
        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);
        return requestParams;
    }

    private RequestParams generateParamLoadMoreBrowseCatalog(String departmentId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(getView().getSearchParameter().getSearchParameterMap());
        requestParams.putString(SearchApiConst.SC, departmentId);

        setRequestParamsDefaultValues(requestParams);
        enrichWithFilterAndSortParams(requestParams);
        removeDefaultCategoryParam(requestParams);
        return requestParams;
    }

    private void setRequestParamsDefaultValues(RequestParams requestParams) {
        requestParams.putString(SearchApiConst.ROWS, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS);
        requestParams.putString(SearchApiConst.START, String.valueOf(getView().getStartFrom()));
        requestParams.putString(SearchApiConst.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(SearchApiConst.TERMS, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_TERM);
        requestParams.putString(SearchApiConst.BREADCRUMB, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_BREADCRUMB);
        requestParams.putString(SearchApiConst.IMAGE_SIZE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(SearchApiConst.OB, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_SORT);
    }

    @Override
    public void detachView() {
        super.detachView();
        getBrowseCatalogUseCase.unsubscribe();
        getBrowseCatalogLoadMoreUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
    }
}
