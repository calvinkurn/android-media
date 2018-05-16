package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.helper.ChatGlideImageRequestListener;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.imageupload.ImageUploadViewModel;

/**
 * Created by stevenfredian on 11/28/17.
 */

public class ImageUploadViewHolder extends BaseChatViewHolder<ImageUploadViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_image_upload;

    private static final String ROLE_USER = "User";

    private View progressBarSendImage;
    private ImageView chatStatus;
    private View chatBalloon;
    private TextView name;
    private TextView label;
    private TextView dot;
    private ImageView attachment;
    private ImageView action;

    public ImageUploadViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        chatStatus = itemView.findViewById(R.id.chat_status);
        name = itemView.findViewById(R.id.name);
        label = itemView.findViewById(R.id.label);
        dot = itemView.findViewById(R.id.dot);
        attachment = itemView.findViewById(R.id.image);
        action = itemView.findViewById(R.id.left_action);
        progressBarSendImage = itemView.findViewById(R.id.progress_bar);
        chatBalloon = itemView.findViewById(R.id.card_group_chat_message);

    }

    @Override
    public void bind(final ImageUploadViewModel element) {
        super.bind(element);

        prerequisiteUISetup(element);
        setupChatBubbleAlignment(chatBalloon, element);

        if (element.isRetry()) {
            setRetryView(element);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToImagePreview(element.getImageUrl(), element.getReplyTime());
            }
        });

        if (element.isDummy()) {
            setVisibility(progressBarSendImage, View.VISIBLE);
            ImageHandler.loadImageChatBlurred(attachment, element.getImageUrl(), new
                    ChatGlideImageRequestListener());
        } else {
            setVisibility(progressBarSendImage, View.GONE);
            ImageHandler.loadImageChat(attachment, element.getImageUrl(),
                    new ChatGlideImageRequestListener());
        }

        setVisibility(attachment, View.VISIBLE);

    }

    private void setupChatBubbleAlignment(View chatBalloon, ImageUploadViewModel element) {
        if (element.isSender()) {
            setChatRight(chatBalloon);
        } else {
            setChatLeft(chatBalloon);
        }
    }

    private void setChatLeft(View chatBalloon) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, chatBalloon);
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, hour);
        chatStatus.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        label.setVisibility(View.GONE);
        dot.setVisibility(View.GONE);
    }

    private void setChatRight(View chatBalloon) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, chatBalloon);
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, hour);
        chatStatus.setVisibility(View.VISIBLE);
    }

    private void setAlignParent(int alignment, View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        params.addRule(alignment);
        view.setLayoutParams(params);
    }

    private void setRetryView(final ImageUploadViewModel element) {
        setVisibility(action, View.VISIBLE);
        setClickListener(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onRetrySendImage(element);
            }
        });
        setVisibility(hour, View.GONE);
        setVisibility(chatStatus, View.GONE);
        setVisibility(progressBarSendImage, View.GONE);
    }

    protected void prerequisiteUISetup(ImageUploadViewModel element) {
        action.setVisibility(View.GONE);
        progressBarSendImage.setVisibility(View.GONE);

        setReadStatus(element);

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

    public void setReadStatus(ImageUploadViewModel element) {
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


    private void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private void setClickListener(View view, View.OnClickListener onClickListener) {
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }
}
