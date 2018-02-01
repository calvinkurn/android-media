package com.tokopedia.inbox.inboxchat.helper;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.model.ListReplyViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.reply.FallbackAttachment;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;

/**
 * Created by stevenfredian on 12/29/17.
 */

public class AttachmentChatHelper {

    private static final String ROLE_ADMINISTRATOR = "Administrator";
    private static final String ROLE_OFFICIAL = "Official";
    private static final String DEFAULT = "1";
    public static final String IMAGE_ATTACHED = "2";

    public void parse(ImageView view, TextView message, ListReplyViewModel element, ChatRoomContract.View viewListener, boolean dummy) {
        Attachment attachment = element.getAttachment();
        String role = element.getRole();
        String msg = element.getMsg();
        if (element.getAttachment() != null) {
            switch (attachment.getType()) {
                case DEFAULT:
                    parseType(view, message, attachment, role, msg, viewListener);
                    break;
                case IMAGE_ATTACHED:
                    parseAttachedImage(view, message, attachment, role, msg, viewListener, dummy);
                    break;
                default:
                    parseDefaultType(view, message, attachment, viewListener);
                    break;
            }
        }else {
            message.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        }
    }

    public void parse(ImageView view, TextView message, ListReplyViewModel element, ChatRoomContract.View viewListener) {
        parse(view, message, element, viewListener, false);
    }

    private void parseAttachedImage(ImageView view, TextView message, final Attachment attachment, String role, String msg, final ChatRoomContract.View viewListener, boolean dummy) {
        if (attachment.getAttributes().getImageUrl() != null) {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.onGoToGallery(attachment);
                }
            });

            if(dummy) {
                ImageHandler.loadImageChatBlurred(view, attachment.getAttributes().getImageUrl(), R.drawable.product_no_photo_default);
            }else {
                ImageHandler.loadImageChat(view, attachment.getAttributes().getImageUrl(), R.drawable.product_no_photo_default);
            }
            message.setVisibility(View.GONE);
        }
    }

    private void parseDefaultType(ImageView view, TextView message, Attachment attachment, ChatRoomContract.View viewListener) {
        view.setVisibility(View.GONE);
        message.setVisibility(View.VISIBLE);
        setMessage(attachment, viewListener, message);
    }

    private void parseType(final ImageView view, TextView message, final Attachment attachment, String role, String msg, final ChatRoomContract.View viewListener) {
        message.setVisibility(View.VISIBLE);
        if (attachment.getAttributes().getImageUrl() != null) {
            view.setVisibility(View.VISIBLE);
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().height = view.getLayoutParams().width;
            ImageHandler.loadImageChat(view, attachment.getAttributes().getImageUrl(), R.drawable.product_no_photo_default);
        } else {
            view.setVisibility(View.GONE);
        }

        boolean isAdmin = role.toLowerCase().contains(ROLE_ADMINISTRATOR.toLowerCase()) || role.toLowerCase().contains(ROLE_OFFICIAL.toLowerCase());
        if (isAdmin) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (attachment != null && attachment.getFallbackAttachment() != null) {
                        viewListener.onGoToWebView(attachment.getAttributes().getUrl());
                    }
                }
            });
        } else {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.onGoToGallery(attachment);
                }
            });
        }

        setMessage(attachment, viewListener, message);
    }

    private void setMessage(Attachment attachment, final ChatRoomContract.View viewListener, final TextView message) {
        if (attachment.getFallbackAttachment().getMessage() != null) {
            final FallbackAttachment fallback = attachment.getFallbackAttachment();
            String string = String.format("%s\n%s", fallback.getMessage(), fallback.getSpan());

            Spannable spannable = new SpannableString(string);

            spannable.setSpan(new ClickableSpan() {
                                  @Override
                                  public void onClick(View view) {
                                      viewListener.onGoToWebView(fallback.getUrl());
                                  }

                                  @Override
                                  public void updateDrawState(TextPaint ds) {
                                      ds.setColor(message.getContext().getResources().getColor(com.tokopedia.core.R.color.medium_green));
                                      ds.setUnderlineText(false);
                                  }
                              }
                    , string.indexOf(fallback.getSpan())
                    , string.length()
                    , 0);

            message.setText(spannable, TextView.BufferType.SPANNABLE);

        }
    }
}
