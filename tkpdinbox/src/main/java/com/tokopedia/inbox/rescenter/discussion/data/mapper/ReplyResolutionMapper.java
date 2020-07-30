package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation.NewReplyDiscussionEntity;
import com.tokopedia.inbox.rescenter.discussion.domain.model.NewReplyDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.reply.ReplyDiscussionDomainData;

import retrofit2.Response;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by hangnadi on 6/16/17.
 */

public class ReplyResolutionMapper implements Func1<Response<TkpdResponse>, NewReplyDiscussionModel> {

    public ReplyResolutionMapper() {
    }

    @Override
    public NewReplyDiscussionModel call(Response<TkpdResponse> response) {
        Timber.d(response.body().getStrResponse());
        NewReplyDiscussionModel domainData = new NewReplyDiscussionModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                domainData.setSuccess(true);
                NewReplyDiscussionEntity entity = response.body()
                        .convertDataObj(NewReplyDiscussionEntity.class);
                if (entity.getCacheKey() != null && !entity.getCacheKey().isEmpty()) {
                    domainData.setCacheKey(entity.getCacheKey());
                } else {
                    domainData.setReplyDiscussionData(mappingDomainData(entity));
                }
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    domainData.setSuccess(false);
                } else {
                    throw new MessageErrorException(response.body().getErrorMessageJoined());
                }
            }
            domainData.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return domainData;
    }

    private ReplyDiscussionDomainData mappingDomainData(NewReplyDiscussionEntity entity) {
        ReplyDiscussionDomainData data = new ReplyDiscussionDomainData();
        NewReplyDiscussionEntity.Conversations conversationLast = entity.getConversations().get(0);
        data.setActionBy(conversationLast.getActionBy());
        data.setConversationId(conversationLast.getId());
        data.setCreateTime(conversationLast.getCreateTime());
        data.setCreateTimeWib(conversationLast.getCreateTimeStr());
        data.setRemark(conversationLast.getMessage());
        data.setRemarkStr(conversationLast.getMessage());
        return data;
    }

}
