package com.tokopedia.discovery.intermediary.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.hades.HadesService;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.intermediary.data.mapper.IntermediaryCategoryMapper;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by alifa on 3/27/17.
 */

public class IntermediaryDataSource {

    private final Context context;
    private final HadesApi hadesApi;
    private final IntermediaryCategoryMapper mapper;

    public IntermediaryDataSource(Context context, HadesApi hadesApi, IntermediaryCategoryMapper mapper) {
        this.context = context;
        this.hadesApi = hadesApi;
        this.mapper = mapper;
    }

    public Observable<IntermediaryCategoryDomainModel> getintermediaryCategory(String categoryId) {

        return hadesApi.getCategories(categoryId)
                .map(mapper);
    }
}
