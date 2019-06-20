package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionFragmentView;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;

import java.util.List;

/**
 * Created by hangnadi on 10/12/17.
 */

public interface CatalogFragmentContract {

    interface View extends BrowseSectionFragmentView {

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

        SearchParameter getSearchParameter();
    }

    interface Presenter extends BrowseSectionFragmentPresenter<View> {

        void requestCatalogList();

        void requestCatalogLoadMore();

        void requestCatalogList(String departmentId);

        void requestCatalogLoadMore(String departmentId);

        void refreshSort();
    }
}
