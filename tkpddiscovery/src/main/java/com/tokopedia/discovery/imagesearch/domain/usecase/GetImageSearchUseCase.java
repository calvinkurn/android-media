package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;
import android.text.TextUtils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.imagesearch.data.repository.ImageSearchRepository;
import com.tokopedia.discovery.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchItemRequest;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchItemResponse;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class GetImageSearchUseCase<T> extends UseCase<SearchResultModel> {


    private ImageSearchRepository imageSearchRepository;
    private Context context;

    private final String REGION_ID = "ap-southeast-1";
    private final String ACCESS_KEY_ID = AuthUtil.KEY.ALIYUN_ACCESS_KEY_ID;
    private final String SECRET_KEY = AuthUtil.KEY.ALIYUN_SECRET_KEY;
    private final String END_POINT_NAME = "ap-southeast-1";
    private final String PRODUCT = "ImageSearch";
    private final String IMAGE_SEARCH_ALIYUN_DOMAIN = "imagesearch.ap-southeast-1.aliyuncs.com";
    private final String IMAGE_SEARCH_INSTANCE = "productsearch01";

    public GetImageSearchUseCase(Context context, ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ImageSearchRepository imageSearchRepository) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.imageSearchRepository = imageSearchRepository;
    }

    public static RequestParams initializeSearchRequestParam(SearchParameter imageSearchProductParameter) {

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BrowseApi.SOURCE, !TextUtils.isEmpty(
                imageSearchProductParameter.getSource()) ? imageSearchProductParameter.getSource() : BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.ROWS, String.valueOf(imageSearchProductParameter.getStartRow()));
        requestParams.putString(BrowseApi.ID, imageSearchProductParameter.getQueryKey());
        return requestParams;
    }

    @Override
    public Observable<SearchResultModel> createObservable(RequestParams requestParams) {
        return imageSearchRepository.getImageSearchResults(requestParams.getParameters());
    }

    public void requestImageSearch(final byte[] imageByteArray, DefaultImageSearchSubscriber subscriber) {
        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                IClientProfile profile = DefaultProfile.getProfile(REGION_ID, ACCESS_KEY_ID,
                        SECRET_KEY);
                DefaultProfile.addEndpoint(END_POINT_NAME, REGION_ID,
                        PRODUCT, IMAGE_SEARCH_ALIYUN_DOMAIN);

                IAcsClient client = new DefaultAcsClient(profile);

                ImageSearchItemRequest request = new ImageSearchItemRequest();
                request.setInstanceName(IMAGE_SEARCH_INSTANCE);
                request.setSearchPicture(imageByteArray);

                if (!request.buildPostContent()) {
                    CommonUtils.dumper("Image Search build post content failed.");
                    return new ImageSearchItemResponse();
                }

                ImageSearchItemResponse response = null;
                try {
                    response = client.getAcsResponse(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return response;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
