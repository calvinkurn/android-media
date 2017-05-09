package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit.Attachment;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit.By_;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit.ConversationLast;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit.SubmitImageEntity;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.AttachmentData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ByData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 4/3/17.
 */

public class SubmitImageMapper implements Func1<Response<TkpdResponse>, ReplySubmitModel> {
    @Override
    public ReplySubmitModel call(Response<TkpdResponse> response) {
        ReplySubmitModel domainData = new ReplySubmitModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                SubmitImageEntity entity = response.body().convertDataObj(SubmitImageEntity.class);
                domainData.setSuccess(true);
                domainData.setReplySubmitData(mappingEntityDomain(entity.getConversationLast().get(0)));
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

    private ReplySubmitData mappingEntityDomain(ConversationLast conversationLast) {
        ReplySubmitData replySubmitData = new ReplySubmitData();
        replySubmitData.setProblemType(conversationLast.getProblemType());
        replySubmitData.setBy(mappingByData(conversationLast.getBy()));
        replySubmitData.setAction(conversationLast.getAction());
        replySubmitData.setActionBy(conversationLast.getActionBy());
        replySubmitData.setAttachment(mappingAttachment(conversationLast.getAttachment()));
        replySubmitData.setChatFlag(conversationLast.getChatFlag());
        replySubmitData.setConversationId(conversationLast.getConversationId());
        replySubmitData.setCreateTime(conversationLast.getCreateTime());
        replySubmitData.setCreateTimeWib(conversationLast.getCreateTimeWib());
        replySubmitData.setIsAppeal(conversationLast.getIsAppeal());
        replySubmitData.setNotificationFlag(conversationLast.getNotificationFlag());
        replySubmitData.setRemark(conversationLast.getRemark());
        replySubmitData.setRemarkStr(conversationLast.getRemarkStr());
        replySubmitData.setResetFlag(conversationLast.getResetFlag());
        replySubmitData.setSolutionFlag(conversationLast.getSolutionFlag());
        replySubmitData.setSystemFlag(conversationLast.getSystemFlag());
        replySubmitData.setTimeAgo(conversationLast.getTimeAgo());
        replySubmitData.setUserImg(conversationLast.getUserImg());
        replySubmitData.setUserLabel(conversationLast.getUserLabel());
        replySubmitData.setUserLabelId(conversationLast.getUserLabelId());
        replySubmitData.setUserName(conversationLast.getUserName());
        replySubmitData.setUserUrl(conversationLast.getUserUrl());
        return replySubmitData;
    }

    private List<AttachmentData> mappingAttachment(List<Attachment> listAttachment) {
        List<AttachmentData> listData = new ArrayList<>();

        for (Attachment attachment : listAttachment) {
            AttachmentData data = new AttachmentData();
            data.setAttId(attachment.getAttId());
            data.setFileUrl(attachment.getFileUrl());
            data.setIsVideo(attachment.getIsVideo());
            data.setRealFileUrl(attachment.getRealFileUrl());
            listData.add(data);
        }

        return listData;
    }

    private ByData mappingByData(By_ by) {
        ByData byData = new ByData();
        byData.setIsReceiver(by.getIsReceiver());
        byData.setIsSender(by.getIsSender());
        return byData;
    }
}
