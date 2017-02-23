package com.tokopedia.seller.topads.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.topads.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsSearchGroupAdsNameUseCase extends UseCase<List<GroupAd>> {

    public static final String KEYWORD_NAME = "KEYWORD_NAME";
    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    public TopAdsSearchGroupAdsNameUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    @Override
    public Observable<List<GroupAd>> createObservable(RequestParams requestParams) {
        return topAdsGroupAdsRepository.searchGroupAds(requestParams.getString(KEYWORD_NAME, ""));
    }

    public static RequestParams createRequestParams(String keywordName){
        RequestParams params = RequestParams.create();
        params.putString(KEYWORD_NAME, keywordName);
        return params;
    }
}
