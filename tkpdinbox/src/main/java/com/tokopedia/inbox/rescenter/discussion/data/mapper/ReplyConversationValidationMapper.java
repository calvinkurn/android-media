package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation.By;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation.ConversationLast;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation.ReplyDiscussionValidationEntity;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation.ReplyDiscussionValidationImageEntity;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ByData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;

import org.json.JSONException;

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

                try {
                    if(response.body().getJsonData().getString("post_key")!= null) {
                        ReplyDiscussionValidationImageEntity entity = response.body()
                                .convertDataObj(ReplyDiscussionValidationImageEntity.class);
                        domainData.setReplyDiscussionData(mappingEntityImageDomain(entity));
                    }else {
                        ReplyDiscussionValidationEntity entity = response.body()
                                .convertDataObj(ReplyDiscussionValidationEntity.class);
                        domainData.setReplyDiscussionData(mappingEntityDomain(entity));
                    }

                } catch (JSONException e) {
                    ReplyDiscussionValidationEntity entity = response.body()
                            .convertDataObj(ReplyDiscussionValidationEntity.class);
                    domainData.setReplyDiscussionData(mappingEntityDomain(entity));
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

    private ReplyDiscussionData mappingEntityImageDomain(ReplyDiscussionValidationImageEntity entity) {
        ReplyDiscussionData data = new ReplyDiscussionData();
        data.setPostKey(entity.getPostKey());
        return data;
    }

    private ReplyDiscussionData mappingEntityDomain(ReplyDiscussionValidationEntity entity) {
        ReplyDiscussionData data = new ReplyDiscussionData();

        if (entity.getConversationLast() != null && entity.getConversationLast().size() != 0) {
            ConversationLast conversationLast = entity.getConversationLast().get(0);
            data.setAction(conversationLast.getAction());
            data.setActionBy(conversationLast.getActionBy());
            data.setBy(mappingBy(conversationLast.getBy()));
            data.setChatFlag(conversationLast.getChatFlag());
            data.setConversationId(conversationLast.getConversationId());
            data.setCreateTime(conversationLast.getCreateTime());
            data.setCreateTimeWib(conversationLast.getCreateTimeWib());
            data.setIsAppeal(conversationLast.getIsAppeal());
            data.setNotificationFlag(conversationLast.getNotificationFlag());
            data.setProblemType(conversationLast.getProblemType());
            data.setRemark(conversationLast.getRemark());
            data.setRemarkStr(conversationLast.getRemarkStr());
            data.setResetFlag(conversationLast.getResetFlag());
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
