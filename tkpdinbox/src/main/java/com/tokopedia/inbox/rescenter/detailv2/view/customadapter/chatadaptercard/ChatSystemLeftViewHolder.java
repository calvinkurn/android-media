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
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemLeftViewModel;
import com.tokopedia.inbox.rescenter.utils.ChatTitleColorUtil;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatSystemLeftViewHolder extends AbstractViewHolder<ChatSystemLeftViewModel> {

    public static final int ACTION_BY_USER = 1;
    public static final int ACTION_BY_SELLER = 2;
    public static final int ACTION_BY_ADMIN = 3;
    public static final int ACTION_BY_SYSTEM = 4;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_system_left;

    DetailResChatFragmentListener.View mainView;
    View layoutDate, layoutTitle;
    TextView tvMessage, tvDate, tvUserTitle, tvUsername, tvTitle;
    ChatProveAdapter adapter;

    public ChatSystemLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        layoutTitle = itemView.findViewById(R.id.layout_title);
        tvUserTitle = (TextView) layoutTitle.findViewById(R.id.tv_user_title);
        tvUsername = (TextView) layoutTitle.findViewById(R.id.tv_username);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(ChatSystemLeftViewModel element) {
        if (element.getConversation().getAction().getTitle() != null)
            tvTitle.setText(MethodChecker.fromHtml(element.getConversation().getAction().getTitle()));
        if (element.getConversation().getSolution().getName() != null)
            tvMessage.setText(MethodChecker.fromHtml(element.getConversation().getSolution().getName()));
        String date = DateFormatUtils.formatDateForResoChatV2(element.getConversation().getCreateTime().getTimestamp());
        tvDate.setText(date);

        layoutTitle.setVisibility(element.isShowTitle() ? View.VISIBLE : View.GONE);

        if (element.getConversation().getAction().getBy() == ACTION_BY_SELLER) {
            tvUserTitle.setText(MainApplication.getAppContext().getResources().getString(R.string.string_tokopedia_seller_title));
            tvUsername.setText(element.getShop().getName());
            ChatTitleColorUtil.sellerColorTitle(tvUserTitle, tvUsername);
        } else if (element.getConversation().getAction().getBy() == ACTION_BY_ADMIN) {
            tvUserTitle.setText(MainApplication.getAppContext().getResources().getString(R.string.string_tokopedia_admin_title));
            tvUsername.setText(MainApplication.getAppContext().getResources().getString(R.string.string_tokopedia_admin_username));
            ChatTitleColorUtil.adminColorTitle(tvUserTitle, tvUsername);
        } else if (element.getConversation().getAction().getBy() == ACTION_BY_USER) {
            tvUserTitle.setText(MainApplication.getAppContext().getResources().getString(R.string.string_tokopedia_buyer_title));
            tvUsername.setText(element.getCustomer().getName());
            ChatTitleColorUtil.buyerColorTitle(tvUserTitle, tvUsername);
        }
    }

}
