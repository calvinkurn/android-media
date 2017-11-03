package com.tokopedia.inbox.inboxchat.data.mapper;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.inboxchat.domain.model.ListReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ListReply;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatConversationViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;

import org.json.JSONException;
import org.json.JSONObject;

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
            ReplyData data = response.body().convertDataObj(ReplyData.class);

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
                    if(item.isHighlight()){
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
                    if(item.isHighlight()){
                        temp.setSpanned(MethodChecker.fromHtml(item.getMsg()));
                    }
                    list.add(temp);
                }
            }

            chatRoomViewModel.setChatList(list);
            chatRoomViewModel.setHasNext(data.isHasNext());
            chatRoomViewModel.setTextAreaReply(data.getTextAreaReply());
            chatRoomViewModel.setHasTimeMachine(data.getTimeMachineStatus() == 1);
            return chatRoomViewModel;
        } else {
            return null;
        }
    }
}