package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;

import java.util.HashMap;

/**
 * Created by henrypriyono on 11/22/17.
 */

public abstract class SearchSectionFragmentPresenterImpl<V extends SearchSectionFragmentView> extends BaseDaggerPresenter<V> implements SearchSectionFragmentPresenter<V> {
    @Override
    public void requestDynamicFilter() {
        requestDynamicFilter(new HashMap<String, String>());
    }

    @Override
    public void requestDynamicFilter(HashMap<String, String> additionalParams) {
        RequestParams params = getDynamicFilterParam();
        params = enrichWithFilterAndSortParams(params);
        params = enrichWithAdditionalParams(params, additionalParams);
        removeDefaultCategoryParam(params);
        getFilterFromNetwork(params);
    }

    protected RequestParams enrichWithAdditionalParams(RequestParams requestParams,
                                                     HashMap<String, String> additionalParams) {
        requestParams.putAll(additionalParams);
        return requestParams;
    }

    protected RequestParams enrichWithFilterAndSortParams(RequestParams requestParams) {
        if (getView().getSelectedSort() != null) {
            requestParams.putAll(getView().getSelectedSort());
        }
        if (getView().getSelectedFilter() != null) {
            requestParams.putAll(getView().getSelectedFilter());
        }
        if (getView().getExtraFilter() != null) {
            requestParams.putAll(getView().getExtraFilter());
        }
        return requestParams;
    }

    protected void removeDefaultCategoryParam(RequestParams params) {
        if (params.getString(BrowseApi.SC, "").equals(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_SC)) {
            params.clearValue(BrowseApi.SC);
        }
    }

    protected abstract RequestParams getDynamicFilterParam();
    protected abstract void getFilterFromNetwork(RequestParams requestParams);
}
