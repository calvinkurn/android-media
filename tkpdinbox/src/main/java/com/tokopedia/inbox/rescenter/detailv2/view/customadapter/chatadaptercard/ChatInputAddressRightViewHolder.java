package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.AddressDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatInputAddressRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAddressDomain;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatInputAddressRightViewHolder extends AbstractViewHolder<ChatInputAddressRightViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_input_address_right;

    DetailResChatFragmentListener.View mainView;
    View layoutDate;
    TextView tvMessage, tvDate, tvTitle;
    ChatProveAdapter adapter;

    public ChatInputAddressRightViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(ChatInputAddressRightViewModel element) {
        tvTitle.setText(element.getConversation().getMessage());
        ConversationAddressDomain addressDomain = element.getConversation().getAddress();
        tvMessage.setText(String.format(element.getAddressFormat(),
                addressDomain.getReceiver(),
                addressDomain.getAddress(),
                addressDomain.getDistrict(),
                addressDomain.getCity(),
                addressDomain.getPostalCode(),
                addressDomain.getProvince(),
                addressDomain.getPostalCode()));
        String date = DateFormatUtils.formatDateForResoChatV2(element.getConversation().getCreateTime().getTimestamp());
        tvDate.setText(date);
    }

}
