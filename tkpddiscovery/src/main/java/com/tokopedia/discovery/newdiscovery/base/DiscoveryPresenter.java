package com.tokopedia.discovery.newdiscovery.base;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tkpd.library.utils.CommonUtils;
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

    private final String REGION_ID = "ap-southeast-1";
    private final String ACCESS_KEY_ID = "LTAIUeEWSvia1KkW";
    private final String SECRET_KEY = "eJLV3PJCCEn7sqf5vVrIzaESTfsNdm";
    private final String END_POINT_NAME = "ap-southeast-1";
    private final String PRODUCT = "ImageSearch";
    private final String IMAGE_SEARCH_ALIYUN_DOMAIN = "imagesearch.ap-southeast-1.aliyuncs.com";
    private final String IMAGE_SEARCH_INSTANCE = "productsearch01";

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

                IClientProfile profile = DefaultProfile.getProfile(REGION_ID, ACCESS_KEY_ID,
                        SECRET_KEY);
                DefaultProfile.addEndpoint(END_POINT_NAME, REGION_ID,
                        PRODUCT, IMAGE_SEARCH_ALIYUN_DOMAIN);

                IAcsClient client = new DefaultAcsClient(profile);

                SearchItemRequestLocal request = new SearchItemRequestLocal();
                request.setInstanceName(IMAGE_SEARCH_INSTANCE);
                request.setSearchPicture(imageByteArray);

                if (!request.buildPostContent()) {
                    CommonUtils.dumper("Image Search build post content failed.");
                    return new NewImageSearchResponse();
                }

                NewImageSearchResponse response = null;
                try {
                    response = client.getAcsResponse(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return response;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultImageSearchSubscriber(getBaseDiscoveryView()));

    }

    @Override
    public void detachView() {
        super.detachView();
        getProductUseCase.unsubscribe();
    }

}
