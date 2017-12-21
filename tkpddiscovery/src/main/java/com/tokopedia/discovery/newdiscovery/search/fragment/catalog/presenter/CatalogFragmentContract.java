package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hangnadi on 10/12/17.
 */

public interface CatalogFragmentContract {

    interface View extends SearchSectionFragmentView {

        String getQueryKey();

        String getDepartmentId();

        void setQueryKey(String queryKey);

        void setDepartmentId(String departmentId);

        void renderListView(List<Visitable> catalogViewModels);

        void renderNextListView(List<Visitable> catalogViewModels);

        void renderErrorView(String message);

        void renderRetryInit();

        void renderUnknown();

        void renderShareURL(String shareURL);

        void setHasNextPage(boolean hasNextPage);

        int getStartFrom();

        void initTopAdsParamsByQuery(RequestParams requestParams);

        void initTopAdsParamsByCategory(RequestParams requestParams);

        String getSource();

        void successRefreshCatalog(List<Visitable> visitables);

        void renderRetryRefresh();

        void setTopAdsEndlessListener();

        void unSetTopAdsEndlessListener();

        void backToTop();
    }

    interface Presenter extends SearchSectionFragmentPresenter<View> {

        void requestCatalogList();

        void requestCatalogLoadMore();

        void requestCatalogList(String departmentId);

        void requestCatalogLoadMore(String departmentId);

        void refreshSort();
    }
}
