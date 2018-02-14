package com.tokopedia.inbox.inboxchat.helper;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.reply.FallbackAttachment;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;

/**
 * Created by stevenfredian on 12/29/17.
 */

public class AttachmentChatHelper {

    private static final String ROLE_ADMINISTRATOR = "Administrator";
    private static final String ROLE_OFFICIAL = "Official";
    private static final String DEFAULT = "1";

    public void parse(ImageView view, TextView message, final Attachment attachment, String role, String msg, final ChatRoomContract.View viewListener) {
        if (attachment != null) {
            switch (attachment.getType()) {
                case DEFAULT:
                    parseType(view, message, attachment, role, msg, viewListener);
                    break;
                default:
                    parseDefaultType(message, attachment, viewListener);
                    break;
            }
        }
    }

    private void parseDefaultType(TextView message, Attachment attachment, ChatRoomContract.View viewListener) {
        setMessage(attachment, viewListener, message);
    }

    private void parseType(final ImageView view, TextView message, final Attachment attachment, String role, String msg, final ChatRoomContract.View viewListener) {
        if (attachment.getAttributes().getImageUrl() != null) {
            view.setVisibility(View.VISIBLE);
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
                        viewListener.onGoToWebView(attachment.getAttributes().getUrl(), attachment.getId());
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

    private void setMessage(final Attachment attachment, final ChatRoomContract.View viewListener, final TextView message) {
        if (attachment.getFallbackAttachment().getMessage() != null) {
            final FallbackAttachment fallback = attachment.getFallbackAttachment();

            Spannable spannable = new SpannableString(fallback.getMessage());

            if (hasFallback(fallback.getSpan())) {
                String string = String.format("%s\n%s", fallback.getMessage(), fallback.getSpan());
                spannable = new SpannableString(string);
                spannable.setSpan(new ClickableSpan() {
                                      @Override
                                      public void onClick(View view) {
                                          viewListener.onGoToWebView(fallback.getUrl(), attachment.getId());
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
            }

            message.setText(spannable, TextView.BufferType.SPANNABLE);

        }
    }

    private boolean hasFallback(String fallback) {
        return fallback != null;
    }
}
