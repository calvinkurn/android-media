package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewholder.movement.ChatLinkHandlerMovementMethod;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.message.MessageViewModel;

/**
 * @author by nisie on 5/16/18.
 */
public class MessageViewHolder extends BaseChatViewHolder<MessageViewModel> {

    private static final String ROLE_USER = "User";

    Context context;
    View view;
    TextView message;
    ImageView chatStatus;
    private View chatBalloon;
    private TextView name;
    private TextView label;
    private TextView dot;

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_message_chat;

    public MessageViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        this.context = itemView.getContext();
        message = itemView.findViewById(R.id.message);
        message.setMovementMethod(new ChatLinkHandlerMovementMethod(viewListener));
        chatStatus = itemView.findViewById(R.id.chat_status);
        name = itemView.findViewById(R.id.name);
        label = itemView.findViewById(R.id.label);
        dot = itemView.findViewById(R.id.dot);
        chatBalloon = itemView.findViewById(R.id.main);

    }

    @Override
    public void bind(MessageViewModel element) {
        super.bind(element);

        message.setText(MethodChecker.fromHtml(element.getMessage()));

        setupChatBubbleAlignment(chatBalloon, element);
        setReadStatus(element);
        setRole(element);
    }

    private void setupChatBubbleAlignment(View chatBalloon, MessageViewModel element) {
        if (element.isSender()) {
            setChatRight(chatBalloon, element);
        } else {
            setChatLeft(chatBalloon, element);
        }
    }

    private void setChatLeft(View chatBalloon, MessageViewModel element) {
        chatBalloon.setBackground(context.getResources().getDrawable(R.drawable
                .left_bubble));
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, chatBalloon);
        message.setTextColor(MethodChecker.getColor(context, R.color.black_70));
        message.setLinkTextColor(MethodChecker.getColor(context, R.color.black_70));
        chatStatus.setVisibility(View.GONE);
    }

    private void setAlignParent(int alignment, View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        params.addRule(alignment);
        view.setLayoutParams(params);
    }

    private void setChatRight(View chatBalloon, MessageViewModel element) {
        chatBalloon.setBackground(context.getResources().getDrawable(R.drawable
                .attach_product_right_bubble));
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, chatBalloon);
        message.setTextColor(MethodChecker.getColor(context, R.color.white));
        message.setLinkTextColor(MethodChecker.getColor(context, R.color.white));
        chatStatus.setVisibility(View.VISIBLE);
    }

    private void setRole(MessageViewModel element) {
        if (element.getFromRole() != null) {
            if (element.getFromRole().toLowerCase().equals(ROLE_USER.toLowerCase())) {
                name.setVisibility(View.GONE);
                label.setVisibility(View.GONE);
                dot.setVisibility(View.GONE);
            } else {
                name.setText(element.getFrom());
                label.setText(element.getFromRole());
                name.setVisibility(View.VISIBLE);
                dot.setVisibility(View.VISIBLE);
                label.setVisibility(View.VISIBLE);
            }
        } else {
            name.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
            dot.setVisibility(View.GONE);
        }
    }

    private void setReadStatus(MessageViewModel element) {
        int imageResource;

        if (element.isRead()) {
            imageResource = R.drawable.ic_chat_read;
        } else {
            imageResource = R.drawable.ic_chat_unread;
        }
        if (element.isDummy()) {
            imageResource = R.drawable.ic_chat_pending;
        }

        chatStatus.setImageResource(imageResource);

    }
}
