package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.GeneratedMessageViewModel;

/**
 * @author by nisie on 3/29/18.
 */

public class GeneratedMessageViewHolder extends BaseChatViewHolder<GeneratedMessageViewModel> {

    private TextView message;

    @LayoutRes
    public static final int LAYOUT = R.layout.generated_message_view_holder;

    public GeneratedMessageViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }

    @Override
    public void bind(GeneratedMessageViewModel element) {
        super.bind(element);
        message.setText(MethodChecker.fromHtml(element.getMessage()));
    }
}