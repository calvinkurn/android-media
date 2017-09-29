package com.tokopedia.inbox.inboxchat.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.inbox.inboxchat.domain.model.message.ListMessage;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxmessage.model.InboxMessagePass;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatPresenter extends BaseDaggerPresenter<InboxChatContract.View>
        implements InboxChatContract.Presenter{

    private final GetMessageListUseCase getMessageListUseCase;
    private final GetReplyListUseCase getReplyListUseCase;
    private final ReplyMessageUseCase replyMessageUseCase;
    InboxMessagePass inboxMessagePass;
    PagingHandler pagingHandler;

    @Inject
    InboxChatPresenter(GetMessageListUseCase getMessageListUseCase,
                       GetReplyListUseCase getReplyListUseCase,
                       ReplyMessageUseCase replyMessageUseCase){
        this.getMessageListUseCase = getMessageListUseCase;
        this.getReplyListUseCase = getReplyListUseCase;
        this.replyMessageUseCase = replyMessageUseCase;
    }

    @Override
    public void attachView(InboxChatContract.View view) {
        super.attachView(view);
        initialize();
        getMessage();
    }

    private void initialize() {
        this.inboxMessagePass = new InboxMessagePass();
        this.inboxMessagePass.setNav(getView().getNav());
        this.pagingHandler = new PagingHandler();
    }

    private void getMessage() {
        showLoading();
        getView().disableActions();
        getView().removeError();
//
        getMessageListUseCase.execute(GetMessageListUseCase.generateParam(inboxMessagePass), new Subscriber<MessageData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(MessageData messageData) {
                getView().enableActions();
//                if (pagingHandler.getPage() == 1 && !isFilterUsed()) {
//                    cacheInteractor.setInboxMessageCache(viewListener.getArguments().getString(PARAM_NAV), result);
//                }
                if (getView().getRefreshHandler().isRefreshing()) {
                    getView().getAdapter().getList().clear();
                    getView().getAdapter().clearSelection();
                }
                getView().finishLoading();

                setResult(messageData);
                if (pagingHandler.CheckNextPage()) {
                    getView().getAdapter().showLoading(true);
                }

                getView().setMustRefresh(false);
            }
        });
    }

    private void showLoading() {
        if (pagingHandler.getPage() == 1 && getView().getAdapter().getList().size() != 0) {
            getView().getRefreshHandler().setRefreshing(true);
            getView().getRefreshHandler().setIsRefreshing(true);
        } else if (pagingHandler.getPage() == 1 && getView().getAdapter().getList().size() == 0 && !getView().getRefreshHandler().isRefreshing()) {
            getView().getAdapter().showLoadingFull(true);
        } else if (!getView().getRefreshHandler().isRefreshing()) {
            getView().getAdapter().showLoading(true);
        }
    }

    private void setResult(MessageData result) {
        getView().getAdapter().setList(result.getList());
        if (getView().getAdapter().getList().size() == 0) {
            getView().getAdapter().showEmptyFull(true);
        } else {
            getView().getAdapter().showEmptyFull(false);
        }

//        pagingHandler.setHasNext(PagingHandler.CheckHasNext(result.getPaging()));
//        pagingHandler.setPagingHandlerModel(result.getPaging());
    }

    @Override
    public void detachView() {
        super.detachView();
        getMessageListUseCase.unsubscribe();
        getReplyListUseCase.unsubscribe();
        replyMessageUseCase.unsubscribe();
    }


    public void onSelected(int position) {
    }

    public void onDeselect(int position) {

    }

    public void goToDetailMessage(int position, ListMessage listMessage) {
    }

    public void goToProfile(int userId) {

    }

    public void refreshData() {
        getView().finishContextMode();
//        if (!networkInteractor.isRequesting()) {
//            viewListener.getAdapter().showLoading(false);
//            pagingHandler.resetPage();
//            inboxMessagePass.setPage(String.valueOf(pagingHandler.getPage()));
//            viewListener.getRefreshHandler().setRefreshing(true);
//            viewListener.getRefreshHandler().setIsRefreshing(true);
//            getInboxMessage();
//        } else {
//            viewListener.getRefreshHandler().finishRefresh();
//        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser, boolean isMustRefresh) {
//        initAnalytics();
        if (isDataEmpty() && isVisibleToUser && !getView().hasRetry()) {
            getMessage();
        } else if (isMustRefresh) {
            refreshData();
        }
    }

    private boolean isDataEmpty() {
        return getView().getAdapter().getList().size() == 0;
    }


}
