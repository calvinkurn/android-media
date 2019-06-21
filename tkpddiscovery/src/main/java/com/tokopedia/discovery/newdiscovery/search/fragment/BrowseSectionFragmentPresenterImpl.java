package com.tokopedia.discovery.newdiscovery.search.fragment;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;

import java.util.HashMap;

/**
 * Created by henrypriyono on 11/22/17.
 */

public abstract class BrowseSectionFragmentPresenterImpl<V extends BrowseSectionFragmentView> extends BaseDaggerPresenter<V> implements BrowseSectionFragmentPresenter<V> {

    public AppComponent getComponent(Context context) {
        return ((MainApplication) context).getAppComponent();
    }

    @Override
    public void requestDynamicFilter() {
        if (getView() == null) {
            return;
        }
        RequestParams params = getDynamicFilterParam();
        params = enrichWithFilterAndSortParams(params);
        removeDefaultCategoryParam(params);
        getFilterFromNetwork(params);
    }

    protected void enrichWithAdditionalParams(RequestParams requestParams,
                                                     HashMap<String, String> additionalParams) {
        requestParams.putAll(additionalParams);
    }

    protected RequestParams enrichWithFilterAndSortParams(RequestParams requestParams) {
        if (getView() == null) {
            return requestParams;
        }
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
