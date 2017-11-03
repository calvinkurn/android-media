package com.tokopedia.inbox.inboxchat.presenter.subscriber;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.inboxchat.domain.model.search.Datum;
import com.tokopedia.inbox.inboxchat.domain.model.search.SearchedMessage;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatContract;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import java.util.ArrayList;

import rx.Subscriber;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.STATE_CHAT_READ;

/**
 * Created by stevenfredian on 10/19/17.
 */

public class SearchMessageSubscriber extends Subscriber<InboxChatViewModel>{

    private InboxChatPresenter presenter;
    private InboxChatContract.View view;

    public SearchMessageSubscriber(InboxChatContract.View view, InboxChatPresenter presenter){
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        presenter.setRequesting(false);
        view.finishSearch();
        view.showError("Pencarian Gagal");
    }

    @Override
    public void onNext(InboxChatViewModel inboxChatViewModel) {
        view.setResultSearch(inboxChatViewModel);
        presenter.prepareNextPage(inboxChatViewModel.isHasNextReplies());
        presenter.setRequesting(false);
        view.finishSearch();
    }

}
