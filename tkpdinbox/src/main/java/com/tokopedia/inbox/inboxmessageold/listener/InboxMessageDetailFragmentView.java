package com.tokopedia.inbox.inboxmessageold.listener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.inbox.inboxmessageold.adapter.InboxMessageDetailAdapter;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetail;
import com.tokopedia.core.util.RefreshHandler;

/**
 * Created by Nisie on 5/19/16.
 */
public interface InboxMessageDetailFragmentView {
    InboxMessageDetailAdapter getAdapter();

    void setHeader(InboxMessageDetail inboxMessageDetail);

    void finishLoading();

    RefreshHandler getRefreshHandler();

    void showUndoFlagSpam(View.OnClickListener listener);

    EditText getReplyMessage();

    void showError(String error);

    void addUrlToReply(String url);

    void scrollToBottom();

    void setViewEnabled(boolean isEnabled);

    void showSuccessUndoSpam(int position);

    void setRetry(String string, View.OnClickListener listener);

    void onSuccessSendReply(Bundle resultData);

    void onSuccessFlagSpam(Bundle resultData);

    void onSuccessUndoFlagSpam(Bundle resultData);

    void onFailedSendReply(Bundle resultData);

    void onFailedFlagSpam(Bundle resultData);

    void onFailedUndoFlagSpam(Bundle resultData);

    void addTempMessage();

    Bundle getArguments();

    Activity getActivity();

    String getString(int resId);

    void setHasReputation(String positivePercentage);

    void setNoReputation();

    void showEmptyState();

    void showEmptyState(String error);
}
