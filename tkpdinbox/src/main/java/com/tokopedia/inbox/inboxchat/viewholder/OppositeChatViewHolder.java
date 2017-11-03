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

import com.tokopedia.core.R2;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
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
    TextView message;
    TextView hour;
    TextView date;
    TextView name;
    TextView label;
    ChatRoomContract.View viewListener;

    @LayoutRes
    public static final int LAYOUT = R.layout.message_item_their;

    public OppositeChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        message = (TextView) itemView.findViewById(R.id.message);
        hour = (TextView) itemView.findViewById(R.id.hour);
        date = (TextView) itemView.findViewById(R.id.date);
        name = (TextView) itemView.findViewById(R.id.name);
        label = (TextView) itemView.findViewById(R.id.label);
        position = getAdapterPosition();
        this.viewListener = viewListener;
    }

    @Override
    public void bind(OppositeChatViewModel element) {
        //        ImageHandler.loadImageCircle2(context, avatar, list.get(position).getUserImage());
        if(!element.isHighlight()) {
            message.setText(element.getMsg());
        }else {
            if(element.getSpanned()!= null && viewListener.getKeyword()!=null) {
                message.setText(highlight(itemView.getContext(), element.getSpanned(), viewListener.getKeyword()));
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

        hour.setText(hourTime);

        element.getSenderId();

        name.setText(element.getSenderName());

        label.setText(element.getRole());
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
