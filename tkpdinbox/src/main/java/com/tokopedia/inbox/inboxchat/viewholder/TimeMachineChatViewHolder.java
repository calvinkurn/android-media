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
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineModel;

/**
 * @author by nisie on 10/24/17.
 */

public class TimeMachineChatViewHolder extends TimeMachineListViewHolder {

    private final ChatRoomContract.View viewListener;
    @LayoutRes
    public static final int LAYOUT = R.layout.time_machine_chatroom_layout;

    public TimeMachineChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final TimeMachineModel element) {
        timeMachineText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToTimeMachine(element.getUrl());
            }
        });
    }
}
