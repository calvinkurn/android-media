package com.tokopedia.inbox.inboxchat.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;

import java.util.Date;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class OppositeChatViewHolder extends AbstractViewHolder<OppositeChatViewModel> {

    private int position;
    private View view;
    private TextView message;
    private TextView hour;
    private TextView date;
    private TextView name;
    private TextView dot;
    private TextView label;
    private TextView oldMessage;
    private View oldMessageView;
    private ImageView attachment;

    ChatRoomContract.View viewListener;

    @LayoutRes
    public static final int LAYOUT = R.layout.message_item_their;
    private AttachmentChatHelper attachmentChatHelper;

    public OppositeChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        view = itemView;
        message = (TextView) itemView.findViewById(R.id.message);
        hour = (TextView) itemView.findViewById(R.id.hour);
        date = (TextView) itemView.findViewById(R.id.date);
        name = (TextView) itemView.findViewById(R.id.name);
        label = (TextView) itemView.findViewById(R.id.label);
        oldMessage = (TextView) itemView.findViewById(R.id.old_message);
        oldMessageView = itemView.findViewById(R.id.old_message_container);
        dot = itemView.findViewById(R.id.dot);
        attachment = itemView.findViewById(R.id.image);
        position = getAdapterPosition();
        attachmentChatHelper = new AttachmentChatHelper();
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final OppositeChatViewModel element) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(itemView.getContext(), view);
            }
        });

        if (!element.isHighlight()) {
            message.setText(MethodChecker.fromHtml(element.getMsg()));
        } else {
            if (element.getSpanned() != null && viewListener.getKeyword() != null) {
                message.setText(highlight(itemView.getContext(), element.getSpanned(), viewListener.getKeyword()));
            }
        }

        message.setMovementMethod(new SelectableSpannedMovementMethod());

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


        String hourTime;

        try {
            hourTime = ChatTimeConverter.formatTime(Long.parseLong(element.getReplyTime()));
        } catch (NumberFormatException e) {
            hourTime = element.getReplyTime();
        }

        hour.setText(hourTime);

        if (element.isShowHour()) {
            hour.setVisibility(View.VISIBLE);
        }else {
            hour.setVisibility(View.GONE);
        }

        name.setText(element.getSenderName());

        label.setText(element.getRole());

        name.setVisibility(View.GONE);
        dot.setVisibility(View.GONE);
        label.setVisibility(View.GONE);

        if (element.getOldMessageTitle() != null && element.getOldMessageTitle().length() > 0) {
            oldMessageView.setVisibility(View.VISIBLE);
            oldMessage.setText(getOldMessageText(element));
        } else {
            oldMessageView.setVisibility(View.GONE);
        }

        attachmentChatHelper.parse(attachment, message, element.getAttachment(), element.getRole(), element.getMsg(), viewListener);
    }

    private Spanned getOldMessageText(OppositeChatViewModel element) {
        return MethodChecker.fromHtml(
                oldMessage.getContext().getResources().getString(R.string.old_message_warn)
                        + " <b>" +
                        element.getOldMessageTitle()
                        + "</b>"

        );
    }

    private SpannableString highlight(Context context, Spanned span, String keyword) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(span);

        //Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().toLowerCase().indexOf(keyword);

        while (indexOfKeyword < span.length() && indexOfKeyword >= 0) {
            //Create a background color span on the keyword
            spannableString.setSpan(new BackgroundColorSpan(MethodChecker.getColor(context, R.color.orange_300)), indexOfKeyword, indexOfKeyword + keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(keyword, indexOfKeyword + keyword.length());
        }

        return spannableString;
    }
}
