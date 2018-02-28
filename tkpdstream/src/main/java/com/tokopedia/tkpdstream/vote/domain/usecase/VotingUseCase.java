package com.tokopedia.tkpdstream.vote.domain.usecase;

import com.tokopedia.tkpdstream.vote.domain.source.GetVoteSource;
import com.tokopedia.tkpdstream.vote.domain.source.VotingSource;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VotingUseCase extends UseCase<VoteInfoViewModel>{

    VotingSource votingSource;

    @Inject
    public VotingUseCase(VotingSource votingSource) {
        this.votingSource = votingSource;
    }

    @Override
    public Observable<VoteInfoViewModel> createObservable(RequestParams requestParams) {
        return votingSource.voting(requestParams.getParameters());
    }

    public RequestParams createParams() {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }
}
