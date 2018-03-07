package com.tokopedia.discovery.newdiscovery.base;

import android.util.Base64;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.network.entity.discovery.ImageSearchResponse;
import com.tokopedia.discovery.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.imagesearch.domain.usecase.RoaSearchRequest;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract.View;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.lang.reflect.Type;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hangnadi on 9/28/17.
 */

@SuppressWarnings("unchecked")
public class DiscoveryPresenter<T1 extends CustomerView, D2 extends View>
        extends BaseDiscoveryPresenter<T1, D2> {

    private GetProductUseCase getProductUseCase;
    private GetImageSearchUseCase getImageSearchUseCase;

    public DiscoveryPresenter(GetProductUseCase getProductUseCase) {
        this.getProductUseCase = getProductUseCase;
    }

    public DiscoveryPresenter(GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        this.getProductUseCase = getProductUseCase;
        this.getImageSearchUseCase = getImageSearchUseCase;
    }

    @Override
    public void requestProduct(SearchParameter searchParameter, boolean forceSearch, boolean requestOfficialStore) {
        super.requestProduct(searchParameter, forceSearch, requestOfficialStore);
        getProductUseCase.execute(
                GetProductUseCase.createInitializeSearchParam(searchParameter, forceSearch, requestOfficialStore),
                new DefaultSearchSubscriber(searchParameter, forceSearch, getBaseDiscoveryView(), false)
        );
    }


    @Override
    public void requestImageSearchProduct(SearchParameter imageSearchProductParameter) {
        super.requestImageSearchProduct(imageSearchProductParameter);
        getImageSearchUseCase.execute(
                GetImageSearchUseCase.initializeSearchRequestParam(imageSearchProductParameter),
                new DefaultSearchSubscriber(imageSearchProductParameter, false, getBaseDiscoveryView(), true)
        );
    }

    @Override
    public void requestImageSearch(final byte[] imageByteArray) {
        super.requestImageSearch(imageByteArray);

        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                IClientProfile profile = DefaultProfile.getProfile("ap-southeast-1", "LTAIgYEAAiMej0WK",
                        "unN3GIXParljB7J7rPrxD3I47NHhtY");
                // add endpoint, no need to modify
                DefaultProfile.addEndpoint("ap-southeast-1", "ap-southeast-1",
                        "IDST", "imagesearch.ap-southeast-1.aliyuncs.com");

                DefaultAcsClient client = new DefaultAcsClient(profile);

                RoaSearchRequest req = new RoaSearchRequest();
                req.setApp("oas_search");
                req.setS(0);
                req.setN(30);

                req.setHttpContent(Base64.encode(imageByteArray, Base64.NO_CLOSE | Base64.NO_WRAP), null, FormatType.RAW);
                long begin = System.currentTimeMillis();

                HttpResponse resp = client.doAction(req);
                long end = System.currentTimeMillis();
                System.out.println("search time(ms):" + (end - begin));

                System.out.println(resp.getUrl());
                String cont = new String(resp.getHttpContent());

                System.out.println(cont);

                Gson gson = new Gson();
                Type type = new TypeToken<ImageSearchResponse>() {
                }.getType();

                return gson.fromJson(cont, type);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultImageSearchSubscriber(getBaseDiscoveryView()));

       /* getImageSearchUseCase.execute(
                GetImageSearchUseCase.initializeSearchRequestParam(imageByteArray),
                new DefaultImageSearchSubscriber(getBaseDiscoveryView())
        );*/

    }

    @Override
    public void detachView() {
        super.detachView();
        getProductUseCase.unsubscribe();
    }

}
