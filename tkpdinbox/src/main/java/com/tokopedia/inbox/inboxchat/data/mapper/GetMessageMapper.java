package com.tokopedia.inbox.inboxchat.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.inboxchat.domain.model.message.ListMessage;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import java.util.ArrayList;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetMessageMapper implements Func1<Response<TkpdResponse>, InboxChatViewModel> {

    private static final String ERROR = "error";
    private static final String ERROR_DESCRIPTION = "error_description";

    @Override
    public InboxChatViewModel call(Response<TkpdResponse> response) {
        if(response.isSuccessful()){
            MessageData data = response.body().convertDataObj(MessageData.class);
            InboxChatViewModel inboxChatViewModel = new InboxChatViewModel();
            inboxChatViewModel.setMode(InboxChatViewModel.GET_CHAT_MODE);

            ArrayList<ChatListViewModel> list = new ArrayList<>();
            for(ListMessage item : data.getList()){
                ChatListViewModel viewModel = new ChatListViewModel();
                viewModel.setId(String.valueOf(item.getMsgId()));
                viewModel.setSenderId(String.valueOf(item.getAttributes().getContact().getId()));
                viewModel.setName(item.getAttributes().getContact().getAttributes().getName());
                viewModel.setImage(item.getAttributes().getContact().getAttributes().getThumbnail());
                viewModel.setLabel(item.getAttributes().getContact().getAttributes().getTag());
                viewModel.setMessage(item.getAttributes().getLastReplyMsg());
                viewModel.setTime(item.getAttributes().getLastReplyTime());
                viewModel.setReadStatus(item.getAttributes().getReadStatus());
                viewModel.setUnreadCounter(item.getAttributes().getUnreads());
                list.add(viewModel);
            }
            inboxChatViewModel.setList(list);
            return inboxChatViewModel;
        }else {
            return null;
        }
    }
}