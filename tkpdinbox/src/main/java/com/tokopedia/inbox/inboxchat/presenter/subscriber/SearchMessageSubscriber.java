package com.tokopedia.inbox.inboxchat.presenter.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatContract;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import rx.Subscriber;

/**
 * Created by stevenfredian on 10/19/17.
 */

public class SearchMessageSubscriber extends Subscriber<InboxChatViewModel> {

    private InboxChatPresenter presenter;
    private InboxChatContract.View view;

    public SearchMessageSubscriber(InboxChatContract.View view, InboxChatPresenter presenter) {
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
        view.showError(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxChatViewModel inboxChatViewModel) {
        presenter.setRequesting(false);
        view.finishSearch();
        view.setResultSearch(inboxChatViewModel);
        presenter.prepareNextPage(inboxChatViewModel.isHasNextReplies());
    }

}
