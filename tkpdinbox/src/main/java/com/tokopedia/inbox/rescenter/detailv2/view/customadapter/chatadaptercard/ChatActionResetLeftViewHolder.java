package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatActionResetLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemLeftViewModel;
import com.tokopedia.inbox.rescenter.utils.ChatTitleColorUtil;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatActionResetLeftViewHolder extends AbstractViewHolder<ChatActionResetLeftViewModel> {

    public static final int ACTION_BY_USER = 1;
    public static final int ACTION_BY_SELLER = 2;
    public static final int ACTION_BY_ADMIN = 3;
    public static final int ACTION_BY_SYSTEM = 4;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_action_reset_left;

    DetailResChatFragmentListener.View mainView;
    View layoutDate, layoutTitle;
    TextView tvSolution, tvDate, tvUserTitle, tvUsername, tvTitle, tvReason;
    ChatProveAdapter adapter;

    public ChatActionResetLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvSolution = (TextView) itemView.findViewById(R.id.tv_solution);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        layoutTitle = itemView.findViewById(R.id.layout_title);
        tvUserTitle = (TextView) layoutTitle.findViewById(R.id.tv_user_title);
        tvUsername = (TextView) layoutTitle.findViewById(R.id.tv_username);
        tvReason = (TextView) itemView.findViewById(R.id.tv_reason);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(ChatActionResetLeftViewModel element) {
        if (element.getConversation().getAction().getTitle() != null)
            tvTitle.setText(
                    String.format(
                            MainApplication.getAppContext().getResources().getString(R.string.string_common_chat_title),
                            element.getConversation().getAction().getTitle()));
        if (element.getConversation().getSolution().getName() != null)
            tvSolution.setText(MethodChecker.fromHtml(element.getConversation().getSolution().getName()));
        tvReason.setText(MethodChecker.fromHtml(element.getConversation().getMessage()));
        String date = element.getConversation().getCreateTime().getTimestamp();
        tvDate.setText(date);

        tvUserTitle.setText(MainApplication.getAppContext().getResources().getString(R.string.string_tokopedia_admin_title));
        tvUsername.setText(MainApplication.getAppContext().getResources().getString(R.string.string_tokopedia_admin_username));
        ChatTitleColorUtil.adminColorTitle(tvUserTitle, tvUsername);
    }

}
