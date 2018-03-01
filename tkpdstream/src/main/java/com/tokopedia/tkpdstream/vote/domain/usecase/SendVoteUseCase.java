package com.tokopedia.tkpdstream.vote.domain.usecase;

import com.tokopedia.tkpdstream.vote.domain.source.VotingSource;
import com.tokopedia.tkpdstream.vote.view.model.VoteStatisticViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class SendVoteUseCase extends UseCase<VoteStatisticViewModel> {

    private static final String PARAM_OPTION_ID = "option_id";
    private static final String PARAM_POLL_ID = "poll_id";
    private VotingSource votingSource;

    @Inject
    public SendVoteUseCase(VotingSource votingSource) {
        this.votingSource = votingSource;
    }

    @Override
    public Observable<VoteStatisticViewModel> createObservable(RequestParams requestParams) {
        return votingSource.sendVote(requestParams.getString(PARAM_POLL_ID, ""),
                getRequestParamToSend(requestParams));
    }

    private HashMap<String, Object> getRequestParamToSend(RequestParams requestParams) {
        requestParams.getParameters().remove(PARAM_POLL_ID);
        return requestParams.getParameters();
    }

    public static RequestParams createParams(String pollId, String optionId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_OPTION_ID, optionId);
        requestParams.putString(PARAM_POLL_ID, pollId);
        return requestParams;
    }
}
