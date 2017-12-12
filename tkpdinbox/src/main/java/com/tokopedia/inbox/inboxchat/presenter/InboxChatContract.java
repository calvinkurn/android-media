package com.tokopedia.inbox.inboxchat.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.adapter.NewInboxChatAdapter;
import com.tokopedia.inbox.inboxchat.viewmodel.DeleteChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import java.util.List;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatContract {

    public interface View extends CustomerView{

        String getNav();

        void enableActions();

        RefreshHandler getRefreshHandler();

        NewInboxChatAdapter getAdapter();

        void finishLoading();

        void setMustRefresh(boolean b);

        void removeError();

        void disableActions();

        void finishContextMode();

        boolean hasRetry();

        Context getActivity();

        void startActivity(Intent instance);

        void startActivityForResult(Intent intent, int openDetailMessage);

        void overridePendingTransition(int i, int i1);

        String getFilter();

        String getKeyword();

        void showEmptyState(String localizedMessage);

        void showError(String localizedMessage);

        Bundle getArguments();

        void moveViewToTop();

        void setOptionsMenuFromSelect();

        void finishSearch();

        void addTimeMachine();

        void onGoToTimeMachine(String url);

        void removeList(List<Pair> originList, List<DeleteChatViewModel> list);

        void setResultSearch(InboxChatViewModel inboxChatViewModel);

        void setResultFetch(InboxChatViewModel messageData);

        Context getContext();

        WebSocketInterface getInterface();

        void notifyConnectionWebSocket();

        void showErrorWarningDelete(int maxMessageDelete);

        void showErrorFull(String errorMessage);

        void dropKeyboard();

        void onErrorDeleteMessage(String errorMessage);

        void setMenuEnabled(boolean b);

        void saveResult();
    }

    interface Presenter extends CustomerPresenter<View>{
        void createWebSocket();

        void resetAttempt();


        void closeWebsocket();

    }
}
