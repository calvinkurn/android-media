package com.tokopedia.inbox.inboxmessage.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.inboxchat.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.data.factory.ReplyFactory;
import com.tokopedia.inbox.inboxchat.data.factory.WebSocketFactory;
import com.tokopedia.inbox.inboxchat.data.mapper.ChatEventMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.GetReplyMapper;
import com.tokopedia.inbox.inboxchat.data.mapper.ReplyMessageMapper;
import com.tokopedia.inbox.inboxchat.data.repository.ReplyRepositoryImpl;
import com.tokopedia.inbox.inboxchat.data.repository.WebSocketRepositoryImpl;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ListenWebSocketUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessage.activity.InboxMessageDetailActivity;
import com.tokopedia.inbox.inboxmessage.fragment.InboxMessageDetailFragment;
import com.tokopedia.inbox.inboxmessage.interactor.InboxMessageActRetrofitInteractor;
import com.tokopedia.inbox.inboxmessage.interactor.InboxMessageActRetrofitInteractorImpl;
import com.tokopedia.inbox.inboxmessage.interactor.InboxMessageCacheInteractor;
import com.tokopedia.inbox.inboxmessage.interactor.InboxMessageCacheInteractorImpl;
import com.tokopedia.inbox.inboxmessage.interactor.InboxMessageRetrofitInteractor;
import com.tokopedia.inbox.inboxmessage.interactor.InboxMessageRetrofitInteractorImpl;
import com.tokopedia.inbox.inboxmessage.listener.InboxMessageDetailFragmentView;
import com.tokopedia.inbox.inboxmessage.model.ActInboxMessagePass;
import com.tokopedia.inbox.inboxmessage.model.InboxMessagePass;
import com.tokopedia.inbox.inboxmessage.model.inboxmessage.InboxMessageItem;
import com.tokopedia.inbox.inboxmessage.model.inboxmessagedetail.ConversationBetween;
import com.tokopedia.inbox.inboxmessage.model.inboxmessagedetail.InboxMessageDetail;
import com.tokopedia.inbox.inboxmessage.model.inboxmessagedetail.InboxMessageDetailItem;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.getproducturlutil.GetProductUrlUtil;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import rx.Subscriber;

/**
 * Created by Nisie on 5/19/16.
 */
public class InboxMessageDetailFragmentPresenterImpl implements InboxMessageDetailFragmentPresenter,
        InboxMessageConstant, WebSocketInterface {

    private final ChatService chatService;
    private final ReplyFactory replyFactory;
    private final ReplyRepositoryImpl replyRepo;

    InboxMessageDetailFragmentView viewListener;
    InboxMessageRetrofitInteractor networkInteractor;
    InboxMessageActRetrofitInteractor actNetworkinteractor;
    InboxMessageCacheInteractor cacheInteractor;
    PagingHandler pagingHandler;
    InboxMessageDetailFragment.DoActionInboxMessageListener listener;

    private GetReplyListUseCase getReplyListUseCase;
    private ReplyMessageUseCase replyMessageUseCase;

    private WebSocketRepositoryImpl webSocketRepo;
    private WebSocketFactory webSocketFactory;
    private final ListenWebSocketUseCase listenWebSocketUseCase;
    private OkHttpClient client;

    public InboxMessageDetailFragmentPresenterImpl(InboxMessageDetailFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new InboxMessageRetrofitInteractorImpl();
        this.actNetworkinteractor = new InboxMessageActRetrofitInteractorImpl();
        this.cacheInteractor = new InboxMessageCacheInteractorImpl();
        this.pagingHandler = new PagingHandler();
        this.listener = (InboxMessageDetailActivity) viewListener.getActivity();

        chatService = new ChatService();
        replyFactory = new ReplyFactory(chatService, new GetReplyMapper(), new ReplyMessageMapper());
        replyRepo = new ReplyRepositoryImpl(replyFactory);

        getReplyListUseCase = new GetReplyListUseCase(new JobExecutor(), new UIThread(), replyRepo);
        replyMessageUseCase = new ReplyMessageUseCase(new JobExecutor(), new UIThread(), replyRepo);

        webSocketFactory = new WebSocketFactory(chatService, new ChatEventMapper());
        webSocketRepo = new WebSocketRepositoryImpl(webSocketFactory);
        listenWebSocketUseCase = new ListenWebSocketUseCase(new JobExecutor(), new UIThread(), webSocketRepo);
    }

    @Override
    public void initData() {
        cacheInteractor.getInboxMessageDetailCache(
                viewListener.getArguments().getString(PARAM_MESSAGE_ID),
                new InboxMessageCacheInteractor.GetInboxMessageDetailCacheListener() {
                    @Override
                    public void onSuccess(InboxMessageDetail inboxMessage) {
                        setResult(inboxMessage);
                        getMessageDetail();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMessageDetail();
                    }
                });

        client = new OkHttpClient();
        String magicString = "wss://chat-staging.tokopedia.com/connect?" +
                "os_type=1" +
                "&device_id="+ GCMHandler.getRegistrationId(viewListener.getActivity()) +
                "&user_id="+ SessionHandler.getLoginID(viewListener.getActivity());
        Request request = new Request.Builder().url(magicString)
                .header("Origin", "https://staging.tokopedia.com")
                .build();
        ChatWebSocketListenerImpl listener = new ChatWebSocketListenerImpl(this);
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();

        NotificationModHandler.clearCacheIfFromNotification(
                Constants.ARG_NOTIFICATION_APPLINK_MESSAGE,
                viewListener.getArguments().getString(PARAM_MESSAGE_ID)
        );
    }

    @Override
    public String getMessageBetween(InboxMessageDetail inboxMessageDetail) {
        String between = viewListener.getString(R.string.title_between) + " : ";
        ArrayList<String> betweenName = new ArrayList<>();
        for (ConversationBetween conversationBetween : inboxMessageDetail.getConversationBetween()) {
            betweenName.add(conversationBetween.getUserName());
        }
        between += TextUtils.join(", ", betweenName);
        return between;
    }


    @Override
    public void getMessageDetail() {
        showLoading();
        viewListener.setViewEnabled(false);

//        networkInteractor.getInboxMessageDetail(viewListener.getActivity(),
//                getMessageDetailParam(),
//                new InboxMessageRetrofitInteractor.GetInboxMessageDetailListener() {
//                    @Override
//                    public void onSuccess(InboxMessageDetail result) {
//                        viewListener.setViewEnabled(true);
//
//                        if (pagingHandler.getPage() == 1) {
//                            viewListener.getAdapter().clearData();
//                            cacheInteractor.setInboxMessageDetailCache(viewListener.getArguments().getString(PARAM_MESSAGE_ID), result);
//                        }
//
//                        viewListener.finishLoading();
//                        setResult(result);
//
//                    }
//
//                    @Override
//                    public void onTimeout(String message) {
//                        if (viewListener.getAdapter().getData().size() == 0) {
//                            viewListener.finishLoading();
//                            viewListener.showEmptyState();
//                        } else {
//                            viewListener.setRetry(viewListener.getString(R.string.msg_connection_timeout),
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                            getMessageDetail();
//                                        }
//                                    });
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        if (viewListener.getAdapter().getData().size() == 0) {
//                            viewListener.finishLoading();
//                            viewListener.showEmptyState(error);
//                        } else {
//                            viewListener.setRetry(error,
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            getMessageDetail();
//                                        }
//                                    });
//                        }
//                    }
//
//                    @Override
//                    public void onNullData() {
//
//                    }
//
//                    @Override
//                    public void onNoConnectionError() {
//                        if (viewListener.getAdapter().getData().size() == 0) {
//                            viewListener.finishLoading();
//                            viewListener.showEmptyState();
//                        } else {
//                            viewListener.setRetry(viewListener.getString(R.string.msg_no_connection),
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            getMessageDetail();
//                                        }
//                                    });
//                        }
//
//                    }
//                });

//        getReplyListUseCase.execute(GetReplyListUseCase.generateParam(viewListener.getArguments().getString(PARAM_MESSAGE_ID), pagingHandler.getPage()), new Subscriber<ReplyData>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(ReplyData model) {
//                viewListener.setViewEnabled(true);
//
//                if (pagingHandler.getPage() == 1) {
//                    viewListener.getAdapter().clearData();
////                    cacheInteractor.setInboxMessageDetailCache(viewListener.getArguments().getString(PARAM_MESSAGE_ID), result);
//                }
//
//                viewListener.finishLoading();
//                viewListener.setTextAreaReply(model.getTextAreaReply()==1);
//                setResult(model);
//            }
//        });
    }

    private void showLoading() {
        if (pagingHandler.getPage() == 1 && viewListener.getAdapter().getData().size() != 0) {
            viewListener.getRefreshHandler().setRefreshing(true);
            viewListener.getRefreshHandler().setIsRefreshing(true);
        } else if (pagingHandler.getPage() == 1 && viewListener.getAdapter().getData().size() == 0
                && !viewListener.getRefreshHandler().isRefreshing()) {
            viewListener.getAdapter().showLoadingFull(true);
        } else{
            viewListener.getAdapter().showLoading(true);
        }
    }

    @Override
    public void setResult(InboxMessageDetail inboxMessageDetail) {
//        pagingHandler.setHasNext(PagingHandler.CheckHasNext(inboxMessageDetail.getPaging()));
//        viewListener.getAdapter().setCanLoadMore(pagingHandler.CheckNextPage());
//        viewListener.getAdapter().setList(inboxMessageDetail.getList());
//        viewListener.setHeader(inboxMessageDetail);
//        setReputation();
//        if (pagingHandler.getPage() == 1)
//            viewListener.scrollTo();

    }


    private void setResult(ReplyData replyData) {
        viewListener.getAdapter().setList(replyData.getList());
        viewListener.setHeader();
        setReputation();
        if (pagingHandler.getPage() == 1)
            viewListener.scrollToBottom();

        pagingHandler.setHasNext(replyData.isHasNext());
        viewListener.getAdapter().setCanLoadMore(pagingHandler.CheckNextPage());

    }

    private void setReputation() {
        InboxMessageItem messageItem = viewListener.getArguments().getParcelable(PARAM_MESSAGE);
        if (messageItem != null) {
            if (messageItem.getUserReputation().getNoReputation().equals("0")) {
                viewListener.setHasReputation(messageItem.getUserReputation().getPositivePercentage());
            } else {
                viewListener.setNoReputation();
            }

        }
    }

    @Override
    public Map<String, String> getMessageDetailParam() {
        InboxMessagePass param = new InboxMessagePass();
        param.setMessageId(viewListener.getArguments().getString(PARAM_MESSAGE_ID));
        param.setNav(viewListener.getArguments().getString(PARAM_NAV));
        param.setPage(String.valueOf(pagingHandler.getPage()));
        return param.getInboxMessageDetailParam();
    }

    @Override
    public void onRefresh() {
        if (!networkInteractor.isRequesting()) {
            pagingHandler.resetPage();
            getMessageDetail();
        } else {
            viewListener.getRefreshHandler().finishRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (!networkInteractor.isRequesting()) {
            pagingHandler.nextPage();
            getMessageDetail();
        }else{
            viewListener.finishLoading();
        }

    }

    @Override
    public void onGoToProfile(String userId) {
        viewListener.getActivity().startActivity(
                PeopleInfoNoDrawerActivity.createInstance(viewListener.getActivity(), userId)
        );
    }

    @Override
    public void sendReply() {
        if (isValidReply()) {
            viewListener.addTempMessage();
            viewListener.setViewEnabled(false);
            viewListener.getRefreshHandler().setRefreshing(true);
            final ActInboxMessagePass pass = getSendReplyParam();

            Bundle param = new Bundle();
            param.putParcelable(PARAM_SEND_REPLY, pass);
//            listener.sendReply(param);

            final String reply = (viewListener.getReplyMessage().getText().toString());
            String messageId = (viewListener.getArguments().getString(PARAM_MESSAGE_ID));

            RequestParams params = ReplyMessageUseCase.generateParam(messageId, reply);
            replyMessageUseCase.execute(params, new Subscriber<ReplyActionData>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ReplyActionData data) {
                    viewListener.onSuccessSendReply(data, reply);
                }
            });
        }
    }

    @Override
    public void flagSpam(final int position, final InboxMessageDetailItem inboxMessageDetailItem) {
        viewListener.getRefreshHandler().setRefreshing(true);
        viewListener.setViewEnabled(false);
        final ActInboxMessagePass pass = getFlagParam(inboxMessageDetailItem.getMessageReplyId());

        Bundle param = new Bundle();
        param.putParcelable(PARAM_FLAG_SPAM, pass);
        param.putInt(EXTRA_POSITION, position);
        param.putParcelable(EXTRA_FLAGGED_MESSAGE, inboxMessageDetailItem);
        listener.flagSpam(param);
    }

    @Override
    public void undoFlagSpam(final int position, InboxMessageDetailItem inboxMessageDetailItem) {
        viewListener.getRefreshHandler().setRefreshing(true);
        viewListener.setViewEnabled(false);

        final ActInboxMessagePass pass = getFlagParam(inboxMessageDetailItem.getMessageReplyId());

        Bundle param = new Bundle();
        param.putParcelable(PARAM_UNDO_FLAG_SPAM, pass);
        param.putInt(EXTRA_POSITION, position);
        param.putParcelable(EXTRA_FLAGGED_MESSAGE, inboxMessageDetailItem);
        listener.undoFlagSpam(param);
    }


    @Override
    public void getUrl() {
        GetProductUrlUtil getProd = GetProductUrlUtil.createInstance(viewListener.getActivity());
        getProd.getOwnShopProductUrl(new GetProductUrlUtil.OnGetUrlInterface() {
            @Override
            public void onGetUrl(String url) {
                viewListener.addUrlToReply(url);
            }
        });
    }

    @Override
    public void updateCache(final InboxMessageDetailItem result) {
        cacheInteractor.getInboxMessageDetailCache(
                viewListener.getArguments().getString(PARAM_MESSAGE_ID),
                new InboxMessageCacheInteractor.GetInboxMessageDetailCacheListener() {
                    @Override
                    public void onSuccess(InboxMessageDetail inboxMessage) {
                        inboxMessage.getList().add(0, result);
                        cacheInteractor.setInboxMessageDetailCache(viewListener.getArguments().getString(PARAM_MESSAGE_ID), inboxMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        CommonUtils.dumper(InboxMessageDetailFragment.class.getSimpleName() + " " + e.toString());
                    }
                });
    }

    @Override
    public void updateCacheFlagSpam(final int position) {
        cacheInteractor.getInboxMessageDetailCache(
                viewListener.getArguments().getString(PARAM_MESSAGE_ID),
                new InboxMessageCacheInteractor.GetInboxMessageDetailCacheListener() {
                    @Override
                    public void onSuccess(InboxMessageDetail inboxMessage) {
                        inboxMessage.getList().remove(position);
                        cacheInteractor.setInboxMessageDetailCache(viewListener.getArguments().getString(PARAM_MESSAGE_ID), inboxMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        CommonUtils.dumper(InboxMessageDetailFragment.class.getSimpleName() + " " + e.toString());
                    }
                });
    }

    @Override
    public void updateCacheUndoFlagSpam(final int position, final InboxMessageDetailItem inboxMessageDetailItem) {
        cacheInteractor.getInboxMessageDetailCache(
                viewListener.getArguments().getString(PARAM_MESSAGE_ID),
                new InboxMessageCacheInteractor.GetInboxMessageDetailCacheListener() {
                    @Override
                    public void onSuccess(InboxMessageDetail inboxMessage) {
                        inboxMessage.getList().add(position, inboxMessageDetailItem);
                        cacheInteractor.setInboxMessageDetailCache(viewListener.getArguments().getString(PARAM_MESSAGE_ID), inboxMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        CommonUtils.dumper(InboxMessageDetailFragment.class.getSimpleName() + " " + e.toString());
                    }
                });
    }

    @Override
    public void setResultBundle() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_POSITION, viewListener.getArguments().getInt(PARAM_POSITION, -1));
        intent.putExtras(bundle);
        viewListener.getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private boolean isValidReply() {
        boolean isValid = true;
        if (viewListener.getReplyMessage().getText().toString().trim().length() == 0) {
            isValid = false;
            viewListener.showError(viewListener.getString(R.string.error_empty_report));
        }
        return isValid;
    }

    private ActInboxMessagePass getSendReplyParam() {
        ActInboxMessagePass pass = new ActInboxMessagePass();
        pass.setReplyMessage(viewListener.getReplyMessage().getText().toString());
        pass.setMessageId(viewListener.getArguments().getString(PARAM_MESSAGE_ID));
        return pass;
    }

    private ActInboxMessagePass getFlagParam(int messageReplyId) {
        ActInboxMessagePass pass = new ActInboxMessagePass();
        pass.setNav(viewListener.getArguments().getString(PARAM_NAV));
        pass.setMessageReplyId(String.valueOf(messageReplyId));
        return pass;
    }

    @Override
    public void onDestroyView() {
        actNetworkinteractor.unSubscribeObservable();
        networkInteractor.unSubscribeObservable();
    }

    public void onIncomingEvent(WebSocketResponse response) {

        switch (response.getCode()){
            case ChatWebSocketConstant.EVENT_TOPCHAT_TYPING :
                viewListener.setOnlineDesc("sedang mengetik");
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING:
                viewListener.setOnlineDesc("baru saja");
                break;
            default:
                break;
        }

    }

    @Override
    public void newWebSocket() {

    }

    @Override
    public void onOpenWebSocket() {

    }
}
