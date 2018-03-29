package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChatroomContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;

/**
 * @author by nisie on 2/27/18.
 */

public class GroupChatPointsViewHolder extends BaseChatViewHolder<GroupChatPointsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_groupchat_points;

    TextView messageView;
    Context context;
    private final ChatroomContract.View.VoteAnnouncementViewHolderListener listener;

    public GroupChatPointsViewHolder(View itemView, ChatroomContract.View.VoteAnnouncementViewHolderListener imageListener) {
        super(itemView);
        messageView = itemView.findViewById(R.id.text);
        listener = imageListener;
    }

    @Override
    public void bind(final GroupChatPointsViewModel element) {
        super.bind(element);
        context = itemView.getContext();
        String text = element.getText();
        String span = element.getSpan();
        Spannable spannable = new SpannableString(element.getSpan());
        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {
//                                  listener.onRedirectUrl(element.getUrl());
                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(MethodChecker.getColor(context, R.color.tkpd_main_green));
                              }
                          }
                , text.indexOf(span)
                , text.indexOf(span)
                        + span.length()
                , 0);

        messageView.setText(spannable, TextView.BufferType.SPANNABLE);
    }

}
