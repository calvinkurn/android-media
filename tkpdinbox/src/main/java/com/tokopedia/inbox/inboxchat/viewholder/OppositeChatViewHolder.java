package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.R2;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class OppositeChatViewHolder extends AbstractViewHolder<OppositeChatViewModel> {

    private int position;
    ImageView avatar;
    TextView message;
    TextView hour;
    TextView date;
    TextView name;
    TextView label;

    @LayoutRes
    public static final int LAYOUT = R.layout.message_item_their;

    public OppositeChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.user_ava);
        message = (TextView) itemView.findViewById(R.id.message);
        hour = (TextView) itemView.findViewById(R.id.hour);
        date = (TextView) itemView.findViewById(R.id.date);
        name = (TextView) itemView.findViewById(R.id.name);
        label = (TextView) itemView.findViewById(R.id.label);
        position = getAdapterPosition();

    }

    @Override
    public void bind(OppositeChatViewModel element) {
        //        ImageHandler.loadImageCircle2(context, avatar, list.get(position).getUserImage());
        message.setText(element.getMsg());
        message.setMovementMethod(new SelectableSpannedMovementMethod());

        date.setVisibility(View.VISIBLE);
        long myTime = Long.parseLong(element.getReplyTime());

        String time = DateFormat.getLongDateFormat(itemView.getContext()).format(new Date(myTime));
        date.setText(time);

        if (element.isShowTime()) {
            date.setVisibility(View.VISIBLE);
        }else {
            date.setVisibility(View.GONE);
        }

        String hourTime = ChatTimeConverter.formatTime(Long.parseLong(element.getReplyTime()));
        hour.setText(hourTime);

        element.getSenderId();

        name.setText("TestName");

        label.setText("TestLabel");

    }
}
