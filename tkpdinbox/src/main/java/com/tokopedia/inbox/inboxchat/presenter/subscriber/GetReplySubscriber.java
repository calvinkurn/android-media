package com.tokopedia.inbox.inboxchat.presenter.subscriber;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.inboxchat.domain.model.GetReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ListReply;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class GetReplySubscriber extends Subscriber<ReplyData> {

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
    }

    @Override
    public void onNext(ReplyData model) {
        view.setViewEnabled(true);
//
//                if (pagingHandler.getPage() == 1) {
//                    viewListener.getAdapter().clearData();
//                    cacheInteractor.setInboxMessageDetailCache(viewListener.getArguments().getString(PARAM_MESSAGE_ID), result);
//                }
//
        view.setTextAreaReply(model.getTextAreaReply() == 1);
        presenter.setResult(convertToViewModel(model));
        presenter.finishLoading();
        presenter.finishRequest(convertToViewModel(model).getList().size() - 1);
    }

    private GetReplyViewModel convertToViewModel(ReplyData model) {

        GetReplyViewModel domainData = new GetReplyViewModel();
        ArrayList<Visitable> list = new ArrayList<>();
        for (ListReply item : model.getList()) {
            if (item.getSenderId().equals(SessionHandler.getLoginID(view.getContext()))) {
                list.add(new MyChatViewModel(
                        item.getReplyId(),
                        item.getSenderId(),
                        item.getMsg(),
                        item.getReplyTime(),
                        item.getFraudStatus(),
                        item.getReadTime(),
                        item.getAttachmentId(),
                        item.getOldMsgId()));
            } else {
                list.add(new OppositeChatViewModel(
                        item.getReplyId(),
                        item.getSenderId(),
                        item.getMsg(),
                        item.getReplyTime(),
                        item.getFraudStatus(),
                        item.getReadTime(),
                        item.getAttachmentId(),
                        item.getOldMsgId()));
            }
        }

        domainData.setList(list);
        domainData.setTextAreaReply(model.getTextAreaReply());
        domainData.setHasNext(model.isHasNext());
        return domainData;
    }
}
