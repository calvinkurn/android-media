package com.tokopedia.ride.chat.utils;

import android.content.Context;
import android.view.View;

/**
 * Created by sachinbansal on 2/13/18.
 */

class MessageViewHolder {
    public final int STATUS_SENT = 0;
    public final int STATUS_RECEIVED = 1;

    View row;
    Context context;

    private MessageView messageView;

    public MessageViewHolder(View convertView) {
        row = convertView;
        context = row.getContext();
        messageView = (MessageView) convertView;
    }

    public void setMessage(String message) {

        messageView.setMessage(message);

    }

    public void setTimestamp(String timestamp) {

        messageView.setTimestamp(timestamp);

    }

    public MessageView getMessageView() {
        return messageView;
    }
}
