package com.tokopedia.tkpdstream.vote.domain.source;

import com.tokopedia.tkpdstream.common.data.VoteApi;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;
import com.tokopedia.tkpdstream.vote.domain.mapper.GetVoteMapper;
import com.tokopedia.tkpdstream.vote.domain.mapper.VotingMapper;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VotingSource {

    VoteApi voteApi;
    VotingMapper votingMapper;

    @Inject
    public VotingSource(@StreamScope VoteApi voteApi, VotingMapper votingMapper) {
        this.voteApi = voteApi;
        this.votingMapper = votingMapper;
    }

    public Observable<VoteInfoViewModel> voting(HashMap<String, Object> parameters) {
        return voteApi.getVoteInfo(parameters).map(votingMapper);
    }
}
