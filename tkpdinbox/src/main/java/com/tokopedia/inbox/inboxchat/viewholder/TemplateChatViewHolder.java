package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class TemplateChatViewHolder extends AbstractViewHolder<TemplateChatModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.item_template_chat_layout;

    ChatRoomContract.View viewListener;
    TextView textHolder;
    ScrollView scrollView;

    public TemplateChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        textHolder = itemView.findViewById(R.id.text);
//        scrollView = itemView.findViewById(R.id.children_layout);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final TemplateChatModel element) {
        textHolder.setText(element.getMessage());

        textHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.addTemplateString(element.getMessage());
            }
        });

    }
}
