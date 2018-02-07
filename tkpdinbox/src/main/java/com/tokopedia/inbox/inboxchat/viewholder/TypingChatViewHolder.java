package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.viewmodel.EmptyChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TypingChatModel;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class TypingChatViewHolder extends AbstractViewHolder<TypingChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.typing_chat_layout;

    ImageView logo;

    public TypingChatViewHolder(View itemView) {
        super(itemView);
        logo = (ImageView) itemView.findViewById(R.id.image);
    }

    @Override
    public void bind(TypingChatModel element) {
        ImageHandler.loadGif(logo, R.raw.typing, R.raw.typing);
    }
}
