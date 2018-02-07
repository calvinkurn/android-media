package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.TemplateChatContract;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class ItemTemplateChatViewHolder extends AbstractViewHolder<TemplateChatModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.item_template_chat_settings;
    TemplateChatContract.View viewListener;
    TextView textHolder;
    private View setting;
    private View edit;

    public ItemTemplateChatViewHolder(View itemView, TemplateChatContract.View viewListener) {
        super(itemView);
        textHolder = itemView.findViewById(R.id.caption);
        setting = itemView.findViewById(R.id.setting);
        edit = itemView.findViewById(R.id.edit);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final TemplateChatModel element) {
        textHolder.setText(element.getMessage());
        final ItemTemplateChatViewHolder ini = this;
        setting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    viewListener.onDrag(ini);
                }
                return false;
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onEnter(element.getMessage(), getAdapterPosition());
            }
        });
    }
}
