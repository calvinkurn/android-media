package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 10/23/17.
 */

public class TopAdsGetSuggestionUseCase extends UseCase<GetSuggestionResponse> {
    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    @Inject
    public TopAdsGetSuggestionUseCase(ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread,
                                        TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    @Override
    public Observable<GetSuggestionResponse> createObservable(RequestParams requestParams) {
        return topAdsGroupAdsRepository.getSuggestion((GetSuggestionBody) requestParams.getObject(TopAdsNetworkConstant.PARAM_SUGGESTION));
    }

    public static RequestParams createRequestParams(GetSuggestionBody getSuggestionBody){
        RequestParams params = RequestParams.create();
        params.putObject(TopAdsNetworkConstant.PARAM_SUGGESTION, getSuggestionBody);
        return params;
    }
}
