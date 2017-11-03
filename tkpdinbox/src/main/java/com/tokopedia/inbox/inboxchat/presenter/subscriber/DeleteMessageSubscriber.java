package com.tokopedia.inbox.inboxchat.presenter.subscriber;

import com.tokopedia.inbox.inboxchat.presenter.InboxChatContract;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by stevenfredian on 10/30/17.
 */

public class DeleteMessageSubscriber extends Subscriber<DeleteChatListViewModel> {

    private InboxChatPresenter presenter;
    private InboxChatContract.View view;

    public DeleteMessageSubscriber(InboxChatContract.View view, InboxChatPresenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(DeleteChatListViewModel listViewModel) {
        List<DeleteChatViewModel> list = listViewModel.getList();
        view.removeList(list);
    }

}

