package com.tokopedia.inbox.inboxchat.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.model.search.RepliesContent;
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
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                SearchedMessage data = response.body().convertDataObj(SearchedMessage.class);
                return convertToDomain(data);
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

    private InboxChatViewModel convertToDomain(SearchedMessage data) {
        InboxChatViewModel inboxChatViewModel = new InboxChatViewModel();
        inboxChatViewModel.setMode(InboxChatViewModel.SEARCH_CHAT_MODE);

        inboxChatViewModel = prepareContact(inboxChatViewModel, data);
        inboxChatViewModel = prepareReplies(inboxChatViewModel, data);

        return inboxChatViewModel;
    }

    private InboxChatViewModel prepareContact(InboxChatViewModel inboxChatViewModel, SearchedMessage data) {
        if (data.getContacts() != null && data.getContacts().getData() != null) {
            int index = 0;

            inboxChatViewModel.setHasNextContacts(data.getContacts().isHasNext());
            inboxChatViewModel.setContactSize(data.getContacts().getData().size());

            ArrayList<Visitable> listContact = new ArrayList<>();

            for (RepliesContent item : data.getContacts().getData()) {
                ChatListViewModel viewModel = new ChatListViewModel();
                viewModel.setId(String.valueOf(item.getMsgId()));
                viewModel.setName(item.getContact().getAttributes().getName());
                viewModel.setSpan(MethodChecker.fromHtml(item.getContact().getAttributes().getName()));
                viewModel.setImage(item.getContact().getAttributes().getThumbnail());
                viewModel.setMessage(item.getLastMessage());
                viewModel.setTime(item.getCreateTime());
                viewModel.setUnreadCounter(0);
                viewModel.setReadStatus(STATE_CHAT_READ);
                viewModel.setLabel(getRole(item.getOppositeType()));
                viewModel.setSpanMode(ChatListViewModel.SPANNED_CONTACT);
                listContact.add(viewModel);
                if (index == 0) {
                    viewModel.setSectionSize(data.getContacts().getData().size());
                }
                index++;
            }

            inboxChatViewModel.setListContact(listContact);
        }
        return inboxChatViewModel;
    }

    private InboxChatViewModel prepareReplies(InboxChatViewModel inboxChatViewModel, SearchedMessage data) {
        if (data.getReplies() != null && data.getReplies().getData() != null) {
            int index = 0;

            inboxChatViewModel.setHasNextReplies(data.getReplies().isHasNext());
            inboxChatViewModel.setChatSize(data.getReplies().getData().size());


            ArrayList<Visitable> listReplies = new ArrayList<>();

            for (RepliesContent item : data.getReplies().getData()) {
                ChatListViewModel viewModel = new ChatListViewModel();
                viewModel.setId(String.valueOf(item.getMsgId()));
                viewModel.setName(item.getContact().getAttributes().getName());
                viewModel.setSpan(MethodChecker.fromHtml(item.getLastMessage()));
                viewModel.setImage(item.getContact().getAttributes().getThumbnail());
                viewModel.setMessage(item.getLastMessage());
                viewModel.setTime(item.getCreateTime());
                viewModel.setUnreadCounter(0);
                viewModel.setReadStatus(STATE_CHAT_READ);
                viewModel.setLabel(getRole(item.getOppositeType()));
                viewModel.setSpanMode(ChatListViewModel.SPANNED_MESSAGE);
                viewModel.setRole("user");
                listReplies.add(viewModel);
                if (index == 0) {
                    viewModel.setSectionSize(data.getReplies().getData().size());
                }
                index++;
            }
            inboxChatViewModel.setListReplies(listReplies);
        }
        return inboxChatViewModel;
    }

    private String getRole(int oppositeType) {
        switch (oppositeType) {
            case 1:
                return "Pengguna";
            case 2:
                return "Penjual";
        }
        return null;
    }
}
