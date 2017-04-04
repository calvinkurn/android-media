package com.tokopedia.topads.sdk.data.datasource;

import android.content.Context;

import com.tokopedia.topads.sdk.domain.interactor.TopAdsMapper;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.network.HttpMethod;
import com.tokopedia.topads.sdk.network.HttpRequest;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;
import com.tokopedia.topads.sdk.base.TKPDMapParam;

import java.io.IOException;


/**
 * @author by errysuprayogi on 3/27/17.
 */

public class CloudTopAdsDataSource implements TopAdsDataSource {

    private Context context;
    private static final String BASE_URL = "https://ta.tokopedia.com/promo/v1.1/display/ads";
    private static final String TKPD_SESSION_ID = "Tkpd-SessionId";

    public CloudTopAdsDataSource(Context context) {
        this.context = context;
    }

    @Override
    public TopAdsModel getTopAds(String sessionId, TKPDMapParam<String, String> params) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(BASE_URL)
                .addHeader(TKPD_SESSION_ID, sessionId)
                .setMethod(HttpMethod.GET)
                .addParameters(params)
                .build();
        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        return new TopAdsMapper(context, executor).getModel();
    }

    @Override
    public String clickTopAdsUrl(String url) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(url)
                .setMethod(HttpMethod.GET)
                .build();
        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        try {
            return executor.executeAsGetRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; //just return empty string.
    }
}
