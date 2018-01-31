package com.tokopedia.home.beranda.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.data.source.api.HomeDataApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import rx.Observable;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeDataSource {
    private HomeDataApi homeDataApi;
    private HomeMapper homeMapper;
    private Context context;

    public HomeDataSource(HomeDataApi homeDataApi,
                          HomeMapper homeMapper,
                          Context context) {
        this.homeDataApi = homeDataApi;
        this.homeMapper = homeMapper;
        this.context = context;
    }

    public Observable<List<Visitable>> getHomeData() {
        return homeDataApi.getHomeData(getRequestPayload()).map(homeMapper);
    }

    private String getRequestPayload() {
        return loadRawString(context.getResources(), R.raw.home_data_query);
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
