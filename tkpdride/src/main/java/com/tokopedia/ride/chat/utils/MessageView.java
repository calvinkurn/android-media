package com.tokopedia.ride.chat.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by sachinbansal on 2/13/18.
 */

public abstract class MessageView extends FrameLayout {

    /**
     * Method to set the messages text in the view so it can be displayed on the screen.
     *
     * @param message The message that you want to be displayed.
     */
    public abstract void setMessage(String message);

    /**
     * Method to set the timestamp that the message was received or sent on the screen.
     *
     * @param timestamp The timestamp that you want to be displayed.
     */
    public abstract void setTimestamp(String timestamp);

    /**
     * Constructs a new message view.
     *
     * @param context
     */
    public MessageView(Context context) {
        super(context);
    }

    /**
     * Constructs a new message view with attributes, this constructor is used when we create a
     * message view using XML.
     *
     * @param context
     * @param attrs
     */
    public MessageView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

}
