package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCommonLeftViewModel;
import com.tokopedia.inbox.rescenter.utils.ChatTitleColorUtil;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatActionFinalLeftViewHolder extends AbstractViewHolder<ChatCommonLeftViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_action_final_left;

    DetailResChatFragmentListener.View mainView;
    View layoutTitle1, layoutDate1, layoutTitle2, layoutDate2;
    TextView tvContent, tvReason, tvDate1, tvDate2, tvUserTitle1, tvUserName1, tvUserTitle2, tvUserName2, tvSystemMessage;


    public ChatActionFinalLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvContent = (TextView) itemView.findViewById(R.id.tv_content_final_solution);
        tvReason = (TextView) itemView.findViewById(R.id.tv_content_reason);
        tvSystemMessage = (TextView) itemView.findViewById(R.id.tv_system_message);

        layoutTitle1 = itemView.findViewById(R.id.layout_title1);
        tvUserTitle1 = (TextView) layoutTitle1.findViewById(R.id.tv_user_title);
        tvUserName1 = (TextView) layoutTitle1.findViewById(R.id.tv_username);

        layoutTitle2 = itemView.findViewById(R.id.layout_title2);
        tvUserTitle2 = (TextView) layoutTitle2.findViewById(R.id.tv_user_title);
        tvUserName2 = (TextView) layoutTitle2.findViewById(R.id.tv_username);

        layoutDate1 = itemView.findViewById(R.id.layout_date_1);
        tvUserName1 = (TextView) layoutDate1.findViewById(R.id.tv_username);

        layoutDate2 = itemView.findViewById(R.id.layout_date_2);
        tvUserName2 = (TextView) layoutDate2.findViewById(R.id.tv_username);

    }

    @Override
    public void bind(ChatCommonLeftViewModel element) {final Context context = itemView.getContext();
        String date = DateFormatUtils.formatDateForResoChatV2(
                element.getConversation().getCreateTime().getTimestamp());

        tvUserTitle1.setText(context.getResources().getString(R.string.string_tokopedia_admin_title));
        tvUserName1.setText(context.getResources().getString(R.string.string_tokopedia_admin_username));
        tvDate1.setText(date);

        tvUserTitle2.setText(context.getResources().getString(R.string.string_tokopedia_system));
        tvUserName2.setText(context.getResources().getString(R.string.string_tokopedia));
        tvDate2.setText(date);

        ChatTitleColorUtil.adminColorTitle(tvUserTitle1, tvUserName1);
        ChatTitleColorUtil.systemColorTitle(tvUserTitle2, tvUserName2);

        tvContent.setText(element.getConversation().getSolution().getName());
        tvReason.setText(element.getConversation().getMessage());
        tvSystemMessage.setText(element.getConversation().getAction().getTitle());
    }
}
