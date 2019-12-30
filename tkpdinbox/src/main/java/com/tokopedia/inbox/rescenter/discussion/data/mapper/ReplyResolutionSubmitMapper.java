package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit.Attachments;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit.Conversations;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit.NewReplyDiscussionSubmitEntity;
import com.tokopedia.inbox.rescenter.discussion.domain.model.NewReplyDiscussionModel;
import com.tokopedia.inbox.rescenter.discussion.domain.model.reply.ReplyAttachmentDomainData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.reply.ReplyDiscussionDomainData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by hangnadi on 7/5/17.
 */

public class ReplyResolutionSubmitMapper implements Func1<Response<TkpdResponse>, NewReplyDiscussionModel> {

    public ReplyResolutionSubmitMapper() {
    }

    @Override
    public NewReplyDiscussionModel call(Response<TkpdResponse> response) {
        Timber.d(response.body().getStrResponse());
        NewReplyDiscussionModel domainData = new NewReplyDiscussionModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                domainData.setSuccess(true);
                NewReplyDiscussionSubmitEntity entity = response.body()
                        .convertDataObj(NewReplyDiscussionSubmitEntity.class);
                domainData.setReplyDiscussionData(mappingDomainData(entity));
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

    private ReplyDiscussionDomainData mappingDomainData(NewReplyDiscussionSubmitEntity entity) {
        ReplyDiscussionDomainData data = new ReplyDiscussionDomainData();
        Conversations conversationLast = entity.getConversations().get(0);
        data.setActionBy(conversationLast.getActionBy());
        data.setConversationId(conversationLast.getId());
        data.setCreateTime(conversationLast.getCreateTime());
        data.setCreateTimeWib(conversationLast.getCreateTimeStr());
        data.setRemark(conversationLast.getMessage());
        data.setRemarkStr(conversationLast.getMessage());
        data.setReplyAttachmentDomainData(conversationLast.getAttachments() != null ? mappingAttachments(conversationLast.getAttachments()) : null);
        return data;
    }

    private List<ReplyAttachmentDomainData> mappingAttachments(List<Attachments> listAttachment) {
        List<ReplyAttachmentDomainData> list = new ArrayList<>();
        for(Attachments attachments : listAttachment) {
            ReplyAttachmentDomainData replyAttachmentDomainData = new ReplyAttachmentDomainData();
            replyAttachmentDomainData.setIsVideo(attachments.getIsVideo());
            replyAttachmentDomainData.setFullUrl(attachments.getFullUrl());
            replyAttachmentDomainData.setThumbnail(attachments.getThumbnail());
            replyAttachmentDomainData.setIsVideo(attachments.getIsVideo());
            list.add(replyAttachmentDomainData);
        }
        return list;
    }

}
