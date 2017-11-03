package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.viewmodel.EmptyChatModel;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class EmptyChatListViewHolder extends AbstractViewHolder<EmptyChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.empty_chat_layout;

    ImageView logo;
    TextView title;
    TextView subtitle;

    public EmptyChatListViewHolder(View itemView) {
        super(itemView);
        logo = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(EmptyChatModel element) {

    }
}
