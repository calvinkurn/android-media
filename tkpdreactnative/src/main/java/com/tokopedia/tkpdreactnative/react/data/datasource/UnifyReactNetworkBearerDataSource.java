package com.tokopedia.tkpdreactnative.react.data.datasource;

import android.net.Uri;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;
import com.tokopedia.tkpdreactnative.react.data.datasource.interceptor.ReactNativeBearerInterceptor;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by alvarisi on 10/9/17.
 */

public class UnifyReactNetworkBearerDataSource {
    private Retrofit.Builder retrofit;
    private SessionHandler sessionHandler;

    public UnifyReactNetworkBearerDataSource(Retrofit.Builder retrofit, SessionHandler sessionHandler) {
        this.retrofit = retrofit;
        this.sessionHandler = sessionHandler;
    }

    public Observable<String> request(ReactNetworkingConfiguration configuration) {
        ReactNativeBearerInterceptor reactNativeInterceptor = new ReactNativeBearerInterceptor(
                sessionHandler.getAccessToken(
                        sessionHandler.getActiveContext()
                )
        );
        reactNativeInterceptor.setHeaders(configuration.getHeaders());

        Uri uri = Uri.parse(configuration.getUrl());
        CommonService commonService = retrofit.baseUrl(uri.getScheme() + "://" + uri.getHost())
                .client(OkHttpFactory.create().buildClientDynamicAuth().newBuilder().addInterceptor(reactNativeInterceptor).build())
                .build().create(CommonService.class);
        return requestToNetwork(configuration, commonService);
    }

    private Observable<String> requestToNetwork(ReactNetworkingConfiguration configuration, CommonService commonService) {
        switch (configuration.getMethod()) {
            case ReactConst.GET:
                return commonService.get(configuration.getUrl(), configuration.getParams());
            case ReactConst.POST:
                return commonService.post(configuration.getUrl(), configuration.getParams());
            case ReactConst.DELETE:
                return commonService.delete(configuration.getUrl(), configuration.getParams());
            case ReactConst.PUT:
                return commonService.put(configuration.getUrl(), configuration.getParams());
            case ReactConst.HEAD:
                return commonService.head(configuration.getUrl(), configuration.getParams());
            default:
                return commonService.get(configuration.getUrl(), configuration.getParams());
        }
    }
}
