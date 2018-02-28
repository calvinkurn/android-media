package com.tokopedia.tkpdstream.vote.domain.source;

import com.tokopedia.tkpdstream.common.data.VoteApi;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;
import com.tokopedia.tkpdstream.vote.domain.mapper.GetVoteMapper;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class GetVoteSource {

    VoteApi voteApi;
    GetVoteMapper getVoteMapper;

    @Inject
    public GetVoteSource(@StreamScope VoteApi voteApi, GetVoteMapper getVoteMapper) {
        this.voteApi = voteApi;
        this.getVoteMapper = getVoteMapper;
    }

    public Observable<VoteInfoViewModel> getVoteInfoSource(HashMap<String, Object> parameters) {
        return null;
    }
}
