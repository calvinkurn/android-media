package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatInputAddressLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAddressDomain;
import com.tokopedia.inbox.rescenter.utils.ChatTitleColorUtil;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatInputAddressLeftViewHolder extends AbstractViewHolder<ChatInputAddressLeftViewModel> {

    public static final int ACTION_BY_USER = 1;
    public static final int ACTION_BY_SELLER = 2;
    public static final int ACTION_BY_ADMIN = 3;
    public static final int ACTION_BY_SYSTEM = 4;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_input_address_left;

    DetailResChatFragmentListener.View mainView;
    View layoutDate, layoutTitle;
    TextView tvMessage, tvDate, tvUserTitle, tvUsername, tvTitle;
    ChatProveAdapter adapter;

    public ChatInputAddressLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
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
    public void bind(ChatInputAddressLeftViewModel element) {
        tvTitle.setText(
                String.format(
                        MainApplication.getAppContext().getResources().getString(R.string.string_common_chat_title),
                        element.getConversation().getAction().getTitle()));
        ConversationAddressDomain addressDomain = element.getConversation().getAddress();
        tvMessage.setText(String.format(element.getAddressFormat(),
                addressDomain.getReceiver(),
                addressDomain.getAddress(),
                addressDomain.getDistrict(),
                addressDomain.getCity(),
                addressDomain.getPostalCode(),
                addressDomain.getProvince(),
                addressDomain.getPhone()));
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
    }

}
