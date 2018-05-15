package com.tokopedia.inbox.inboxchat.presenter.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;

import rx.Subscriber;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class GetReplySubscriber extends Subscriber<ChatRoomViewModel> {

    private final ChatRoomPresenter presenter;
    private ChatRoomContract.View view;


    public GetReplySubscriber(ChatRoomContract.View view, ChatRoomPresenter chatRoomPresenter) {
        this.view = view;
        presenter = chatRoomPresenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.setViewEnabled(true);
        view.showError(ErrorHandler.getErrorMessage(e));
        view.hideMainLoading();
        presenter.finishRequest();
    }

    @Override
    public void onNext(ChatRoomViewModel model) {
        view.setViewEnabled(true);
        view.setResult(model);
        presenter.finishRequest();
    }
}
