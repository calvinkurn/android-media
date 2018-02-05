package com.tokopedia.home.explore.data.source;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.source.api.HomeDataApi;
import com.tokopedia.home.beranda.data.source.pojo.HomeData;
import com.tokopedia.home.explore.domain.model.ExploreDataModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreDataSource {
    private Context context;
    private HomeDataApi homeDataApi;
    private GlobalCacheManager cacheManager;
    private Gson gson;

    public ExploreDataSource(Context context, HomeDataApi homeDataApi,
                             GlobalCacheManager cacheManager,
                             Gson gson) {
        this.context = context;
        this.homeDataApi = homeDataApi;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    public Observable<ExploreDataModel> getExploreData() {
        return homeDataApi.getExploreData(getRequestPayload())
                .doOnNext(saveToCache())
                .map(getMapper());
    }

    private Action1<Response<GraphqlResponse<ExploreDataModel>>> saveToCache() {
        return new Action1<Response<GraphqlResponse<ExploreDataModel>>>() {
            @Override
            public void call(Response<GraphqlResponse<ExploreDataModel>> response) {
                if (response.isSuccessful()) {
                    ExploreDataModel data = response.body().getData();
                    cacheManager.setKey(TkpdCache.Key.EXPLORE_DATA_CACHE);
                    cacheManager.setValue(gson.toJson(data));
                    cacheManager.store();
                }
            }
        };
    }

    @NonNull
    private Func1<Response<GraphqlResponse<ExploreDataModel>>, ExploreDataModel> getMapper() {
        return new Func1<Response<GraphqlResponse<ExploreDataModel>>, ExploreDataModel>() {
            @Override
            public ExploreDataModel call(Response<GraphqlResponse<ExploreDataModel>> response) {
                if (response.isSuccessful()) {
                    return response.body().getData();
                } else {
                    String messageError = ErrorHandler.getErrorMessage(response);
                    if (!TextUtils.isEmpty(messageError)) {
                        throw new ErrorMessageException(messageError);
                    } else {
                        throw new RuntimeException(String.valueOf(response.code()));
                    }
                }
            }
        };
    }

    private String getRequestPayload() {
        return loadRawString(context.getResources(), R.raw.explore_data_query);
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }

    public Observable<ExploreDataModel> getDataCache() {
        return Observable.just(true).map(new Func1<Boolean, Response<GraphqlResponse<ExploreDataModel>>>() {
            @Override
            public Response<GraphqlResponse<ExploreDataModel>> call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.EXPLORE_DATA_CACHE);
                if (cache != null) {
                    ExploreDataModel data = gson.fromJson(cache, ExploreDataModel.class);
                    GraphqlResponse<ExploreDataModel> graphqlResponse = new GraphqlResponse<>();
                    graphqlResponse.setData(data);
                    return Response.success(graphqlResponse);
                }
                throw new RuntimeException("Cache is empty!!");
            }
        }).map(getMapper()).onErrorResumeNext(getExploreData());
    }
}
