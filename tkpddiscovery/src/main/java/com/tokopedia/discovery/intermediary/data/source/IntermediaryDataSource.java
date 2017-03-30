package com.tokopedia.discovery.intermediary.data.source;

import android.content.Context;

import com.tokopedia.core.database.recharge.operator.Operator;
import com.tokopedia.core.database.recharge.product.Product;
import com.tokopedia.core.network.apiservices.ace.apis.SearchApi;
import com.tokopedia.core.network.apiservices.hades.HadesService;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.core.network.entity.categoriesHades.CategoryHadesModel;
import com.tokopedia.core.network.entity.hotlist.HotListResponse;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.intermediary.data.mapper.IntermediaryCategoryMapper;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

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
        param.put("perPage", "4");

        return Observable.zip(hadesApi.getCategories(categoryId),
                aceApi.getHotlistCategory(param), mapper);

    }


}
