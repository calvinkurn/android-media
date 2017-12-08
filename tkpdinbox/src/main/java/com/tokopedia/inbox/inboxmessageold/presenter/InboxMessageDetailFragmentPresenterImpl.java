package com.tokopedia.inbox.inboxmessageold.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.inbox.inboxmessageold.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessageold.activity.InboxMessageDetailActivity;
import com.tokopedia.inbox.inboxmessageold.fragment.InboxMessageDetailFragment;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageActRetrofitInteractor;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageActRetrofitInteractorImpl;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageCacheInteractor;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageCacheInteractorImpl;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageRetrofitInteractor;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageRetrofitInteractorImpl;
import com.tokopedia.inbox.inboxmessageold.listener.InboxMessageDetailFragmentView;
import com.tokopedia.inbox.inboxmessageold.model.ActInboxMessagePass;
import com.tokopedia.inbox.inboxmessageold.model.InboxMessagePass;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessage.InboxMessageItem;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.ConversationBetween;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetail;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetailItem;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.getproducturlutil.GetProductUrlUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Nisie on 5/19/16.
 */
public class InboxMessageDetailFragmentPresenterImpl implements InboxMessageDetailFragmentPresenter,
        InboxMessageConstant {

    InboxMessageDetailFragmentView viewListener;
    InboxMessageRetrofitInteractor networkInteractor;
    InboxMessageActRetrofitInteractor actNetworkinteractor;
    InboxMessageCacheInteractor cacheInteractor;
    PagingHandler pagingHandler;
    InboxMessageDetailFragment.DoActionInboxMessageListener listener;

    public InboxMessageDetailFragmentPresenterImpl(InboxMessageDetailFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new InboxMessageRetrofitInteractorImpl();
        this.actNetworkinteractor = new InboxMessageActRetrofitInteractorImpl();
        this.cacheInteractor = new InboxMessageCacheInteractorImpl();
        this.pagingHandler = new PagingHandler();
        this.listener = (InboxMessageDetailActivity) viewListener.getActivity();
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
        NotificationModHandler.clearCacheIfFromNotification(
                Constants.ARG_NOTIFICATION_APPLINK_MESSAGE,
                viewListener.getArguments().getString(PARAM_MESSAGE_ID)
        );
    }

    @Override
    public String getMessageBetween(InboxMessageDetail inboxMessageDetail) {
        String between = viewListener.getString(com.tokopedia.core.R.string.title_between) + " : ";
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

        networkInteractor.getInboxMessageDetail(viewListener.getActivity(),
                getMessageDetailParam(),
                new InboxMessageRetrofitInteractor.GetInboxMessageDetailListener() {
                    @Override
                    public void onSuccess(InboxMessageDetail result) {
                        viewListener.setViewEnabled(true);

                        if (pagingHandler.getPage() == 1) {
                            viewListener.getAdapter().clearData();
                            cacheInteractor.setInboxMessageDetailCache(viewListener.getArguments().getString(PARAM_MESSAGE_ID), result);
                        }

                        viewListener.finishLoading();
                        setResult(result);

                    }

                    @Override
                    public void onTimeout(String message) {
                        if (viewListener.getAdapter().getData().size() == 0) {
                            viewListener.finishLoading();
                            viewListener.showEmptyState();
                        } else {
                            viewListener.setRetry(viewListener.getString(com.tokopedia.core.R.string.msg_connection_timeout),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            getMessageDetail();
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onError(String error) {
                        if (viewListener.getAdapter().getData().size() == 0) {
                            viewListener.finishLoading();
                            viewListener.showEmptyState(error);
                        } else {
                            viewListener.setRetry(error,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getMessageDetail();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onNullData() {

                    }

                    @Override
                    public void onNoConnectionError() {
                        if (viewListener.getAdapter().getData().size() == 0) {
                            viewListener.finishLoading();
                            viewListener.showEmptyState();
                        } else {
                            viewListener.setRetry(viewListener.getString(com.tokopedia.core.R.string.msg_no_connection),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getMessageDetail();
                                        }
                                    });
                        }

                    }
                });
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
        pagingHandler.setHasNext(PagingHandler.CheckHasNext(inboxMessageDetail.getPaging()));
        viewListener.getAdapter().setCanLoadMore(pagingHandler.CheckNextPage());
        viewListener.getAdapter().setList(inboxMessageDetail.getList());
        viewListener.setHeader(inboxMessageDetail);
        setReputation();
        if (pagingHandler.getPage() == 1)
            viewListener.scrollToBottom();

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
            listener.sendReply(param);
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
            viewListener.showError(viewListener.getString(com.tokopedia.core.R.string.error_empty_report));
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
}
