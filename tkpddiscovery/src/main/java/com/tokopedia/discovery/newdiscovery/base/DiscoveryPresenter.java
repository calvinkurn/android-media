package com.tokopedia.discovery.newdiscovery.base;

import android.util.Log;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.imagesearch.domain.usecase.NewImageSearchResponse;
import com.tokopedia.discovery.imagesearch.domain.usecase.SearchItemRequestLocal;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract.View;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

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

                IClientProfile profile = DefaultProfile.getProfile("ap-southeast-1", "LTAIUeEWSvia1KkW",
                        "eJLV3PJCCEn7sqf5vVrIzaESTfsNdm");
                // add endpoint, no need to modify
                DefaultProfile.addEndpoint("ap-southeast-1", "ap-southeast-1",
                        "ImageSearch", "imagesearch.ap-southeast-1.aliyuncs.com");

                IAcsClient client = new DefaultAcsClient(profile);

                SearchItemRequestLocal request = new SearchItemRequestLocal();
                request.setNum(100);
                request.setStart(0);
                request.setCatId("0");
                request.setInstanceName("productsearch01");
                request.setSearchPicture(imageByteArray);

                if (!request.buildPostContent()) {
                    System.out.println("build post content failed.");
                    return new NewImageSearchResponse();
                }

                NewImageSearchResponse response = client.getAcsResponse(request);

                Log.e("ImageSearch Res: ", response.toString());
                return response;
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
