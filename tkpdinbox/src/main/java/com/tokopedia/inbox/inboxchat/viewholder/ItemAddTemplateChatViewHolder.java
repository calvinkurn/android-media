package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.TemplateChatContract;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class ItemAddTemplateChatViewHolder extends AbstractViewHolder<TemplateChatModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.item_add_template_chat_settings;
    TemplateChatContract.View viewListener;
    View view;
    TextView textView;
    ImageView imageView;

    public ItemAddTemplateChatViewHolder(View itemView, TemplateChatContract.View viewListener) {
        super(itemView);
        view = itemView;
        imageView = itemView.findViewById(R.id.setting);
        textView = itemView.findViewById(R.id.caption);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final TemplateChatModel element) {
        if(element.size() >=5) {
            imageView.setImageDrawable(MethodChecker.getDrawable(view.getContext(), R.drawable.ic_plus_grey));
            textView.setTextColor(MethodChecker.getColor(view.getContext(),R.color.add_template_disabled));
        }else {
            imageView.setImageDrawable(MethodChecker.getDrawable(view.getContext(), R.drawable.ic_plus_green));
            textView.setTextColor(MethodChecker.getColor(view.getContext(),R.color.medium_green));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onEnter(null, getAdapterPosition());
            }
        });
    }
}
