package com.tokopedia.inbox.rescenter.detailv2.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.ResolutionCenterActionMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.ReplyConversationValidationMapper;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.ReplyDiscussionValidationUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;

import rx.Observable;

/**
 * Created by hangnadi on 3/22/17.
 */

public class CloudActionResCenterDataStore {

    private final Context context;
    private final ResCenterActApi resCenterActApi;
    private final ResolutionCenterActionMapper resolutionCenterActionMapper;
    private final ReplyConversationValidationMapper replyConversationValidationMapper;
    private final GenerateHostMapper generateHostMapper;

    public CloudActionResCenterDataStore(Context context, ResCenterActApi resCenterActApi) {
        super();
        this.context = context;
        this.resCenterActApi = resCenterActApi;
        this.resolutionCenterActionMapper = new ResolutionCenterActionMapper();
        this.replyConversationValidationMapper = new ReplyConversationValidationMapper();
        this.generateHostMapper = new GenerateHostMapper();
    }

    public Observable<ResolutionActionDomainData> cancelResolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActApi.cancelResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> reportResolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActApi.reportResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> finishReturSolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActApi.finishResolutionReturn2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> acceptAdminSolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActApi.acceptAdminResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> acceptSolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActApi.acceptResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> inputAddress(TKPDMapParam<String, Object> parameters) {
        return resCenterActApi.inputAddressResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> editAddress(TKPDMapParam<String, Object> parameters) {
        return resCenterActApi.editAddressResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ReplyDiscussionValidationModel> replyConversationValidation(
            TKPDMapParam<String, Object> parameters) {
        return resCenterActApi.replyConversationValidation2(
                        AuthUtil.generateParamsNetwork2(context, parameters))
                .map(replyConversationValidationMapper);
    }

    public Observable<GenerateHostModel> generateTokenHost(TKPDMapParam<String, Object> parameters) {
        return resCenterActApi.generateTokenHost(
                AuthUtil.generateParamsNetwork2(context, parameters)
        ).map(generateHostMapper);
    }
}
