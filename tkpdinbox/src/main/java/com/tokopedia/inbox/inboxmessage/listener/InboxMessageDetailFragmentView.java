package com.tokopedia.inbox.inboxmessage.listener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxmessage.adapter.InboxMessageDetailAdapter;
import com.tokopedia.inbox.inboxmessage.model.inboxmessagedetail.InboxMessageDetail;
import com.tokopedia.core.util.RefreshHandler;

/**
 * Created by Nisie on 5/19/16.
 */
public interface InboxMessageDetailFragmentView {
    InboxMessageDetailAdapter getAdapter();

    void setHeader();

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

    void onSuccessSendReply(ReplyActionData replyData, String reply);

    void setOnlineDesc(String s);

    void setTextAreaReply(boolean textAreaReply);
}
