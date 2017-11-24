package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatLeftViewModel;
import com.tokopedia.inbox.rescenter.utils.ChatTitleColorUtil;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatLeftViewHolder extends AbstractViewHolder<ChatLeftViewModel> {

    public static final int ACTION_BY_USER = 1;
    public static final int ACTION_BY_SELLER = 2;
    public static final int ACTION_BY_ADMIN = 3;
    public static final int ACTION_BY_SYSTEM = 4;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_chat_left;

    DetailResChatFragmentListener.View mainView;
    View layoutDate, layoutTitle;
    TextView tvMessage, tvDate, tvUserTitle, tvUsername;
    RecyclerView rvAttachment;
    ChatProveAdapter adapter;

    public ChatLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        rvAttachment = (RecyclerView) itemView.findViewById(R.id.rv_attachment);
        layoutTitle = itemView.findViewById(R.id.layout_title);
        tvUserTitle = (TextView) layoutTitle.findViewById(R.id.tv_user_title);
        tvUsername = (TextView) layoutTitle.findViewById(R.id.tv_username);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(ChatLeftViewModel element) {
        tvMessage.setText(MethodChecker.fromHtml(element.getConversation().getMessage()));
        if (element.getConversation().getAttachment() == null || element.getConversation().getAttachment().size() == 0) {
            rvAttachment.setVisibility(View.GONE);
        } else {
            rvAttachment.setVisibility(View.VISIBLE);
            adapter = new ChatProveAdapter(itemView.getContext(), element.getConversation().getAttachment());
            rvAttachment.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            rvAttachment.setAdapter(adapter);
        }
        String date = DateFormatUtils.formatDateForResoChatV2(element.getConversation().getCreateTime().getTimestamp());
        tvDate.setText(date);

        layoutTitle.setVisibility(element.isShowTitle() ? View.VISIBLE : View.GONE);

        if (element.getConversation().getAction().getBy() == ACTION_BY_SELLER) {
            tvUserTitle.setText("Penjual");
            tvUsername.setText(element.getShop().getName());
            ChatTitleColorUtil.sellerColorTitle(tvUserTitle, tvUsername);
        } else if (element.getConversation().getAction().getBy() == ACTION_BY_ADMIN) {
            tvUserTitle.setText("Admin");
            tvUsername.setText("Admin");
            ChatTitleColorUtil.adminColorTitle(tvUserTitle, tvUsername);
        } else if (element.getConversation().getAction().getBy() == ACTION_BY_USER) {
            tvUserTitle.setText("Pembeli");
            tvUsername.setText(element.getCustomer().getName());
            ChatTitleColorUtil.buyerColorTitle(tvUserTitle, tvUsername);
        }
    }

}
