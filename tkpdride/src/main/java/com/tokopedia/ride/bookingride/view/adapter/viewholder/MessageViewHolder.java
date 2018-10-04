package com.tokopedia.ride.bookingride.view.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.tokopedia.ride.chat.utils.UberSmsMessageView;


public class MessageViewHolder {
    public final int STATUS_SENT = 0;
    public final int STATUS_RECEIVED = 1;

    View row;
    Context context;

    private UberSmsMessageView uberSmsMessageView;

    public MessageViewHolder(View convertView) {
        row = convertView;
        context = row.getContext();
        uberSmsMessageView = (UberSmsMessageView) convertView;
    }

    public void setMessage(String message) {

        uberSmsMessageView.setMessage(message);

    }

    public void setTimestamp(String timestamp) {

        uberSmsMessageView.setTimestamp(timestamp);

    }

    public UberSmsMessageView getUberSmsMessageView() {
        return uberSmsMessageView;
    }
}
