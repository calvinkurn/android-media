package com.tokopedia.inbox.inboxmessage.presenter;

import android.content.Intent;

import com.tokopedia.inbox.inboxchat.domain.model.message.ListMessage;
import com.tokopedia.inbox.inboxmessage.model.ActInboxMessagePass;
import com.tokopedia.inbox.inboxmessage.model.InboxMessagePass;
import com.tokopedia.inbox.inboxmessage.model.inboxmessage.InboxMessageItem;

import java.util.ArrayList;

/**
 * Created by Nisie on 5/9/16.
 */
public interface InboxMessageFragmentPresenter {
    void initData();

    void initAnalytics();

    void refreshData();

    void loadMore(int lastItemPosition, int visibleItem);

    boolean hasNextPage();

    boolean isOnLastPosition(int itemPosition, int visibleItem);

    void getInboxMessage();

    boolean isLoading();

    void onDeselect(int position);

    void onSelected(int position);

    void moveInbox(String act);

    int getMenuID();

    void onDestroyView();

    void setUserVisibleHint(boolean isVisibleToUser, boolean isMustRefresh);

    void goToDetailMessage(int position, ListMessage messageId);

    void goToProfile(int userId);

    void generateSearchParam();

    void archiveMessages(ActInboxMessagePass pass);

    void undoArchiveMessage(ArrayList<InboxMessageItem> listMove);

    void moveToInbox(ActInboxMessagePass pass);

    void undoMoveToInbox(final ArrayList<InboxMessageItem> listMove);

    void deleteMessages(ActInboxMessagePass pass);

    void undoDeleteMessage(final ArrayList<InboxMessageItem> listMove);

    void deleteForever(ActInboxMessagePass pass);

    void setNav(String nav);

    void setMessageRead(Intent data);

    boolean hasActionListener();

    void markAsRead();

    void markAsUnread();

    InboxMessagePass getFilterParam();

    void restoreFilterBundle(InboxMessagePass inboxMessagePass);
}
