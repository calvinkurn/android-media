package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ReplyReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReplyReviewDomain;

import rx.Observable;

/**
 * @author by nisie on 9/28/17.
 */

public class CloudReplyReviewDataSource {
    private final ReputationService reputationService;
    private final ReplyReviewMapper replyReviewMapper;

    public CloudReplyReviewDataSource(ReputationService reputationService,
                                      ReplyReviewMapper replyReviewMapper) {
        this.reputationService = reputationService;
        this.replyReviewMapper = replyReviewMapper;
    }

    public Observable<SendReplyReviewDomain> insertReviewResponse(RequestParams requestParams) {
        return reputationService.getApi()
                .insertReviewResponse(AuthUtil.generateParamsNetwork2(MainApplication
                                .getAppContext(),
                        requestParams
                        .getParameters()))
                .map(replyReviewMapper);
    }
}
