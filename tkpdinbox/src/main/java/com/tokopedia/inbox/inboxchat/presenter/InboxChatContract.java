package com.tokopedia.inbox.inboxchat.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatAdapter;
import com.tokopedia.inbox.inboxmessage.adapter.InboxMessageAdapter;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.inbox.inboxchat.adapter.DummyAdapter;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatAdapter;
import com.tokopedia.inbox.inboxmessage.adapter.InboxMessageAdapter;

import java.util.Arrays;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatContract {

    public interface View extends CustomerView{

        String getNav();

        void enableActions();

        RefreshHandler getRefreshHandler();

        InboxChatAdapter getAdapter();

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
    }

    interface Presenter extends CustomerPresenter<View>{
    }
}
