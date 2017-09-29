package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;

import java.util.Date;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class MyChatViewHolder extends AbstractViewHolder<MyChatViewModel>{

    TextView message;
    TextView hour;
    TextView date;


    @LayoutRes
    public static final int LAYOUT = R.layout.message_item_mine;

    public MyChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);

        message = (TextView) itemView.findViewById(R.id.message);
        hour = (TextView) itemView.findViewById(R.id.hour);
        date = (TextView) itemView.findViewById(R.id.date);
    }

    @Override
    public void bind(MyChatViewModel element) {
        message.setText(element.getMsg());
        message.setMovementMethod(new SelectableSpannedMovementMethod());

        date.setVisibility(View.VISIBLE);
        String time;

        try {
            long myTime = Long.parseLong(element.getReplyTime());
            time = DateFormat.getLongDateFormat(itemView.getContext()).format(new Date(myTime));
        }catch (NumberFormatException e){
            time = element.getReplyTime();
        }
        date.setText(time);

        if (element.isShowTime()) {
            date.setVisibility(View.VISIBLE);
        }else {
            date.setVisibility(View.GONE);
        }

        String hourTime;

        try {
            hourTime = ChatTimeConverter.formatTime(Long.parseLong(element.getReplyTime()));
        }catch (NumberFormatException e){
            hourTime = element.getReplyTime();
        }

        hour.setText(hourTime);

        element.getSenderId();

    }
}
