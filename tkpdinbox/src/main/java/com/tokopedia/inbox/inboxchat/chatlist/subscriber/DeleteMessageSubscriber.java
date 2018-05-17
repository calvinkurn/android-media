package com.tokopedia.inbox.inboxchat.chatlist.subscriber;

import android.util.Pair;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.inboxchat.chatlist.listener.InboxChatContract;
import com.tokopedia.inbox.inboxchat.chatlist.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.DeleteChatListViewModel;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.DeleteChatViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by stevenfredian on 10/30/17.
 */

public class DeleteMessageSubscriber extends Subscriber<DeleteChatListViewModel> {

    private InboxChatPresenter presenter;
    private InboxChatContract.View view;
    private List<Pair> originList;

    public DeleteMessageSubscriber(List<Pair> listMove, InboxChatContract.View view, InboxChatPresenter presenter) {
        originList = listMove;
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorDeleteMessage(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(DeleteChatListViewModel listViewModel) {
        List<DeleteChatViewModel> list = listViewModel.getList();
        view.removeList(originList, list);
    }

}

