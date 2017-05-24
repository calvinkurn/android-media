package com.tokopedia.discovery.intermediary.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.ace.apis.SearchApi;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.discovery.intermediary.data.mapper.IntermediaryCategoryMapper;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by alifa on 3/27/17.
 */

public class IntermediaryDataSource {

    private final Context context;
    private final HadesApi hadesApi;
    private final SearchApi aceApi;
    private final IntermediaryCategoryMapper mapper;

    public IntermediaryDataSource(Context context, HadesApi hadesApi, SearchApi searchApi,
                                  IntermediaryCategoryMapper mapper) {
        this.context = context;
        this.hadesApi = hadesApi;
        this.aceApi=searchApi;
        this.mapper = mapper;
    }

    public Observable<IntermediaryCategoryDomainModel> getintermediaryCategory(String categoryId) {

        Map<String, String> param = new HashMap<>();
        param.put("categories",categoryId);
        param.put("perPage", "7");

        return Observable.zip(hadesApi.getCategories(HadesApi.ANDROID_DEVICE,categoryId),
                aceApi.getHotlistCategory(param), mapper);

    }


}
