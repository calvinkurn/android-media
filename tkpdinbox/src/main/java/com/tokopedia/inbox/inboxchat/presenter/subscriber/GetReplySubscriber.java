package com.tokopedia.inbox.inboxchat.presenter.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.inboxchat.domain.model.GetReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ListReply;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;

import java.util.ArrayList;

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

//        if (view.getAdapter().getList().size() == 0) {
//            view.finishLoading();
//            view.showEmptyState(error);
//        } else {
//            view.setRetry(error,
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            getMessageDetail();
//                        }
//                    });
//        }
        view.showError("Gagal");
    }

    @Override
    public void onNext(ChatRoomViewModel model) {
        view.setViewEnabled(true);
//
//                if (pagingHandler.getPage() == 1) {
//                    viewListener.getAdapter().clearData();
//                    cacheInteractor.setInboxMessageDetailCache(viewListener.getArguments().getString(PARAM_MESSAGE_ID), result);
//                }
//
        view.setTextAreaReply(model.getTextAreaReply() == 1);
        view.setResult(model);
        view.finishLoading();
        presenter.finishRequest();
    }
}
