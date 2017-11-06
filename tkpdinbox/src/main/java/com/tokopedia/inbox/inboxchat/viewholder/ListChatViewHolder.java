package com.tokopedia.inbox.inboxchat.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.adapter.InboxChatAdapter;
import com.tokopedia.inbox.inboxchat.adapter.NewInboxChatAdapter;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatContract;
import com.tokopedia.inbox.inboxchat.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxmessage.model.inboxmessage.InboxMessageItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.STATE_CHAT_READ;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.STATE_CHAT_UNREAD;

/**
 * Created by stevenfredian on 10/25/17.
 */

public class ListChatViewHolder extends AbstractViewHolder<ChatListViewModel>{

    TextView userName;

    TextView time;

    ImageView avatar;

    TextView message;

    View mainView;

    TextView label;

    TextView counterUnread;

    ImageView checked;

    TextView section;

    InboxChatContract.View viewListener;

    InboxChatPresenter presenter;

    @LayoutRes
    public static final int LAYOUT = R.layout.message_item;

    public ListChatViewHolder(View itemView, InboxChatContract.View viewListener, InboxChatPresenter presenter) {
        super(itemView);

        userName = (TextView) itemView.findViewById(R.id.user_name);
        time = (TextView) itemView.findViewById(R.id.time);
        avatar = (ImageView) itemView.findViewById(R.id.user_ava);
        message = (TextView) itemView.findViewById(R.id.message);
        mainView = itemView.findViewById(R.id.main);
        label = (TextView) itemView.findViewById(R.id.label);
        counterUnread = (TextView) itemView.findViewById(R.id.counter_unread);
        section = (TextView) itemView.findViewById(R.id.section);
        checked = (ImageView) itemView.findViewById(R.id.checked);
        this.viewListener = viewListener;
        this.presenter = presenter;
    }

    @Override
    public void bind(ChatListViewModel element) {

        if(element.isTyping()){
            userName.setText(element.getName());
            message.setText("sedang mengetik...");
            message.setTypeface(null, Typeface.ITALIC);
            message.setTextColor(MethodChecker.getColor(message.getContext(), R.color.medium_green));
        }else {
            if (element.getSpanMode() == ChatListViewModel.SPANNED_MESSAGE) {
                message.setText(highlight(message.getContext(), element.getSpan(), viewListener.getKeyword()));
                userName.setText(element.getName());
            } else if (element.getSpanMode() == ChatListViewModel.SPANNED_CONTACT) {
                userName.setText(highlight(message.getContext(), element.getSpan(), viewListener.getKeyword()));
                message.setText(element.getMessage());
            } else {
                message.setText(element.getMessage());
                userName.setText(element.getName());
            }
            message.setTypeface(null, Typeface.NORMAL);
            message.setTextColor(MethodChecker.getColor(message.getContext(), R.color.black_54));
        }

        if(element.getSectionSize()>0){
            section.setText(element.getSectionSize() + " ditemukan");
            section.setVisibility(View.VISIBLE);
        }else {
            section.setVisibility(View.GONE);
        }

        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getImage());

        long unixdate = Long.parseLong(element.getTime());
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixdate * 1000);
        System.out.println("Formatted Date:" + formatter.format(calendar.getTime()));

        setTime(element, getAdapterPosition());
        setLabel(element.getLabel());

        setSelectedStatus(viewListener.getActivity(), element, getAdapterPosition());
        setReadStatus(element);


//        avatar.setOnClickListener(onGoToProfile(list.get(position)));
//        userName.setOnClickListener(onGoToProfile(list.get(position)));
        mainView.setOnClickListener(onMessageClicked(element, viewListener,getAdapterPosition()));

        mainView.setOnLongClickListener(onLongClickListener(element));
    }



    private SpannableString highlight(Context context, Spanned span, String keyword) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = new SpannableString(span);

        //Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().toLowerCase().indexOf(keyword);

        while (indexOfKeyword < span.length() && indexOfKeyword >= 0) {
            //Create a background color span on the keyword
            spannableString.setSpan(new ForegroundColorSpan(MethodChecker.getColor(context,R.color.medium_green)), indexOfKeyword, indexOfKeyword + keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(keyword, indexOfKeyword + keyword.length());
        }

        return spannableString;
    }

    private void setSelectedStatus(Context context, ChatListViewModel element, int position) {
        if (element.isChecked())
            setSelectedState();
        else if (!element.isChecked())
            setUnselectedState(context, position);
    }

    private void setReadStatus(ChatListViewModel element) {
        int readStatus = element.getReadStatus();
        int counter = element.getUnreadCounter();
        if (readStatus == STATE_CHAT_UNREAD)
            setNotReadState(counter);
        else if (readStatus == STATE_CHAT_READ)
            setReadState();
    }

    private void setLabel(String labelS) {
        if(label!=null && label.length()>0){
            label.setVisibility(View.VISIBLE);
            label.setText(labelS);
        }else {
            label.setVisibility(View.GONE);
        }
    }

    private void setTime(ChatListViewModel element, int position) {
        String unixTime = ChatTimeConverter.formatTimeStamp(Long.parseLong(element.getTime()));
        time.setVisibility(View.VISIBLE);
        time.setText(unixTime);
    }

    private View.OnLongClickListener onLongClickListener(final ChatListViewModel element) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = getAdapterPosition();
                if (element.isChecked()) {
                    setReadState();
                    presenter.onDeselect(position);
                } else {
                    setSelectedState();
                    presenter.onSelected(position);
                }

                return true;
            }
        };
    }

    private View.OnClickListener onMessageClicked(final ChatListViewModel element, InboxChatContract.View viewListener, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.getSelected() == 0) {
                    element.setReadStatus(STATE_CHAT_READ);
                    element.setUnreadCounter(0);
                    presenter.goToDetailMessage(position, element);
                } else if (element.isChecked()) {
                    setReadState();
                    presenter.onDeselect(position);
                } else {
                    setSelectedState();
                    presenter.onSelected(position);
                }
            }
        };
    }

    private View.OnClickListener onGoToProfile(final InboxMessageItem messageItem) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (messageelement.getUserLabelId() != IS_ADMIN)
//                    presenter.goToProfile(messageelement.getUserId());
            }
        };
    }

    private void setSelectedState() {
        Context context = mainView.getContext();
        mainView.setBackgroundColor(context.getResources().getColor(R.color.green_selected));
        checked.setVisibility(View.VISIBLE);
    }

    private void setUnselectedState(Context context, int position) {
        mainView.setBackgroundColor(context.getResources().getColor(R.color.white));
        checked.setVisibility(View.GONE);
    }

    private void setReadState() {
        counterUnread.setVisibility(View.GONE);
        userName.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

    }

    private void setNotReadState(int counter) {
        counterUnread.setVisibility(View.VISIBLE);
        userName.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));

        if(counter > 0) {
            counterUnread.setText(String.valueOf(counter));
        }else {
            counterUnread.setVisibility(View.GONE);
        }
    }

}
