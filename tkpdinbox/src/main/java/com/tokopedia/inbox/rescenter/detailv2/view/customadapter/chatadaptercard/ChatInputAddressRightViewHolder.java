package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatInputAddressRightViewModel;
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
    Button btnChangeAddress;
    FrameLayout ffChangeAddress;

    public ChatInputAddressRightViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
        btnChangeAddress = (Button) itemView.findViewById(R.id.btn_change_address);
        ffChangeAddress = (FrameLayout) itemView.findViewById(R.id.ff_change_address);
    }

    @Override
    public void bind(final ChatInputAddressRightViewModel element) {
        tvTitle.setText(
                String.format(
                        MainApplication.getAppContext().getResources().getString(R.string.string_common_chat_title),
                        element.getConversation().getAction().getTitle()));
        final ConversationAddressDomain addressDomain = element.getConversation().getAddress();
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
        ffChangeAddress.setVisibility(element.getConversation().getButton().getEditAddress() == 1 ?
                View.VISIBLE : View.GONE);

        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.intentToEditAddress(element.getConversation().getResConvId(), addressDomain.getAddressId());
            }
        });
    }

}
