package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.entity.discovery.ImageSearchResponse;
import com.tokopedia.discovery.imagesearch.data.repository.ImageSearchRepository;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchResultModel;

import rx.Observable;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class GetImageSearchUseCase extends UseCase<ImageSearchResultModel> {


    private ImageSearchRepository imageSearchRepository;
    private Context context;


    public GetImageSearchUseCase(Context context, ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ImageSearchRepository imageSearchRepository) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.imageSearchRepository = imageSearchRepository;
    }

    public static RequestParams initializeSearchRequestParam(byte[] array) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject("imageByteArray", array);
        return requestParams;
    }


    @Override
    public Observable<ImageSearchResultModel> createObservable(RequestParams requestParams) {
        return imageSearchRepository.getImageSearchResults(requestParams);
    }
}
