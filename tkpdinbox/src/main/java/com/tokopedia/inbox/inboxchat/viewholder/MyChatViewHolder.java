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
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
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
    private TextView name;
    private TextView label;
    private TextView dot;
    private ImageView attachment;
    ChatRoomContract.View viewListener;
    private static final String ROLE_USER = "User";

    @LayoutRes
    public static final int LAYOUT = R.layout.message_item_mine;
    private AttachmentChatHelper attachmentChatHelper;

    public MyChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        view = itemView;
        message = itemView.findViewById(R.id.message);
        hour = itemView.findViewById(R.id.hour);
        date = itemView.findViewById(R.id.date);
        chatStatus = itemView.findViewById(R.id.chat_status);
        name = itemView.findViewById(R.id.name);
        label = itemView.findViewById(R.id.label);
        dot = itemView.findViewById(R.id.dot);
        attachment = itemView.findViewById(R.id.image);
        attachmentChatHelper = new AttachmentChatHelper();
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final MyChatViewModel element) {

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

        if(element.getRole()!=null){
            if(element.getRole().toLowerCase().equals(ROLE_USER.toLowerCase())){
                name.setVisibility(View.GONE);
                label.setVisibility(View.GONE);
                dot.setVisibility(View.GONE);
            }else{
                name.setText(element.getSenderName());
                label.setText(element.getRole());
                name.setVisibility(View.VISIBLE);
                dot.setVisibility(View.VISIBLE);
                label.setVisibility(View.VISIBLE);
            }
        }else {
            name.setVisibility(View.GONE);
            label.setVisibility(View.GONE);
            dot.setVisibility(View.GONE);
        }

        attachmentChatHelper.parse(attachment, message, element.getAttachment(), element.getRole(), element.getMsg(), viewListener);

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
