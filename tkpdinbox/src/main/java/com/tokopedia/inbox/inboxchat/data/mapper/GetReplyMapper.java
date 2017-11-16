package com.tokopedia.inbox.inboxchat.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ListReply;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;

import java.util.ArrayList;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetReplyMapper implements Func1<Response<TkpdResponse>, ChatRoomViewModel> {

    @Override
    public ChatRoomViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                ReplyData data = response.body().convertDataObj(ReplyData.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }


    private ChatRoomViewModel mappingToDomain(ReplyData data) {

        ChatRoomViewModel chatRoomViewModel = new ChatRoomViewModel();

        ArrayList<Visitable> list = new ArrayList<>();

        for (ListReply item : data.getList()) {
            if (!item.isOpposite()) {
                MyChatViewModel temp = new MyChatViewModel();
                temp.setReplyId(item.getReplyId());
                temp.setSenderId(item.getSenderId());
                temp.setMsg(item.getMsg());
                temp.setReplyTime(item.getReplyTime());
                temp.setFraudStatus(item.getFraudStatus());
                temp.setReadTime(item.getReadTime());
                temp.setAttachmentId(item.getAttachmentId());
                temp.setOldMsgId(item.getOldMsgId());
                temp.setMsgId(item.getMsgId());
                temp.setRole(item.getRole());
                temp.setSenderName(item.getSenderName());
                temp.setHighlight(item.isHighlight());
                temp.setReadStatus(item.isMessageIsRead());
                temp.setOldMessageTitle(item.getOldMessageTitle());
                if (item.isHighlight()) {
                    temp.setSpanned(MethodChecker.fromHtml(item.getMsg()));
                }
                list.add(temp);
            } else {

                OppositeChatViewModel temp = new OppositeChatViewModel();
                temp.setReplyId(item.getReplyId());
                temp.setSenderId(item.getSenderId());
                temp.setMsg(item.getMsg());
                temp.setReplyTime(item.getReplyTime());
                temp.setFraudStatus(item.getFraudStatus());
                temp.setReadTime(item.getReadTime());
                temp.setAttachmentId(item.getAttachmentId());
                temp.setOldMsgId(item.getOldMsgId());
                temp.setMsgId(item.getMsgId());
                temp.setRole(item.getRole());
                temp.setSenderName(item.getSenderName());
                temp.setHighlight(item.isHighlight());
                if (item.isHighlight()) {
                    temp.setSpanned(MethodChecker.fromHtml(item.getMsg()));
                }
                temp.setOldMessageTitle(item.getOldMessageTitle());
                list.add(temp);
            }
        }

        chatRoomViewModel.setChatList(list);
        chatRoomViewModel.setHasNext(data.isHasNext());
        chatRoomViewModel.setTextAreaReply(data.getTextAreaReply());
        chatRoomViewModel.setHasTimeMachine(data.getTimeMachineStatus() == 1);
        return chatRoomViewModel;
    }
}