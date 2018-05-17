package com.tokopedia.inbox.inboxchat.chatlist.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.inboxchat.chatlist.listener.InboxChatContract;
import com.tokopedia.inbox.inboxchat.chatlist.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.InboxChatViewModel;

import rx.Subscriber;

/**
 * Created by stevenfredian on 10/19/17.
 */

public class GetMessageSubscriber extends Subscriber<InboxChatViewModel>{

    private InboxChatPresenter presenter;
    private InboxChatContract.View view;

    public GetMessageSubscriber(InboxChatContract.View view, InboxChatPresenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        presenter.setRequesting(false);
        presenter.setError(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxChatViewModel messageData) {
        presenter.setRequesting(false);
        view.enableActions();
//                if (pagingHandler.getPage() == 1 && !isFilterUsed()) {
//                    cacheInteractor.setInboxMessageCache(viewListener.getArguments().getString(PARAM_NAV), result);
//                }
//        if (view.getRefreshHandler().isRefreshing()) {
//            view.getAdapter().getList().clear();
//            view.getAdapter().clearSelection();
//        }
        view.finishLoading();

        view.setResultFetch(messageData);
        presenter.prepareNextPage(messageData.isHasNext());
        view.saveResult();

        view.setMustRefresh(false);
    }
}
