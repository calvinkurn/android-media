package com.tokopedia.inbox.inboxchat.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.R;
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
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                MessageData data = response.body().convertDataObj(MessageData.class);
                return convertToDomain(data);
            }else {
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

    private InboxChatViewModel convertToDomain(MessageData data) {
        InboxChatViewModel inboxChatViewModel = new InboxChatViewModel();
        inboxChatViewModel.setMode(InboxChatViewModel.GET_CHAT_MODE);
        inboxChatViewModel.setHasNext(data.isHasNext());

        ArrayList<Visitable> list = new ArrayList<>();
        for (ListMessage item : data.getList()) {
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
            viewModel.setRole(item.getAttributes().getContact().getRole());
            list.add(viewModel);
        }
        inboxChatViewModel.setListReplies(list);
        inboxChatViewModel.setHasTimeMachine(data.getTimeMachineStatus() == 1);
        return inboxChatViewModel;
    }
}