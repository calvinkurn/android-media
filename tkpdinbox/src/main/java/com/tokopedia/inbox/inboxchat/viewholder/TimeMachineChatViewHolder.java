package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineChatModel;

/**
 * @author by nisie on 10/24/17.
 */

public class TimeMachineChatViewHolder extends AbstractViewHolder<TimeMachineChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.time_machine_chatroom_layout;

    private final ChatRoomContract.View viewListener;
    private TextView timeMachineText;


    public TimeMachineChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        timeMachineText = (TextView) itemView.findViewById(R.id.time_machine_text);

        Spannable spannable = new SpannableString(MainApplication.getAppContext().getString(R.string
                .time_machine_chat));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(false);
                                  ds.setColor(MethodChecker.getColor(MainApplication
                                          .getAppContext(), R.color.medium_green));
                              }
                          }
                , MainApplication.getAppContext().getString(R.string.time_machine_chat).indexOf
                        ("Riwayat")
                , MainApplication.getAppContext().getString(R.string.time_machine_chat).length()
                , 0);

        timeMachineText.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void bind(final TimeMachineChatModel element) {
        timeMachineText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToTimeMachine(element.getUrl());
            }
        });
    }
}
