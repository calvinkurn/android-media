package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewholder.movement.ChatLinkHandlerMovementMethod;

import java.util.Date;

/**
 * @author by nisie on 5/9/18.
 */
public class BaseChatViewHolder<T extends Visitable> extends AbstractViewHolder<T> {

    protected View view;
    protected TextView message;
    protected TextView hour;
    protected TextView date;

    ChatRoomContract.View viewListener;

    BaseChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        view = itemView;
        this.viewListener = viewListener;
        message = (TextView) itemView.findViewById(R.id.message);
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


            setMessage(element);

            setClickableUrl();
            setHeaderDate(element);
            setBottomHour(element);
        }

    }

    protected void setMessage(BaseChatViewModel element) {
        message.setText(MethodChecker.fromHtml(element.getMessage()));
    }

    protected void setClickableUrl() {
        message.setMovementMethod(new ChatLinkHandlerMovementMethod(viewListener));
    }

    protected void setBottomHour(BaseChatViewModel element) {
        String hourTime;

        try {
            hourTime = ChatTimeConverter.formatTime(Long.parseLong(element.getReplyTime()));
        } catch (NumberFormatException e) {
            hourTime = element.getReplyTime();
        }

        hour.setText(hourTime);
    }

    protected void setHeaderDate(BaseChatViewModel element) {
        date.setVisibility(View.VISIBLE);
        String time;

        try {
            long myTime = Long.parseLong(element.getReplyTime());
            time = DateFormat.getLongDateFormat(itemView.getContext()).format(new Date(myTime));
        } catch (NumberFormatException e) {
            time = element.getReplyTime();
        }
        date.setText(time);

        if (element.isShowTime()) {
            date.setVisibility(View.VISIBLE);
        } else {
            date.setVisibility(View.GONE);
        }
    }
}
