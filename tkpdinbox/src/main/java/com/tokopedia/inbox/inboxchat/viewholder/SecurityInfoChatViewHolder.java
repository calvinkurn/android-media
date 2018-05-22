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
 * @author by steven on 05/09/18.
 */

public class SecurityInfoChatViewHolder extends AbstractViewHolder<TimeMachineChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.security_info_chatroom_layout;

    private final ChatRoomContract.View viewListener;
    private TextView timeMachineText;


    public SecurityInfoChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        timeMachineText = (TextView) itemView.findViewById(R.id.time_machine_text);

        String securityInfo = MainApplication.getAppContext().getString(R.string.security_info_chat);
        String securityInfoLink = MainApplication.getAppContext().getString(R.string.security_info_chat_link);

        Spannable spannable = new SpannableString(String.format("%s %s", securityInfo, securityInfoLink));

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
                , securityInfo.length()
                , spannable.length()
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
