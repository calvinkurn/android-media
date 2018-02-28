package com.tokopedia.tkpdstream.vote.domain.usecase;

import com.tokopedia.tkpdstream.vote.domain.source.GetVoteSource;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class GetVoteUseCase extends UseCase<VoteInfoViewModel>{

    GetVoteSource getVoteSource;

    @Inject
    public GetVoteUseCase(GetVoteSource getVoteSource) {
        this.getVoteSource = getVoteSource;
    }

    @Override
    public Observable<VoteInfoViewModel> createObservable(RequestParams requestParams) {
        return getVoteSource.getVoteInfoSource(requestParams.getParameters());
    }

    public RequestParams createParams() {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }
}
