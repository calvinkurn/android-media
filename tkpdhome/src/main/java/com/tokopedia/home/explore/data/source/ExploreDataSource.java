package com.tokopedia.home.explore.data.source;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.source.api.HomeDataApi;
import com.tokopedia.home.explore.domain.model.ExploreDataModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreDataSource {
    private Context context;
    private HomeDataApi homeDataApi;

    public ExploreDataSource(Context context, HomeDataApi homeDataApi) {
        this.context = context;
        this.homeDataApi = homeDataApi;
    }

    public Observable<ExploreDataModel> getExploreData() {
        return homeDataApi.getExploreData(getRequestPayload())
                .map(new Func1<Response<GraphqlResponse<ExploreDataModel>>, ExploreDataModel>() {
            @Override
            public ExploreDataModel call(Response<GraphqlResponse<ExploreDataModel>> response) {
                if(response.isSuccessful()) {
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
        });
    }

    private String getRequestPayload() {
        return loadRawString(context.getResources(), R.raw.explore_data_query);
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
