package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;

import java.util.Date;

/**
 * @author by nisie on 5/9/18.
 */
public class BaseChatViewHolder<T extends Visitable> extends AbstractViewHolder<T> {

    protected View view;
    protected TextView hour;
    protected TextView date;

    ChatRoomContract.View viewListener;

    BaseChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        view = itemView;
        this.viewListener = viewListener;
        hour = (TextView) itemView.findViewById(R.id.hour);
        date = (TextView) itemView.findViewById(R.id.date);
    }

    @Override
    public void bind(T viewModel) {
        if (viewModel instanceof BaseChatViewModel) {

            BaseChatViewModel element = (BaseChatViewModel) viewModel;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardHandler.DropKeyboard(itemView.getContext(), view);
                }
            });

            setHeaderDate(element);
            setBottomHour(element);
        }

    }

    protected void setBottomHour(BaseChatViewModel element) {
        String hourTime;

        try {
            hourTime = ChatTimeConverter.formatTime(Long.parseLong(element.getReplyTime()));
        } catch (NumberFormatException e) {
            hourTime = element.getReplyTime();
        }

        if (hour != null && TextUtils.isEmpty(hourTime)) {
            hour.setVisibility(View.GONE);
        } else if (hour != null) {
            hour.setText(hourTime);
            hour.setVisibility(View.VISIBLE);
        }

    }

    protected void setHeaderDate(BaseChatViewModel element) {
        String time;

        try {
            long myTime = Long.parseLong(element.getReplyTime());
            time = DateFormat.getLongDateFormat(itemView.getContext()).format(new Date(myTime));
        } catch (NumberFormatException e) {
            time = element.getReplyTime();
        }

        if (date != null
                && element.isShowTime()
                && !TextUtils.isEmpty(time)) {
            date.setVisibility(View.VISIBLE);
            date.setText(time);
        } else if (date != null) {
            date.setVisibility(View.GONE);
        }
    }

    public void onViewRecycled() {

    }
}
