package com.tokopedia.home.beranda.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.beranda.domain.model.HomeData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeDataSource {
    private HomeDataApi homeDataApi;
    private HomeMapper homeMapper;
    private Context context;
    private GlobalCacheManager cacheManager;
    private Gson gson;

    public HomeDataSource(HomeDataApi homeDataApi,
                          HomeMapper homeMapper,
                          Context context,
                          GlobalCacheManager cacheManager,
                          Gson gson) {
        this.homeDataApi = homeDataApi;
        this.homeMapper = homeMapper;
        this.context = context;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    public Observable<List<Visitable>> getCache() {
        return Observable.just(true).map(new Func1<Boolean, Response<GraphqlResponse<HomeData>>>() {
            @Override
            public Response<GraphqlResponse<HomeData>> call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.HOME_DATA_CACHE);
                if (cache != null) {
                    HomeData homeData = gson.fromJson(cache, HomeData.class);
                    GraphqlResponse<HomeData> graphqlResponse = new GraphqlResponse<>();
                    graphqlResponse.setData(homeData);
                    return Response.success(graphqlResponse);
                }
                throw new RuntimeException("Cache is empty!!");
            }
        }).map(homeMapper);
    }

    public Observable<List<Visitable>> getHomeData() {
        return homeDataApi.getHomeData(getRequestPayload()).map(saveToCache()).map(homeMapper);
    }

    private Func1<Response<GraphqlResponse<HomeData>>, Response<GraphqlResponse<HomeData>>> saveToCache() {
        return new Func1<Response<GraphqlResponse<HomeData>>, Response<GraphqlResponse<HomeData>>>() {
            @Override
            public Response<GraphqlResponse<HomeData>> call(Response<GraphqlResponse<HomeData>> response) {
                if (response.isSuccessful()) {
                    HomeData homeData = response.body().getData();
                    cacheManager.setKey(TkpdCache.Key.HOME_DATA_CACHE);
                    cacheManager.setValue(gson.toJson(homeData));
                    cacheManager.store();
                }
                return response;
            }
        };
    }

    private String getRequestPayload() {
        return loadRawString(context.getResources(), R.raw.home_query);
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {rawResource.close();} catch (IOException e) {}
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
        } catch (IOException e) {}
        return stringBuilder.toString();
    }
}
