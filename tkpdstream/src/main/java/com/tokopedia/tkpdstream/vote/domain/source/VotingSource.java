package com.tokopedia.tkpdstream.vote.domain.source;

import com.tokopedia.tkpdstream.common.data.VoteApi;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;
import com.tokopedia.tkpdstream.vote.domain.mapper.SendVoteMapper;
import com.tokopedia.tkpdstream.vote.view.model.VoteStatisticViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VotingSource {

    private VoteApi voteApi;
    private SendVoteMapper votingMapper;

    @Inject
    public VotingSource(@StreamScope VoteApi voteApi, SendVoteMapper votingMapper) {
        this.voteApi = voteApi;
        this.votingMapper = votingMapper;
    }

    public Observable<VoteStatisticViewModel> sendVote(String pollId,
                                                       HashMap<String, Object> parameters) {
        return voteApi.sendVote(pollId, parameters).map(votingMapper);
    }
}
