package com.tokopedia.seller.product.data.source.cloud;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.data.mapper.GenerateHostMapper;
import com.tokopedia.seller.product.data.source.cloud.api.GenerateHostApi;
import com.tokopedia.seller.product.data.source.cloud.model.GenerateHost;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 4/11/17.
 */

public class GenerateHostDataSourceCloud {
    private final GenerateHostApi generateHostApi;
    private final GenerateHostMapper generateHostMapper;
    private final Context context;
    private GenerateHost generateHost;

    @Inject
    public GenerateHostDataSourceCloud(@ActivityContext Context context, GenerateHostApi generateHostApi, GenerateHostMapper generateHostMapper) {
        this.generateHostApi = generateHostApi;
        this.generateHostMapper = generateHostMapper;
        this.context = context;
    }

    public Observable<GenerateHost> generateHost() {
        return Observable.just(generateHost)
                .map(new CheckHostNotEmpty())
                .onErrorResumeNext(fetchGenerateHostFromNetwork());
    }

    private Observable<GenerateHost> fetchGenerateHostFromNetwork() {
        return generateHostApi.generateHost(AuthUtil.generateParamsNetwork(context, getParamsGenerateHost()))
                .flatMap(generateHostMapper)
                .doOnNext(storeHost());
    }

    @NonNull
    private Action1<GenerateHost> storeHost() {
        return new Action1<GenerateHost>() {
            @Override
            public void call(GenerateHost generateHost) {
                GenerateHostDataSourceCloud.this.generateHost = generateHost;
            }
        };
    }

    public TKPDMapParam<String,String> getParamsGenerateHost() {
        TKPDMapParam<String, String> paramsGenerateHost = new TKPDMapParam<>();
        paramsGenerateHost.put(ProductNetworkConstant.SERVER_LANGUAGE, ProductNetworkConstant.GOLANG_VALUE);
        return paramsGenerateHost;
    }

    private class CheckHostNotEmpty implements Func1<GenerateHost, GenerateHost> {
        @Override
        public GenerateHost call(GenerateHost generateHost) {
            if (generateHost != null) {
                return generateHost;
            } else {
                throw new RuntimeException("generate host not available");
            }
        }
    }
}
