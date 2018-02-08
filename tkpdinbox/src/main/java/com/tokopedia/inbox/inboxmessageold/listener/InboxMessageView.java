package com.tokopedia.inbox.inboxmessageold.listener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.inbox.inboxmessageold.adapter.InboxMessageAdapter;
import com.tokopedia.core.util.RefreshHandler;

/**
 * Created by Nisie on 5/10/16.
 */
public interface InboxMessageView {
    void disableActions();

    void enableActions();

    String getFilter();

    String getKeyword();

    InboxMessageAdapter getAdapter();

    void finishLoading();

    void removeError();

    void showError(String message);

    void setRetry(String message, View.OnClickListener listener);

    void setOptionsMenu();

    void showUndoSnackBar(String message, View.OnClickListener onUndo);

    void onSuccessMoveArchive(Bundle resultData);

    void onSuccessUndoMoveArchive(Bundle resultData);

    void onSuccessMoveToInbox(Bundle resultData);

    void onSuccessUndoMoveToInbox(Bundle resultData);

    void onSuccessDeleteMessage(Bundle resultData);

    void onSuccessUndoDeleteMessage(Bundle resultData);

    void onSuccessDeleteForever(Bundle resultData);

    void onFailedMoveArchive(Bundle resultData);

    void onFailedUndoMoveArchive(Bundle resultData);

    void onFailedMoveToInbox(Bundle resultData);

    void onFailedUndoMoveToInbox(Bundle resultData);

    void onFailedDeleteMessage(Bundle resultData);

    void onFailedUndoDeleteMessage(Bundle resultData);

    void onFailedDeleteForever(Bundle resultData);

    RefreshHandler getRefreshHandler();

    void showLoadingDialog();

    boolean getUserVisibleHint();

    Bundle getArguments();

    Activity getActivity();

    String getString(int resId);

    void startActivityForResult(Intent intent, int requestCode);

    void startActivity(Intent intent);

    void finishContextMode();

    void showEmptyState();

    void showEmptyState(String error);

    boolean hasRetry();

    void setMustRefresh(boolean isMustRefresh);

    void onFailedMarkAsRead(Bundle resultData);

    void onFailedMarkAsUnread(Bundle resultData);

    void onSuccessMarkAsRead(Bundle resultData);

    void onSuccessMarkAsUnread(Bundle resultData);
}
