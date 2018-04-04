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

/**
 * @author by nisie on 2/27/18.
 */

public class GroupChatPointsViewHolder extends BaseChatViewHolder<GroupChatPointsViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_groupchat_points;

    TextView messageView;
    Context context;
    View mainView;
    ImageView icon;
    private final ChatroomContract.View.GroupChatPointsViewHolderListener listener;

    public GroupChatPointsViewHolder(View itemView, ChatroomContract.View.GroupChatPointsViewHolderListener imageListener) {
        super(itemView);
        mainView = itemView;
        messageView = itemView.findViewById(R.id.text);
        icon = itemView.findViewById(R.id.icon);
        listener = imageListener;
    }

    @Override
    public void bind(final GroupChatPointsViewModel element) {
        super.bind(element);

        setIcon(element.getType());

        context = itemView.getContext();
        String text = element.getText();
        String span = element.getSpan();
        if (TextUtils.isEmpty(span)) {
            messageView.setText(text);
        } else {
            Spannable spannable = new SpannableString(element.getSpan());
            spannable.setSpan(new ClickableSpan() {
                                  @Override
                                  public void onClick(View view) {
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

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPointsClicked(element.getUrl());
                }
            });
        }
    }

    private void setIcon(String type) {
        switch (type) {
            case GroupChatPointsViewModel.TYPE_POINTS:
                loadIcon(R.drawable.ic_got_points);
                break;
            case GroupChatPointsViewModel.TYPE_COUPON:
                loadIcon(R.drawable.ic_got_coupon);
                break;
            case GroupChatPointsViewModel.TYPE_LOYALTY:
                loadIcon(R.drawable.ic_got_coupon);
                break;
        }
    }

    private void loadIcon(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(icon, resId);
        } else {
            icon.setImageResource(resId);
        }
    }

}
