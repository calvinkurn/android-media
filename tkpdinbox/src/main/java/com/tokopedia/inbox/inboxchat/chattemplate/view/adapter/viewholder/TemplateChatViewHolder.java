package com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.chattemplate;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.chatroom.listener.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.templatechat.TemplateChatModel;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class TemplateChatViewHolder extends AbstractViewHolder<TemplateChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_template_chat_layout;

    ChatRoomContract.View viewListener;
    TextView textHolder;
    ImageView icon;

    public TemplateChatViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView);
        textHolder = itemView.findViewById(R.id.text);
        icon = itemView.findViewById(R.id.setting);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final TemplateChatModel element) {
        if(element.isIcon()){
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.goToSettingTemplate();
                }
            });
            textHolder.setVisibility(View.GONE);
            icon.setVisibility(View.VISIBLE);
        }else {
            textHolder.setText(element.getMessage());

            textHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.addTemplateString(element.getMessage());
                }
            });

            textHolder.setVisibility(View.VISIBLE);
            icon.setVisibility(View.GONE);
        }
    }
}
