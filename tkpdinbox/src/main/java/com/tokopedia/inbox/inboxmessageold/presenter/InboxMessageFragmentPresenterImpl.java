package com.tokopedia.inbox.inboxmessageold.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.inbox.inboxmessageold.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessageold.activity.InboxMessageDetailActivity;
import com.tokopedia.inbox.inboxmessageold.fragment.InboxMessageFragment;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageCacheInteractor;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageCacheInteractorImpl;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageRetrofitInteractor;
import com.tokopedia.inbox.inboxmessageold.interactor.InboxMessageRetrofitInteractorImpl;
import com.tokopedia.inbox.inboxmessageold.listener.InboxMessageView;
import com.tokopedia.inbox.inboxmessageold.model.ActInboxMessagePass;
import com.tokopedia.inbox.inboxmessageold.model.InboxMessagePass;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessage.InboxMessage;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessage.InboxMessageItem;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetailItem;

import java.util.ArrayList;

/**
 * Created by Nisie on 5/9/16.
 */
public class InboxMessageFragmentPresenterImpl implements InboxMessageFragmentPresenter,
        InboxMessageConstant {


    InboxMessageView viewListener;
    InboxMessageRetrofitInteractor networkInteractor;

    PagingHandler pagingHandler;
    InboxMessageCacheInteractor cacheInteractor;
    InboxMessagePass inboxMessagePass;
    InboxMessageFragment.DoActionInboxMessageListener actListener;

    public InboxMessageFragmentPresenterImpl(InboxMessageFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new InboxMessageRetrofitInteractorImpl();
        this.cacheInteractor = new InboxMessageCacheInteractorImpl();
        this.pagingHandler = new PagingHandler();
        this.inboxMessagePass = new InboxMessagePass();
        this.inboxMessagePass.setNav(viewListener.getArguments().getString(PARAM_NAV, ""));
        if (viewListener.getActivity() instanceof InboxMessageFragment.DoActionInboxMessageListener) {
            this.actListener = (InboxMessageFragment.DoActionInboxMessageListener) viewListener.getActivity();
        } else {
            this.actListener = null;
        }

    }

    @Override
    public void initData() {
        if (viewListener.getUserVisibleHint()) {
            cacheInteractor.getInboxMessageCache(viewListener.getArguments().getString(PARAM_NAV), new InboxMessageCacheInteractor.GetInboxMessageCacheListener() {
                @Override
                public void onSuccess(InboxMessage inboxMessage) {
                    setResult(inboxMessage);
                    getInboxMessage();
                }

                @Override
                public void onError(Throwable e) {
                    getInboxMessage();
                }
            });
        }
    }

    @Override
    public void getInboxMessage() {
        showLoading();
        viewListener.disableActions();
        viewListener.removeError();
        networkInteractor.getInboxMessage(viewListener.getActivity(), inboxMessagePass.getInboxMessageParam(), new InboxMessageRetrofitInteractor.GetInboxMessageListener() {
            @Override
            public void onSuccess(InboxMessage result) {
                viewListener.enableActions();
                if (pagingHandler.getPage() == 1 && !isFilterUsed()) {
                    cacheInteractor.setInboxMessageCache(viewListener.getArguments().getString(PARAM_NAV), result);
                }
                if (viewListener.getRefreshHandler().isRefreshing()) {
                    viewListener.getAdapter().getList().clear();
                    viewListener.getAdapter().clearSelection();
                }
                viewListener.finishLoading();

                setResult(result);
                if (pagingHandler.CheckNextPage()) {
                    viewListener.getAdapter().showLoading(true);
                }

                viewListener.setMustRefresh(false);
            }

            @Override
            public void onTimeout(String message) {

                if (viewListener.getAdapter().getList().size() == 0) {
                    viewListener.finishLoading();
                    viewListener.showEmptyState();
                } else if (pagingHandler.getPage() == 1) {
                    viewListener.disableActions();
                    viewListener.getRefreshHandler().setPullEnabled(true);
                    viewListener.showError("");
                } else {
                    viewListener.setRetry(viewListener.getString(com.tokopedia.core.R.string.msg_connection_timeout),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getInboxMessage();
                                }
                            });
                }
            }

            @Override
            public void onError(String error) {
                viewListener.enableActions();
                viewListener.finishLoading();
                if (viewListener.getAdapter().getList().size() == 0) {
                    viewListener.showEmptyState(error);
                } else {
                    viewListener.enableActions();
                    viewListener.showError(error);
                }

            }

            @Override
            public void onNullData() {
                viewListener.enableActions();
                viewListener.finishLoading();
                viewListener.getAdapter().showEmptyFull(true);
            }

            @Override
            public void onNoConnectionError() {
                viewListener.finishLoading();

                if (viewListener.getAdapter().getList().size() == 0) {
                    viewListener.showEmptyState();
                } else if (pagingHandler.getPage() == 1) {
                    viewListener.disableActions();
                    viewListener.getRefreshHandler().setPullEnabled(true);
                    viewListener.showError("");
                } else {
                    viewListener.setRetry(viewListener.getString(com.tokopedia.core.R.string.msg_no_connection), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getInboxMessage();
                        }
                    });
                }
            }
        });
    }

    private void showLoading() {
        if (pagingHandler.getPage() == 1 && viewListener.getAdapter().getList().size() != 0) {
            viewListener.getRefreshHandler().setRefreshing(true);
            viewListener.getRefreshHandler().setIsRefreshing(true);
        } else if (pagingHandler.getPage() == 1 && viewListener.getAdapter().getList().size() == 0 && !viewListener.getRefreshHandler().isRefreshing()) {
            viewListener.getAdapter().showLoadingFull(true);
        } else if (!viewListener.getRefreshHandler().isRefreshing()) {
            viewListener.getAdapter().showLoading(true);
        }
    }

    private boolean isFilterUsed() {
        return viewListener.getFilter().equals(PARAM_UNREAD) ||
                !viewListener.getKeyword().equals("");
    }

    @Override
    public boolean isLoading() {
        return networkInteractor.isRequesting();
    }

    @Override
    public void onDeselect(int position) {
        viewListener.getAdapter().removeChecked(position);
        viewListener.setOptionsMenu();

    }

    @Override
    public void onSelected(int position) {
        viewListener.getAdapter().addChecked(position);
        viewListener.setOptionsMenu();

    }

    @Override
    public void moveInbox(String act) {
        viewListener.disableActions();
        final ActInboxMessagePass pass = getMoveInboxPass();
        switch (act) {
            case ARCHIVE_ALL:
                archiveMessages(pass);
                break;
            case MOVE_ALL:
                moveToInbox(pass);
                break;
            case DELETE_ALL:
                deleteMessages(pass);
                break;
            case DELETE_FOREVER:
                deleteForever(pass);
                break;
        }

    }


    @Override
    public void archiveMessages(ActInboxMessagePass pass) {
        viewListener.showLoadingDialog();

        Bundle param = new Bundle();
        param.putParcelable(PARAM_ARCHIVE_MESSAGE, pass);
        actListener.archiveMessage(param);
    }

    @Override
    public void undoArchiveMessage(final ArrayList<InboxMessageItem> listMove) {
        viewListener.showLoadingDialog();

        final ActInboxMessagePass pass = getMoveInboxPass();
        pass.setListMove(listMove);

        Bundle param = new Bundle();
        param.putParcelable(PARAM_UNDO_ARCHIVE_MESSAGE, pass);
        actListener.undoArchiveMessage(param);

    }

    @Override
    public void moveToInbox(ActInboxMessagePass pass) {
        viewListener.showLoadingDialog();

        Bundle param = new Bundle();
        param.putParcelable(PARAM_MOVE_TO_INBOX, pass);
        actListener.moveToInbox(param);
    }

    @Override
    public void undoMoveToInbox(final ArrayList<InboxMessageItem> listMove) {
        viewListener.showLoadingDialog();

        final ActInboxMessagePass pass = getMoveInboxPass();
        pass.setListMove(listMove);

        Bundle param = new Bundle();
        param.putParcelable(PARAM_UNDO_MOVE_TO_INBOX, pass);
        actListener.undoMoveToInbox(param);
    }

    @Override
    public void deleteMessages(ActInboxMessagePass pass) {

        viewListener.showLoadingDialog();

        Bundle param = new Bundle();
        param.putParcelable(PARAM_DELETE_MESSAGE, pass);
        actListener.deleteMessage(param);

    }

    @Override
    public void undoDeleteMessage(final ArrayList<InboxMessageItem> listMove) {
        viewListener.showLoadingDialog();

        final ActInboxMessagePass pass = getMoveInboxPass();
        pass.setListMove(listMove);

        Bundle param = new Bundle();
        param.putParcelable(PARAM_UNDO_DELETE_MESSAGE, pass);
        actListener.undoDeleteMessage(param);

    }

    @Override
    public void deleteForever(ActInboxMessagePass pass) {
        viewListener.showLoadingDialog();


        Bundle param = new Bundle();
        param.putParcelable(PARAM_DELETE_FOREVER, pass);
        actListener.deleteMessageForever(param);

    }

    @Override
    public void setNav(String nav) {
        inboxMessagePass.setNav(nav);
    }

    @Override
    public void setMessageRead(final Intent data) {
        cacheInteractor.getInboxMessageCache(viewListener.getArguments().getString(PARAM_NAV), new InboxMessageCacheInteractor.GetInboxMessageCacheListener() {
            @Override
            public void onSuccess(InboxMessage inboxMessage) {
                InboxMessageDetailItem sentMessage = data.getExtras().getParcelable(PARAM_SENT_MESSAGE);
                int position = data.getIntExtra(PARAM_POSITION, -1);
                if (position != -1) {
                    viewListener.getAdapter().getList().get(position).setMessageReadStatus(STATE_READ);
                    if (sentMessage != null) {
                        viewListener.getAdapter().getList().get(position).setMessageCreateTimeFmt(viewListener.getAdapter().getList().get(position).getMessageCreateTimeFmt());
                        viewListener.getAdapter().getList().get(position).setMessageReply(sentMessage.getMessageReply().toString());
                        viewListener.getAdapter().getList().get(position).setUserFullName(sentMessage.getUserName().toString());
                        viewListener.getAdapter().getList().get(position).setUserReputation(sentMessage.getUserReputation());
                        viewListener.getAdapter().getList().get(position).setUserImage(sentMessage.getUserImage());
                        viewListener.getAdapter().getList().get(position).setUserLabel(sentMessage.getUserLabel());

                        inboxMessage.getList().clear();
                        inboxMessage.getList().addAll(viewListener.getAdapter().getList());
                    }
                    viewListener.getAdapter().notifyDataSetChanged();
                }

                cacheInteractor.setInboxMessageCache(viewListener.getArguments().getString(PARAM_NAV), inboxMessage);
            }

            @Override
            public void onError(Throwable e) {

            }
        });


    }

    @Override
    public boolean hasActionListener() {
        return actListener != null;
    }

    @Override
    public InboxMessagePass getFilterParam() {
        return inboxMessagePass;
    }

    @Override
    public void restoreFilterBundle(InboxMessagePass inboxMessagePass) {
        this.inboxMessagePass = inboxMessagePass;
    }

    @Override
    public int getMenuID() {
        switch (viewListener.getArguments().getString(PARAM_NAV, "")) {
            case MESSAGE_ARCHIVE:
                return com.tokopedia.core.R.menu.inbox_message_archive;
            case MESSAGE_TRASH:
                return com.tokopedia.core.R.menu.inbox_message_trash;
            default:
                return com.tokopedia.core.R.menu.inbox_message;
        }
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unSubscribeObservable();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser, boolean isMustRefresh) {
        initAnalytics();
        if (isDataEmpty() && isVisibleToUser && !viewListener.hasRetry()) {
            getInboxMessage();
        } else if (isMustRefresh) {
            refreshData();
        }
    }

    @Override
    public void goToDetailMessage(int position, InboxMessageItem message) {
        Intent intent = new Intent(viewListener.getActivity(), InboxMessageDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NAV, viewListener.getArguments().getString(PARAM_NAV));
        bundle.putParcelable(PARAM_MESSAGE, message);
        bundle.putString(PARAM_MESSAGE_ID, String.valueOf(message.getMessageId()));
        bundle.putInt(PARAM_POSITION, position);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        viewListener.startActivityForResult(intent, OPEN_DETAIL_MESSAGE);
        viewListener.getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void goToProfile(int userId) {
        if (viewListener.getActivity().getApplicationContext() instanceof TkpdInboxRouter) {
            viewListener.startActivity(
                    ((TkpdInboxRouter) viewListener.getActivity().getApplicationContext())
                            .getTopProfileIntent(viewListener.getActivity(), String.valueOf(userId))
            );
        }
    }

    @Override
    public void generateSearchParam() {
        pagingHandler.resetPage();
        inboxMessagePass.setPage(String.valueOf(pagingHandler.getPage()));
        inboxMessagePass.setFilter(viewListener.getFilter());
        inboxMessagePass.setKeyword(viewListener.getKeyword());
    }

    private boolean isDataEmpty() {
        return viewListener.getAdapter().getList().size() == 0;
    }

    private ActInboxMessagePass getMoveInboxPass() {
        ActInboxMessagePass actMoveMessagePass = new ActInboxMessagePass();
        actMoveMessagePass.setNav(viewListener.getArguments().getString(PARAM_NAV));
        actMoveMessagePass.setListMove(viewListener.getAdapter().getListMove());
        return actMoveMessagePass;
    }

    private void setResult(InboxMessage result) {
        viewListener.getAdapter().setList(result.getList());
        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.getAdapter().showEmptyFull(true);
        } else {
            viewListener.getAdapter().showEmptyFull(false);
        }

        pagingHandler.setHasNext(PagingHandler.CheckHasNext(result.getPaging()));
        pagingHandler.setPagingHandlerModel(result.getPaging());
    }

    @Override
    public void initAnalytics() {
        try {
            String NAV = viewListener.getArguments().getString(PARAM_NAV);
            CommonUtils.dumper("LocalTag : Inbox Message -" + NAV);
            String screenName;
            switch (NAV) {
                case MESSAGE_ARCHIVE:
                    screenName = AppScreen.SCREEN_INBOX_ARCHIVE;
                    break;
                case MESSAGE_SENT:
                    screenName = AppScreen.SCREEN_INBOX_SENT;
                    break;
                case MESSAGE_TRASH:
                    screenName = AppScreen.SCREEN_INBOX_TRASH;
                    break;
                default:
                    screenName = AppScreen.SCREEN_INBOX_MAIN;
                    break;
            }
            ScreenTracking.screen(screenName);
        } catch (NullPointerException e) {
            CommonUtils.dumper("LocalTag Inbox Message Err : " + e.toString());
        }
    }

    @Override
    public void refreshData() {
        viewListener.finishContextMode();
        if (!networkInteractor.isRequesting()) {
            viewListener.getAdapter().showLoading(false);
            pagingHandler.resetPage();
            inboxMessagePass.setPage(String.valueOf(pagingHandler.getPage()));
            viewListener.getRefreshHandler().setRefreshing(true);
            viewListener.getRefreshHandler().setIsRefreshing(true);
            getInboxMessage();
        } else {
            viewListener.getRefreshHandler().finishRefresh();
        }
    }

    @Override
    public void loadMore(int lastItemPosition, int visibleItem) {
        if (hasNextPage() && isOnLastPosition(lastItemPosition, visibleItem) && canLoadMore()) {
            pagingHandler.setPage(pagingHandler.getNextPage());
            inboxMessagePass.setPage(String.valueOf(pagingHandler.getPage()));
            getInboxMessage();
        }
    }

    @Override
    public boolean hasNextPage() {
        return pagingHandler.CheckNextPage();
    }

    private boolean canLoadMore() {
        return !networkInteractor.isRequesting();
    }

    @Override
    public boolean isOnLastPosition(int itemPosition, int visibleItem) {
        return itemPosition == visibleItem;
    }

    @Override
    public void markAsRead() {
        viewListener.disableActions();
        final ActInboxMessagePass pass = getMoveInboxPass();

        viewListener.showLoadingDialog();
        Bundle param = new Bundle();
        param.putParcelable(PARAM_MARK_AS_READ, pass);
        actListener.markAsRead(param);
    }

    @Override
    public void markAsUnread() {
        viewListener.disableActions();
        final ActInboxMessagePass pass = getMoveInboxPass();

        viewListener.showLoadingDialog();
        Bundle param = new Bundle();
        param.putParcelable(PARAM_MARK_AS_UNREAD, pass);
        actListener.markAsUnread(param);
    }
}
