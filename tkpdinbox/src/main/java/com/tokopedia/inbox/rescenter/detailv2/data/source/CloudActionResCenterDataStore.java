package com.tokopedia.inbox.rescenter.detailv2.data.source;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.ResolutionCenterActionMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AcceptSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.EditAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.FinishResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.InputAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.GenerateHostV2Mapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.ReplyConversationValidationMapper;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;

import rx.Observable;

/**
 * Created by hangnadi on 3/22/17.
 */

public class CloudActionResCenterDataStore {

    private final Context context;
    private final ResCenterActApi resCenterActApi;
    private final ResolutionApi resolutionApi;
    private final ResolutionCenterActionMapper resolutionCenterActionMapper;
    private final ReplyConversationValidationMapper replyConversationValidationMapper;
    private final GenerateHostV2Mapper generateHostV2Mapper;

    public CloudActionResCenterDataStore(Context context, ResCenterActApi resCenterActApi, ResolutionApi resolutionApi) {
        super();
        this.context = context;
        this.resCenterActApi = resCenterActApi;
        this.resolutionCenterActionMapper = new ResolutionCenterActionMapper();
        this.replyConversationValidationMapper = new ReplyConversationValidationMapper();
        this.generateHostV2Mapper = new GenerateHostV2Mapper();
        this.resolutionApi = resolutionApi;
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

    public Observable<ResolutionActionDomainData> acceptSolution(RequestParams params) {
        return resolutionApi.acceptResolution(params.getString(AcceptSolutionUseCase.PARAM_RESOLUTION_ID, ""),
                params.getParameters())
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
        ).map(generateHostV2Mapper);
    }

    public Observable<ResolutionActionDomainData> finishResolution(RequestParams params) {
        return resolutionApi.finishResolution(
                params.getString(FinishResolutionUseCase.RESO_ID, ""),
                params.getParameters()).map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> cancelResolution(RequestParams params) {
        return resolutionApi.cancelResolution(
                params.getString(FinishResolutionUseCase.RESO_ID, ""),
                params.getParameters()).map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> askHelpResolutionV2(RequestParams params) {
        return resolutionApi.askHelpResolution(
                params.getString(FinishResolutionUseCase.RESO_ID, ""),
                params.getParameters()).map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> inputAddressV2(RequestParams params) {
        return resolutionApi.inputAddress(
                params.getString(InputAddressUseCase.PARAM_RESOLUTION_ID, ""),
                params.getParameters()).map(resolutionCenterActionMapper);
    }

    public Observable<ResolutionActionDomainData> editAddressV2(RequestParams params) {
        return resolutionApi.editAddress(
                params.getString(EditAddressUseCase.PARAM_RESOLUTION_ID, ""),
                params.getString(EditAddressUseCase.PARAM_CONVERSATION_ID, ""),
                params.getParameters())
                .map(resolutionCenterActionMapper);
    }
}
