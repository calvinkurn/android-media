package com.tokopedia.inbox.inboxchat.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.inboxchat.domain.model.search.Datum;
import com.tokopedia.inbox.inboxchat.domain.model.search.SearchedMessage;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import java.util.ArrayList;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.STATE_CHAT_READ;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class SearchChatMapper implements Func1<Response<TkpdResponse>, InboxChatViewModel> {
    @Override
    public InboxChatViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            SearchedMessage data = response.body().convertDataObj(SearchedMessage.class);

            InboxChatViewModel inboxChatViewModel = new InboxChatViewModel();
            inboxChatViewModel.setMode(InboxChatViewModel.SEARCH_CHAT_MODE);
            ArrayList<ChatListViewModel> list = new ArrayList<>();

            int index = 0;
            for(Datum item : data.getContacts().getData()){
                ChatListViewModel viewModel = new ChatListViewModel();
                viewModel.setId(String.valueOf(item.getMsgId()));
                viewModel.setName(item.getOppositeName());
                viewModel.setSpan(MethodChecker.fromHtml(item.getOppositeName()));
                viewModel.setMessage(item.getLastMessage());
                viewModel.setTime(item.getCreateTime());
                viewModel.setUnreadCounter(0);
                viewModel.setReadStatus(STATE_CHAT_READ);
                viewModel.setSpanMode(ChatListViewModel.SPANNED_CONTACT);
                list.add(viewModel);
                if(index==0){
                    viewModel.setSectionSize(data.getContacts().getData().size());
                }
                index++;
            }

            index = 0;
            for(Datum item : data.getReplies().getData()){
                ChatListViewModel viewModel = new ChatListViewModel();
                viewModel.setId(String.valueOf(item.getMsgId()));
                viewModel.setName(item.getOppositeName());
                viewModel.setSpan(MethodChecker.fromHtml(item.getLastMessage()));
                viewModel.setMessage(item.getLastMessage());
                viewModel.setTime(item.getCreateTime());
                viewModel.setUnreadCounter(0);
                viewModel.setReadStatus(STATE_CHAT_READ);
                viewModel.setSpanMode(ChatListViewModel.SPANNED_MESSAGE);
                list.add(viewModel);
                if(index==0){
                    viewModel.setSectionSize(data.getReplies().getData().size());
                }
                index++;
            }
            inboxChatViewModel.setList(list);
            return inboxChatViewModel;
        } else {
            return new InboxChatViewModel();
        }
    }
}
