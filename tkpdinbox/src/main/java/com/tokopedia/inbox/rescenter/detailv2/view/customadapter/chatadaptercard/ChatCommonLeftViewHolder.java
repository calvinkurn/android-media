package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCommonLeftViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatCommonLeftViewHolder extends AbstractViewHolder<ChatCommonLeftViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_common_left;

    DetailResChatFragmentListener.View mainView;
    View layoutTitle, layoutDate;
    TextView tvContent, tvUserTitle, tvUsername, tvDate;


    public ChatCommonLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);

        layoutTitle = itemView.findViewById(R.id.layout_title);
        layoutDate = itemView.findViewById(R.id.layout_date);

        tvUserTitle = (TextView) layoutTitle.findViewById(R.id.tv_user_title);
        tvUsername = (TextView) layoutTitle.findViewById(R.id.tv_username);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(ChatCommonLeftViewModel element) {
        final Context context = itemView.getContext();
        tvContent.setText(element.getConversation().getAction().getTitle());

        tvUserTitle.setText(context.getResources().getString(R.string.string_tokopedia_system));
        tvUsername.setText(context.getResources().getString(R.string.string_tokopedia));
        String date = element.getConversation().getCreateTime().getTimestamp();
        tvDate.setText(date);
    }
}
