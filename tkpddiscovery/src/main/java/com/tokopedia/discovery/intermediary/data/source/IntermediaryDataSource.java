package com.tokopedia.discovery.intermediary.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.ace.apis.SearchApi;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.discovery.intermediary.data.mapper.IntermediaryCategoryMapper;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;

import static com.tokopedia.core.network.apiservices.hades.apis.HadesApi.CATEGORIES_PARAM;
import static com.tokopedia.core.network.apiservices.hades.apis.HadesApi.CURATED_PARAM;
import static com.tokopedia.core.network.apiservices.hades.apis.HadesApi.PAGE_PARAM;

/**
 * Created by alifa on 3/27/17.
 */

public class IntermediaryDataSource {

    private final Context context;
    private final HadesApi hadesApi;
    private final SearchApi aceApi;
    private final MojitoApi mojitoApi;
    private final IntermediaryCategoryMapper mapper;

    private final static String NUM_PAGE = "7";
    private final static String NUM_CURATED = "6";

    public IntermediaryDataSource(Context context, HadesApi hadesApi, SearchApi searchApi,
                                  MojitoApi mojitoApi, IntermediaryCategoryMapper mapper) {
        this.context = context;
        this.hadesApi = hadesApi;
        this.aceApi=searchApi;
        this.mojitoApi = mojitoApi;
        this.mapper = mapper;
    }

    public Observable<IntermediaryCategoryDomainModel> getintermediaryCategory(String categoryId, CategoryHadesModel categoryHadesModel) {

        Map<String, String> param = new HashMap<>();
        param.put(CATEGORIES_PARAM,categoryId);
        param.put(PAGE_PARAM, NUM_PAGE);

        Map<String, String> paramCat = new HashMap<>();
        paramCat.put(CURATED_PARAM,NUM_CURATED);

        return Observable.zip(Observable.just(categoryHadesModel), aceApi.getHotlistCategory(param), mojitoApi.getBrandsCategory(categoryId), mapper);

    }

    public Observable<Response<CategoryHadesModel>> getCategoryHeader(String categoryId) {

        Map<String, String> paramCat = new HashMap<>();
        paramCat.put(CURATED_PARAM,NUM_CURATED);

        return hadesApi.getCategories(HadesApi.ANDROID_DEVICE,categoryId,paramCat);

    }

}
