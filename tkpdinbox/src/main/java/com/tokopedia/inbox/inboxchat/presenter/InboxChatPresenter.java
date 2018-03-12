package com.tokopedia.inbox.inboxchat.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity;
import com.tokopedia.inbox.inboxchat.analytics.TopChatTrackingEventLabel;
import com.tokopedia.inbox.inboxchat.domain.usecase.DeleteMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SearchMessageUseCase;
import com.tokopedia.inbox.inboxchat.presenter.subscriber.DeleteMessageSubscriber;
import com.tokopedia.inbox.inboxchat.presenter.subscriber.GetMessageSubscriber;
import com.tokopedia.inbox.inboxchat.presenter.subscriber.SearchMessageSubscriber;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessage.model.InboxMessagePass;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import static com.tokopedia.inbox.inboxchat.domain.usecase.SearchMessageUseCase.PARAM_BY_REPLY;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatPresenter extends BaseDaggerPresenter<InboxChatContract.View>
        implements InboxChatContract.Presenter, InboxMessageConstant {

    private GetMessageListUseCase getMessageListUseCase;
    private SearchMessageUseCase searchMessageUseCase;
    private DeleteMessageListUseCase deleteMessageListUseCase;
    InboxMessagePass inboxMessagePass;
    PagingHandler pagingHandler;
    private boolean isRequesting;
    private InboxChatViewModel viewModel;
    private int contactSize;
    private int chatSize;
    private List<Visitable> listFetchCache;
    private OkHttpClient client;
    private String magicString;
    private ChatWebSocketListenerImpl listener;
    private WebSocket ws;
    private int attempt;
    boolean inActionMode;
    private CountDownTimer countDownTimer;

    @Inject
    InboxChatPresenter(GetMessageListUseCase getMessageListUseCase,
                       SearchMessageUseCase searchMessageUseCase,
                       DeleteMessageListUseCase deleteMessageListUseCase) {
        this.getMessageListUseCase = getMessageListUseCase;
        this.searchMessageUseCase = searchMessageUseCase;
        this.deleteMessageListUseCase = deleteMessageListUseCase;
    }

    @Override
    public void attachView(InboxChatContract.View view) {
        super.attachView(view);
        initialize();
    }

    private void initialize() {
        this.inboxMessagePass = new InboxMessagePass();
        this.inboxMessagePass.setNav(getView().getNav());
        this.pagingHandler = new PagingHandler();
        isRequesting = false;
        inActionMode = false;
        contactSize = 0;
        chatSize = 0;
        attempt = 0;

        client = new OkHttpClient();
        magicString = TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN + TkpdBaseURL.Chat.CHAT_WEBSOCKET +
                "?os_type=1" +
                "&device_id=" + GCMHandler.getRegistrationId(getView().getContext()) +
                "&user_id=" + SessionHandler.getLoginID(getView().getContext());
        listener = new ChatWebSocketListenerImpl(getView().getInterface());

        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                createWebSocket();
            }
        };

        createWebSocket();
    }

    public void getMessage() {
        if (viewModel != null) viewModel.setKeyword("");
        if (!getView().getAdapter().containLoading()) {
            showLoading();
        }
        getView().disableActions();
        getView().removeError();
        isRequesting = true;
        getMessageListUseCase.execute(GetMessageListUseCase.generateParam(inboxMessagePass, pagingHandler.getPage()), new GetMessageSubscriber(getView(), this));
    }

    private void showLoading() {
        if (pagingHandler.getPage() == 1 && getView().getAdapter().getList().size() != 0) {
            getView().getRefreshHandler().setRefreshing(true);
            getView().getRefreshHandler().setIsRefreshing(true);
        } else if (pagingHandler.getPage() == 1 && getView().getAdapter().getList().size() == 0 && !getView().getRefreshHandler().isRefreshing()) {
            getView().getAdapter().showLoadingFull(true);
        } else if (!getView().getRefreshHandler().isRefreshing()) {
            getView().getAdapter().showLoading();
        }
    }

    private InboxChatViewModel modifyViewModel(InboxChatViewModel result) {
        String temp = "";
        if (viewModel != null) {
            temp = viewModel.getKeyword();
        }
        viewModel = result;
        viewModel.setKeyword(temp);
        return viewModel;
    }


    public void setResultFetch(InboxChatViewModel result) {
        viewModel = modifyViewModel(result);

        if (pagingHandler.getPage() == 1) {
            contactSize = 0;
            chatSize = 0;
            getView().getAdapter().setList(result.getListReplies());
            chatSize += result.getChatSize();
//            getView().getAdapter().addList(contactSize, result.getListContact());
//            contactSize += result.getContactSize();
        } else {
            getView().getAdapter().addList(result.getListReplies());
            chatSize += result.getChatSize();
//            getView().getAdapter().addList(contactSize, result.getListContact());
//            contactSize += result.getContactSize();
        }

        getView().getAdapter().showEmptyFull(false);
        getView().getAdapter().showEmptySearch(false);

        if (getView().getAdapter().getList().size() == 0) {
            if (result.getMode() == InboxChatViewModel.SEARCH_CHAT_MODE) {
                getView().getAdapter().showEmptySearch(true);
            } else if (result.getMode() == InboxChatViewModel.GET_CHAT_MODE) {
                getView().getAdapter().showEmptyFull(true);
            }
        }

        if (!result.isHasNext() && result.isHasTimeMachine()) {
            getView().addTimeMachine();
        }
    }

    public void setCache(List<Visitable> list) {
        this.listFetchCache = new ArrayList<>();
        this.listFetchCache.addAll(list);
    }

    public void resetSearch() {
        viewModel.setMode(InboxChatViewModel.GET_CHAT_MODE);
        viewModel.setKeyword("");
        getView().getAdapter().setList(listFetchCache);
        chatSize = listFetchCache.size();
        contactSize = 0;
    }

    public void setResultSearch(InboxChatViewModel result) {
        viewModel = modifyViewModel(result);

        if (pagingHandler.getPage() == 1) {
            contactSize = 0;
            chatSize = 0;
            getView().getAdapter().setList(result.getListReplies());
            chatSize += result.getChatSize();
            getView().getAdapter().addList(contactSize, result.getListContact());
            contactSize += result.getContactSize();
        } else {
            getView().getAdapter().addList(result.getListReplies());
            chatSize += result.getChatSize();
            getView().getAdapter().addList(contactSize, result.getListContact());
            contactSize += result.getContactSize();
        }


        getView().getAdapter().showEmptyFull(false);
        getView().getAdapter().showEmptySearch(false);

        if (getView().getAdapter().getList().size() == 0) {
            if (result.getMode() == InboxChatViewModel.SEARCH_CHAT_MODE) {
                getView().getAdapter().showEmptySearch(true);
            } else if (result.getMode() == InboxChatViewModel.GET_CHAT_MODE) {
                getView().getAdapter().showEmptyFull(true);
            }
        }

        if (!result.isHasNext() && result.isHasTimeMachine()) {
            getView().addTimeMachine();
        }

        getView().setMenuEnabled(false);
    }


    @Override
    public void detachView() {
        super.detachView();
        getMessageListUseCase.unsubscribe();
        searchMessageUseCase.unsubscribe();
        deleteMessageListUseCase.unsubscribe();
        countDownTimer.cancel();
    }


    public void onSelected(int position) {
        getView().getAdapter().addChecked(position);
        getView().setOptionsMenuFromSelect();
    }

    public void onDeselect(int position) {
        getView().getAdapter().removeChecked(position);
        getView().setOptionsMenuFromSelect();
    }

    public int getSelected() {
        return getView().getAdapter().getListMove().size();
    }

    public void goToDetailMessage(int position, ChatListViewModel listMessage) {
        ws.close(1000, "");
        getView().dropKeyboard();

        UnifyTracking.eventOpenTopChat(TopChatTrackingEventLabel.Category.INBOX_CHAT,
                TopChatTrackingEventLabel.Action.INBOX_CHAT_CLICK,
                TopChatTrackingEventLabel.Name.INBOX_CHAT);

        getView().getAdapter().notifyItemChanged(position);
        Intent intent = ChatRoomActivity.getCallingIntent(getView().getActivity(),
                getView().getArguments().getString(PARAM_NAV),
                String.valueOf(listMessage.getId()),
                position,
                listMessage.getName(),
                listMessage.getLabel(),
                listMessage.getSenderId(),
                listMessage.getRole(),
                viewModel.getMode(),
                viewModel.getKeyword(),
                listMessage.getImage());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getView().startActivityForResult(intent, OPEN_DETAIL_MESSAGE);
        getView().overridePendingTransition(0, 0);
    }

    public void goToProfile(int userId) {
        if (getView().getActivity().getApplicationContext() instanceof TkpdInboxRouter) {
            getView().startActivity(
                    ((TkpdInboxRouter) getView().getActivity().getApplicationContext())
                            .getTopProfileIntent(getView().getActivity(), String.valueOf(userId))
            );
        }
    }

    public void goToShop(int shopId) {
        Intent intent = new Intent(getView().getActivity(), ShopInfoActivity.class);
        Bundle bundle = ShopInfoActivity.createBundle(String.valueOf(shopId), "");
        intent.putExtras(bundle);
        getView().startActivity(intent);
    }

    public void refreshData() {
        getView().finishContextMode();
        if (!isRequesting) {
            getView().getAdapter().removeLoading();
            pagingHandler.resetPage();
            inboxMessagePass.setPage(String.valueOf(pagingHandler.getPage()));
            getView().getRefreshHandler().setRefreshing(true);
            getView().getRefreshHandler().setIsRefreshing(true);
            getMessage();
        } else {
            getView().getRefreshHandler().finishRefresh();
        }
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


    public boolean hasActionListener() {
        return true;
    }

    public void moveViewToTop() {
        getView().moveViewToTop();
    }

    public int getMenuID() {
        switch (getView().getArguments().getString(PARAM_NAV, "")) {
            case MESSAGE_ARCHIVE:
                return R.menu.inbox_message_archive;
            case MESSAGE_TRASH:
                return R.menu.inbox_message_trash;
            default:
                return R.menu.inbox_chat_delete;
        }
    }

    public void initSearch(String keyword) {
        pagingHandler.resetPage();
        searchAll(keyword);
    }

    private void searchAll(String keyword) {
        if (!isRequesting) {
            if (viewModel != null) viewModel.setKeyword(keyword);
            searchMessageUseCase.execute(SearchMessageUseCase.generateParam(keyword), new SearchMessageSubscriber(getView(), this));
            isRequesting = true;
        }
    }

    private void searchReplies(String keyword) {
        search(keyword, PARAM_BY_REPLY);
    }

    public void search(String keyword, String by) {
        if (!isRequesting) {
            if (viewModel != null) viewModel.setKeyword(keyword);
            searchMessageUseCase.execute(SearchMessageUseCase.generateParam(keyword, pagingHandler.getPage(), by), new SearchMessageSubscriber(getView(), this));
            isRequesting = true;
        }
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean requesting) {
        isRequesting = requesting;
    }

    public void prepareNextPage(boolean hasNext) {
        if (hasNext) {
            getView().getAdapter().showLoading();
        }
    }

    public void onLoadMore() {
        if (!isRequesting) {
            pagingHandler.nextPage();
            if (viewModel.getMode() == InboxChatViewModel.GET_CHAT_MODE) {
                getMessage();
            } else if (viewModel.getMode() == InboxChatViewModel.SEARCH_CHAT_MODE) {
                searchReplies(getView().getKeyword());
            }
        } else {
            getView().finishLoading();
        }
    }

    public String getKeyword() {
        return getView().getKeyword();
    }

    public void deleteMessage(List<Pair> listMove) {
        deleteMessageListUseCase.execute(DeleteMessageListUseCase.generateParam(listMove)
                , new DeleteMessageSubscriber(listMove, getView(), this));
    }


    @Override
    public void createWebSocket() {
        try {
            Request request = new Request.Builder().url(magicString)
                    .header("Origin", TkpdBaseURL.WEB_DOMAIN)
                    .build();
            ws = client.newWebSocket(request, listener);
            attempt++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recreateWebSocket() {
        countDownTimer.start();
    }

    @Override
    public void resetAttempt() {
        attempt = 0;
    }

    @Override
    public void closeWebsocket() {
        client.dispatcher().executorService().shutdown();
        ws.close(1000, "Goodbye !");
    }

    public boolean isInActionMode() {
        return inActionMode;
    }

    public void setInActionMode(boolean inActionMode) {
        this.inActionMode = inActionMode;
    }

    public void setError(String errorMessage) {
        if (pagingHandler.getPage() == 1) {
            getView().showErrorFull(errorMessage);
        } else {
            getView().showError(errorMessage);
        }
    }
}
