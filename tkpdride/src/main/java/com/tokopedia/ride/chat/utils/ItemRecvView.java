package com.tokopedia.ride.chat.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.tokopedia.ride.R;

/**
 * Created by sachinbansal on 2/13/18.
 */

public class ItemRecvView extends MessageView {

    private TextView messageTextView, timestampTextView;

    /**
     * Method to set the messages text in the view so it can be displayed on the screen.
     *
     * @param message The message that you want to be displayed.
     */
    public void setMessage(String message) {

        if (messageTextView == null) {

            messageTextView = (TextView) findViewById(R.id.message_text_view);

        }

        messageTextView.setText(message);


    }

    /**
     * Method to set the timestamp that the message was received or sent on the screen.
     *
     * @param timestamp The timestamp that you want to be displayed.
     */
    public void setTimestamp(String timestamp) {

        if (timestampTextView == null) {

            timestampTextView = (TextView) findViewById(R.id.timestamp_text_view);

        }

        timestampTextView.setText(timestamp);

    }

    /**
     * Constructs a new message view.
     *
     * @param context
     */
    public ItemRecvView(Context context) {

        super(context);
        initializeView(context);

    }

    /**
     * Constructs a new message view with attributes, this constructor is used when we create a
     * message view using XML.
     *
     * @param context
     * @param attrs
     */
    public ItemRecvView(Context context, AttributeSet attrs) {

        super(context, attrs);
        initializeView(context);

    }

    /**
     * Inflates the view so it can be displayed and grabs any child views that we may require
     * later on.
     *
     * @param context The context that is used to inflate the view.
     */
    private void initializeView(Context context) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chat_item_rcv, this);

        this.messageTextView = (TextView) findViewById(R.id.message_text_view);
        this.timestampTextView = (TextView) findViewById(R.id.timestamp_text_view);

    }

}

