package com.tokopedia.inbox.inboxchat.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetMessageListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SearchMessageUseCase;
import com.tokopedia.inbox.inboxchat.presenter.subscriber.GetMessageSubscriber;
import com.tokopedia.inbox.inboxchat.presenter.subscriber.SearchMessageSubscriber;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;
import com.tokopedia.inbox.inboxmessage.activity.InboxMessageDetailActivity;
import com.tokopedia.inbox.inboxmessage.model.InboxMessagePass;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatPresenter extends BaseDaggerPresenter<InboxChatContract.View>
        implements InboxChatContract.Presenter, InboxMessageConstant{

    private final GetMessageListUseCase getMessageListUseCase;
    private final SearchMessageUseCase searchMessageUseCase;
    InboxMessagePass inboxMessagePass;
    PagingHandler pagingHandler;
    private boolean isRequesting;
    private InboxChatViewModel viewModel;

    @Inject
    InboxChatPresenter(GetMessageListUseCase getMessageListUseCase,
                       SearchMessageUseCase searchMessageUseCase){
        this.getMessageListUseCase = getMessageListUseCase;
        this.searchMessageUseCase = searchMessageUseCase;
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
    }

    public void getMessage() {
        showLoading();
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
            getView().getAdapter().showLoading(true);
        }
    }

    public void setResult(InboxChatViewModel result) {
        viewModel = result;

        if(pagingHandler.getPage()==1) {
            getView().getAdapter().setList(result.getList());
        }else {
            getView().getAdapter().addList(result.getList());
        }

        if (getView().getAdapter().getList().size() == 0) {
            getView().getAdapter().showEmptyFull(true);
        } else {
            getView().getAdapter().showEmptyFull(false);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        getMessageListUseCase.unsubscribe();
        searchMessageUseCase.unsubscribe();
    }


    public void onSelected(int position) {
        getView().getAdapter().addChecked(position);
        getView().setOptionsMenu();
    }

    public void onDeselect(int position) {
        getView().getAdapter().removeChecked(position);
        getView().setOptionsMenu();
    }

    public void goToDetailMessage(int position, ChatListViewModel listMessage) {
        Intent intent = new Intent(getView().getActivity(), InboxMessageDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NAV, getView().getArguments().getString(PARAM_NAV));
        bundle.putParcelable(PARAM_MESSAGE, null);
        bundle.putString(PARAM_MESSAGE_ID, String.valueOf(listMessage.getId()));
        bundle.putInt(PARAM_POSITION, position);
        bundle.putString(PARAM_SENDER_NAME, listMessage.getName());
        bundle.putString(PARAM_SENDER_TAG, listMessage.getLabel());
        bundle.putString(PARAM_SENDER_ID, String.valueOf(listMessage.getSenderId()));
        bundle.putInt(PARAM_MODE, viewModel.getMode());
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getView().startActivityForResult(intent, OPEN_DETAIL_MESSAGE);
        getView().overridePendingTransition(0, 0);
    }

    public void goToProfile(int userId) {

    }

    public void refreshData() {
        getView().finishContextMode();
        if (!isRequesting) {
            getView().getAdapter().showLoading(false);
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
                return R.menu.inbox_chat;
        }
    }

    public void initSearch(String keyword) {
        pagingHandler.resetPage();
        search(keyword);
    }
    public void search(String keyword) {
        if(!isRequesting) {
            searchMessageUseCase.execute(SearchMessageUseCase.generateParam(keyword, pagingHandler.getPage()), new SearchMessageSubscriber(getView(), this));
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
            getView().getAdapter().showLoading(true);
        }else {
            getView().getAdapter().showLoading(false);
        }
    }

    public void onLoadMore() {
        if (!isRequesting) {
            pagingHandler.nextPage();
            if(viewModel.getMode() == InboxChatViewModel.GET_CHAT_MODE){
                getMessage();
            }else if(viewModel.getMode() == InboxChatViewModel.SEARCH_CHAT_MODE) {
                search(getView().getKeyword());
            }
        }else{
            getView().finishLoading();
        }
    }

    public String getKeyword() {
        return getView().getKeyword();
    }
}
