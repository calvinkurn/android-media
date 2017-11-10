package com.tokopedia.inbox.inboxchat.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.viewmodel.EmptyChatModel;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class EmptyChatListViewHolder extends AbstractViewHolder<EmptyChatModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.empty_chat_layout;

    Context context;
    ImageView logo;
    TextView title;
    TextView subtitle;

    public EmptyChatListViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        logo = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);
        subtitle = (TextView) itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bind(EmptyChatModel element) {
        if(element.getType() != EmptyChatModel.SEARCH){
            logo.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.empty_chat));
            title.setText(context.getString(R.string.no_existing_chat));
            subtitle.setText(context.getString(R.string.please_try_chat));
            title.setVisibility(View.VISIBLE);
        }else {
            logo.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.empty_chat));
            subtitle.setText(context.getString(R.string.no_existing_chat_user));
            subtitle.setVisibility(View.GONE);
        }
    }
}
