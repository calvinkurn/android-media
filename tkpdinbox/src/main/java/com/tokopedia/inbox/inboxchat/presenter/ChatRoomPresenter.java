package com.tokopedia.inbox.inboxchat.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.model.GetReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxchat.presenter.subscriber.GetReplySubscriber;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_MESSAGE_ID;

/**
 * Created by stevenfredian on 9/26/17.
 */

public class ChatRoomPresenter extends BaseDaggerPresenter<ChatRoomContract.View> implements ChatRoomContract.Presenter{

    private final GetMessageListUseCase getMessageListUseCase;
    private final GetReplyListUseCase getReplyListUseCase;
    private final ReplyMessageUseCase replyMessageUseCase;
    public PagingHandler pagingHandler;
    boolean isRequesting;

    @Inject
    ChatRoomPresenter(GetMessageListUseCase getMessageListUseCase,
                       GetReplyListUseCase getReplyListUseCase,
                       ReplyMessageUseCase replyMessageUseCase){
        this.getMessageListUseCase = getMessageListUseCase;
        this.getReplyListUseCase = getReplyListUseCase;
        this.replyMessageUseCase = replyMessageUseCase;
    }

    @Override
    public void attachView(ChatRoomContract.View view) {
        super.attachView(view);
        isRequesting=false;
        this.pagingHandler = new PagingHandler();
    }

    public void onGoToProfile(String s) {

    }

    public void onLoadMore() {
        if (!isRequesting) {
            pagingHandler.nextPage();
            getReply();
        }else{
            getView().finishLoading();
        }
    }


    public void getReply() {
        isRequesting = true;
        getReplyListUseCase.execute(GetReplyListUseCase.generateParam(getView().getArguments().getString(PARAM_MESSAGE_ID), pagingHandler.getPage()), new GetReplySubscriber(getView(), this));
    }

    public void setResult(GetReplyViewModel replyData) {
        getView().setCanLoadMore(false);
        getView().setHeader();
        if(pagingHandler.getPage()==1) {
            getView().getAdapter().setList(replyData.getList());
            getView().scrollToBottom();
        }else {
            getView().getAdapter().addList(replyData.getList());
        }
        getView().setTextAreaReply(replyData.getTextAreaReply()==1);
        getView().setCanLoadMore(replyData.isHasNext());
    }

    public void finishLoading() {
        getView().finishLoading();
    }

    public void finishRequest(int i) {
        isRequesting = false;
    }

    private boolean isValidReply() {
        boolean isValid = true;
        if (getView().getReplyMessage().trim().length() == 0) {
            isValid = false;
            getView().showError(getView().getString(R.string.error_empty_report));
        }
        return isValid;
    }

    public void sendMessage() {
        if(isValidReply()){
            getView().addDummyMessage();
            getView().setViewEnabled(false);

            final String reply = (getView().getReplyMessage());
            String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));

            RequestParams params = ReplyMessageUseCase.generateParam(messageId, reply);
            isRequesting = true;
            replyMessageUseCase.execute(params, new Subscriber<ReplyActionData>() {
                @Override
                public void onCompleted() {
                    isRequesting = false;
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ReplyActionData data) {
                    getView().onSuccessSendReply(data, reply);
                }
            });
        }
    }

    public void onRefresh() {
        if (!isRequesting) {
            pagingHandler.resetPage();
            getReply();
        } else {
            getView().getRefreshHandler().finishRefresh();
        }
    }
}
