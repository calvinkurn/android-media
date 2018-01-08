package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatActionEarlyLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCommonLeftViewModel;
import com.tokopedia.inbox.rescenter.utils.ChatTitleColorUtil;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatActionEarlyLeftViewHolder extends AbstractViewHolder<ChatActionEarlyLeftViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_action_early_left;

    DetailResChatFragmentListener.View mainView;
    View layoutTitle1, layoutDate1;
    TextView tvContent, tvReason, tvDate1, tvUserTitle1, tvUserName1;


    public ChatActionEarlyLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvContent = (TextView) itemView.findViewById(R.id.tv_content_final_solution);
        tvReason = (TextView) itemView.findViewById(R.id.tv_content_reason);

        layoutTitle1 = itemView.findViewById(R.id.layout_title1);
        tvUserTitle1 = (TextView) layoutTitle1.findViewById(R.id.tv_user_title);
        tvUserName1 = (TextView) layoutTitle1.findViewById(R.id.tv_username);

        layoutDate1 = itemView.findViewById(R.id.layout_date_1);
        tvDate1 = (TextView) layoutDate1.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(ChatActionEarlyLeftViewModel element) {
        final Context context = itemView.getContext();
        String date = element.getConversation().getCreateTime().getTimestamp();

        tvUserTitle1.setText(context.getResources().getString(R.string.string_tokopedia_admin_title));
        tvUserName1.setText(context.getResources().getString(R.string.string_tokopedia_admin_username));
        tvDate1.setText(date);

        ChatTitleColorUtil.adminColorTitle(tvUserTitle1, tvUserName1);

        tvContent.setText(element.getConversation().getSolution().getName());
        tvReason.setText(element.getConversation().getMessage());
    }
}
