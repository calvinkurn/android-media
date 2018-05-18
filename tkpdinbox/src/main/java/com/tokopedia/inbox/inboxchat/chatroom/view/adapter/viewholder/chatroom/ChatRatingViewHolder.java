package com.tokopedia.inbox.inboxchat.chatroom.view.adapter.viewholder.chatroom;

import android.support.annotation.LayoutRes;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.viewholder.common.BaseChatViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.common.util.ChatLinkHandlerMovementMethod;
import com.tokopedia.inbox.inboxchat.common.util.ChatTimeConverter;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.rating.ChatRatingViewModel;

import java.util.Date;

import static com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.rating.ChatRatingViewModel.RATING_BAD;
import static com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.rating.ChatRatingViewModel.RATING_GOOD;
import static com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.rating.ChatRatingViewModel.RATING_NONE;

/**
 * @author by yfsx on 14/05/18.
 */
public class ChatRatingViewHolder extends BaseChatViewHolder<ChatRatingViewModel> {

    private TextView message;
    private LinearLayout ratingHolder;
    private ImageView ratingYes, ratingNo, ratingSelected;
    private int position;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_chat_rating;

    public ChatRatingViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        view = itemView;
        message = (TextView) itemView.findViewById(R.id.message);
        hour = (TextView) itemView.findViewById(R.id.hour);
        date = (TextView) itemView.findViewById(R.id.date);
        ratingHolder = itemView.findViewById(R.id.rating_option_holder);
        ratingYes = itemView.findViewById(R.id.rating_option_yes);
        ratingNo = itemView.findViewById(R.id.rating_option_no);
        ratingSelected = itemView.findViewById(R.id.rating_selected);
        position = getAdapterPosition();
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final ChatRatingViewModel element) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(itemView.getContext(), view);
            }
        });

        message.setMovementMethod(new ChatLinkHandlerMovementMethod(viewListener));
        message.setText(MethodChecker.fromHtml(element.getMessage()));
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
        String fullTime;
        try {
            fullTime = ChatTimeConverter.formatFullTime(Long.parseLong(element.getReplyTime()));
        } catch (NumberFormatException e) {
            fullTime = "";
        }
        switch (element.getRatingStatus()) {
            case RATING_NONE:
                ratingHolder.setVisibility(View.VISIBLE);
                ratingSelected.setVisibility(View.GONE);
                ratingYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewListener.onClickRating(element, RATING_GOOD);
                    }
                });

                ratingNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewListener.onClickRating(element, RATING_BAD);
                    }
                });
                break;

            case RATING_GOOD:
                ratingHolder.setVisibility(View.GONE);
                ratingSelected.setSelected(true);
                ratingSelected.setVisibility(View.VISIBLE);
                break;

            case RATING_BAD:
                ratingHolder.setVisibility(View.GONE);
                ratingSelected.setSelected(false);
                ratingSelected.setVisibility(View.VISIBLE);
                break;

            default:
                ratingHolder.setVisibility(View.GONE);
                ratingSelected.setVisibility(View.GONE);
        }

    }
}
