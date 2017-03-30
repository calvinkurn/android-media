package com.tokopedia.inbox.rescenter.detailv2.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.ResolutionCenterActionMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.ActionDiscussionModel;

import rx.Observable;

/**
 * Created by hangnadi on 3/22/17.
 */

public class CloudActionResCenterDataStore {

    private final Context context;
    private final ResCenterActService resCenterActService;
    private final ResolutionCenterActionMapper resolutionCenterActionMapper;

    public CloudActionResCenterDataStore(Context context, ResCenterActService resCenterActService) {
        super();
        this.context = context;
        this.resCenterActService = resCenterActService;
        this.resolutionCenterActionMapper = new ResolutionCenterActionMapper();
    }

    public Observable<ResolutionActionDomainData> cancelResolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActService.getApi()
                .cancelResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> reportResolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActService.getApi()
                .reportResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> finishReturSolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActService.getApi()
                .finishResolutionReturn2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> acceptAdminSolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActService.getApi()
                .acceptAdminResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> acceptSolution(TKPDMapParam<String, Object> parameters) {
        return resCenterActService.getApi()
                .acceptResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> inputAddress(TKPDMapParam<String, Object> parameters) {
        return resCenterActService.getApi()
                .inputAddressResolution2(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }

    public Observable<ActionDiscussionModel> replyConversationValidation(
            TKPDMapParam<String, Object> parameters) {
        return resCenterActService.getApi()
                .replyConversationValidation(
                        AuthUtil.generateParamsNetwork2(context, parameters))
                .map(resolutionCenterActionMapper);
    }
}
