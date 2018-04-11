package com.tokopedia.ride.chat.utils;

import android.content.Context;

/**
 * Created by sachinbansal on 2/13/18.
 */

public class ViewBuilder implements ViewBuilderInterface {

    /**
     * Returns a MessageView object which is used to display messages that the chat-ui
     * has received.
     *
     * @param context A context that is used to instantiate the view.
     * @return MessageView object for displaying received messages.
     */
    public UberSmsMessageView buildRecvView(Context context) {

        UberSmsMessageView view = new ItemRecvViewUberSms(context);
        return view;

    }

    /**
     * Returns a MessageView object which is used to display messages that the chat-ui
     * has sent.
     *
     * @param context A context that is used to instantiate the view.
     * @return MessageView object for displaying sent messages.
     */
    public UberSmsMessageView buildSentView(Context context) {

        UberSmsMessageView view = new ItemSentViewUberSms(context);
        return view;

    }

}

