package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProductGeneralAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemLeftViewModel;
import com.tokopedia.inbox.rescenter.utils.ChatTitleColorUtil;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatSystemLeftViewHolder extends AbstractViewHolder<ChatSystemLeftViewModel> {

    public static final int COUNT_MAX_PRODUCT = 5;

    public static final int ACTION_BY_USER = 1;
    public static final int ACTION_BY_SELLER = 2;
    public static final int ACTION_BY_ADMIN = 3;
    public static final int ACTION_BY_SYSTEM = 4;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_system_left;

    DetailResChatFragmentListener.View mainView;
    View layoutDate, layoutTitle;
    TextView tvMessage, tvDate, tvTitle, tvReasonTitle, tvReason, tvAttachment, tvUserTitle, tvUsername;
    RecyclerView rvAttachment;
    ChatProductGeneralAdapter adapter;

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
        tvReasonTitle = itemView.findViewById(R.id.tv_reason_title);
        tvReason = itemView.findViewById(R.id.tv_reason);
        tvAttachment = itemView.findViewById(R.id.tv_attachment);
        rvAttachment = itemView.findViewById(R.id.rv_attachment);
    }

    @Override
    public void bind(ChatSystemLeftViewModel element) {
        if (element.getConversation().getAction().getTitle() != null)
            tvTitle.setText(
                    String.format(
                            MainApplication.getAppContext().getResources().getString(R.string.string_common_chat_title),
                            element.getConversation().getAction().getTitle()));
        if (element.getConversation().getSolution().getName() != null)
            tvMessage.setText(MethodChecker.fromHtml(element.getConversation().getSolution().getName()));
        String date = element.getConversation().getCreateTime().getTimestamp();
        tvDate.setText(date);

        layoutTitle.setVisibility(View.VISIBLE);

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
        if (element.getConversation().getAttachment() == null || element.getConversation().getAttachment().size() == 0) {
            rvAttachment.setVisibility(View.GONE);
            tvAttachment.setVisibility(View.GONE);
        } else {
            rvAttachment.setVisibility(View.VISIBLE);
            tvAttachment.setVisibility(View.VISIBLE);
            rvAttachment.setLayoutManager(new LinearLayoutManager(
                    itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false));
            adapter = new ChatProductGeneralAdapter(mainView, MainApplication.getAppContext(), element.getConversation().getAttachment(), COUNT_MAX_PRODUCT);
            rvAttachment.setAdapter(adapter);
        }

        if (!element.getConversation().getMessage().isEmpty()) {
            tvReason.setVisibility(View.VISIBLE);
            tvReasonTitle.setVisibility(View.VISIBLE);
            tvReason.setText(element.getConversation().getMessage());
        } else {
            tvReason.setVisibility(View.GONE);
            tvReasonTitle.setVisibility(View.GONE);
        }
    }

}
