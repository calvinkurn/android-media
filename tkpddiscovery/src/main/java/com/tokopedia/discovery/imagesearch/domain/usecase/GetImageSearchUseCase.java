package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.imagesearch.data.repository.ImageSearchRepository;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import rx.Observable;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class GetImageSearchUseCase extends UseCase<SearchResultModel> {


    private ImageSearchRepository imageSearchRepository;
    private Context context;


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
}
