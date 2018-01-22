package com.tokopedia.discovery.imagesearch.data.source;

import android.util.Base64;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.entity.discovery.ImageSearchResponse;
import com.tokopedia.discovery.imagesearch.data.mapper.ImageSearchResultMapper;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchResultModel;
import com.tokopedia.discovery.imagesearch.domain.usecase.RoaSearchRequest;

import java.lang.reflect.Type;

import rx.Observable;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class ImageSearchDataSource {

    private final ImageSearchResultMapper imageSearchResultMapper;

    public ImageSearchDataSource(ImageSearchResultMapper imageSearchResultMapper) {
        this.imageSearchResultMapper = imageSearchResultMapper;
    }


    public Observable<ImageSearchResultModel> getImageSearch(RequestParams param) {

        // TODO: 1/17/18 Get Results from SDK and


        /*SearchImageAsyncTask searchImageAsyncTask = new SearchImageAsyncTask(this);
            searchImageAsyncTask.execute(byteArray);*/

        return null;
    }
}
