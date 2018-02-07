package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdynamicfilter.helper.DynamicFilterDbManager;

import java.lang.reflect.Type;
import java.util.List;

import rx.Subscriber;

/**
 * Created by hangnadi on 10/16/17.
 */

public class GetDynamicFilterSubscriber extends Subscriber<DynamicFilterModel> {

    protected final SearchSectionFragmentView view;

    public GetDynamicFilterSubscriber(SearchSectionFragmentView view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.renderFailGetDynamicFilter();
    }

    @Override
    public void onNext(DynamicFilterModel dynamicFilterModel) {
        if (dynamicFilterModel != null) {
            storeLocalFilter(dynamicFilterModel);
            view.renderDynamicFilter(dynamicFilterModel);
        } else {
            view.renderFailGetDynamicFilter();
        }
    }

    private void storeLocalFilter(DynamicFilterModel model) {
        Type listType = new TypeToken<List<Filter>>() {}.getType();
        Gson gson = new Gson();
        String filterData = gson.toJson(model.getData().getFilter(), listType);

        DynamicFilterDbManager cache = new DynamicFilterDbManager();
        cache.setFilterID(view.getScreenNameId());
        cache.setFilterData(filterData);
        cache.store();
    }
}
