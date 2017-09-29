package com.tokopedia.inbox.inboxchat.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatAdapter;
import com.tokopedia.inbox.inboxmessage.adapter.InboxMessageAdapter;

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
    }

    interface Presenter extends CustomerPresenter<View>{
    }
}
