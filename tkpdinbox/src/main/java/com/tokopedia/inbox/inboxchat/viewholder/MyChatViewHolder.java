package com.tokopedia.inbox.inboxchat.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
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

    View view;
    TextView message;
    TextView hour;
    TextView date;
    ImageView chatStatus;
    ChatRoomContract.View viewListener;

    @LayoutRes
    public static final int LAYOUT = R.layout.message_item_mine;

    public MyChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        view = itemView;
        message = (TextView) itemView.findViewById(R.id.message);
        hour = (TextView) itemView.findViewById(R.id.hour);
        date = (TextView) itemView.findViewById(R.id.date);
        chatStatus = (ImageView) itemView.findViewById(R.id.chat_status);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(MyChatViewModel element) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(itemView.getContext(), view);
            }
        });

        if(!element.isHighlight()) {
            message.setText(MethodChecker.fromHtml(element.getMsg()));
        }else {
            if(element.getSpanned()!= null && viewListener.getKeyword()!=null) {
                message.setText(highlight(itemView.getContext(), element.getSpanned(), viewListener.getKeyword()));
            }else {
                message.setText(MethodChecker.fromHtml(element.getMsg()));
            }
        }
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

        if (element.isShowHour()) {
            hour.setVisibility(View.VISIBLE);
            chatStatus.setVisibility(View.VISIBLE);
        }else {
            hour.setVisibility(View.GONE);
            chatStatus.setVisibility(View.GONE);
        }

        hour.setText(hourTime);

        int imageResource;

        if(element.isReadStatus()){
            imageResource = R.drawable.ic_chat_read;
        }else {
            imageResource = R.drawable.ic_chat_unread;
        }
        if(element.isDummy()){
            imageResource = R.drawable.ic_chat_pending;
        }

        chatStatus.setImageResource(imageResource);

        element.getSenderId();

    }

    private SpannableString highlight(Context context, Spanned span, String keyword) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(span);

        //Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().toLowerCase().indexOf(keyword);

        while (indexOfKeyword < span.length() && indexOfKeyword >= 0) {
            //Create a background color span on the keyword
            spannableString.setSpan(new BackgroundColorSpan(MethodChecker.getColor(context,R.color.orange_300)), indexOfKeyword, indexOfKeyword + keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(keyword, indexOfKeyword + keyword.length());
        }

        return spannableString;
    }
}
