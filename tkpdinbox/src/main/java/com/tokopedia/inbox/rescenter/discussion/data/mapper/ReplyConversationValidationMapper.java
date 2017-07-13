package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation.By;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation.ConversationLast;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation.ReplyDiscussionValidationEntity;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation.ReplyDiscussionValidationImageEntity;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ByData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.reply.ReplyDiscussionDomainData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyConversationValidationMapper implements Func1<Response<TkpdResponse>, ReplyDiscussionValidationModel> {

    @Override
    public ReplyDiscussionValidationModel call(Response<TkpdResponse> response) {
        ReplyDiscussionValidationModel domainData = new ReplyDiscussionValidationModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                domainData.setSuccess(true);

                ReplyDiscussionValidationImageEntity entity = response.body().convertDataObj(ReplyDiscussionValidationImageEntity.class);
                if (entity.getPostKey() != null) {
                    domainData.setReplyDiscussionData(mappingImageEntityDomain(entity));
                } else {
                    ReplyDiscussionValidationEntity replyDiscussionValidationEntity = response.body().convertDataObj(ReplyDiscussionValidationEntity.class);
                    domainData.setReplyDiscussionData(mappingEntityDomain(replyDiscussionValidationEntity));
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

    private ReplyDiscussionDomainData mappingImageEntityDomain(ReplyDiscussionValidationImageEntity entity) {
        ReplyDiscussionDomainData data = new ReplyDiscussionDomainData();
        data.setPostKey(entity.getPostKey());
        return data;
    }

    private ReplyDiscussionDomainData mappingEntityDomain(ReplyDiscussionValidationEntity entity) {
        ReplyDiscussionDomainData data = new ReplyDiscussionDomainData();

        if (entity.getConversationLast() != null && entity.getConversationLast().size() != 0) {
            ConversationLast conversationLast = entity.getConversationLast().get(0);
            data.setActionBy(conversationLast.getActionBy());
            data.setBy(mappingBy(conversationLast.getBy()));
            data.setConversationId(conversationLast.getConversationId());
            data.setCreateTime(conversationLast.getCreateTime());
            data.setCreateTimeWib(conversationLast.getCreateTimeWib());
            data.setRemark(conversationLast.getRemark());
            data.setRemarkStr(conversationLast.getRemarkStr());
        }
        return data;
    }

    private ByData mappingBy(By by) {
        ByData data = new ByData();
        data.setIsReceiver(by.getIsReceiver());
        data.setIsSender(by.getIsSender());
        return data;
    }

}
