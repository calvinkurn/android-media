package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProductGeneralAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatAwbLeftViewModel;
import com.tokopedia.inbox.rescenter.utils.ChatTitleColorUtil;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatAwbLeftViewHolder extends AbstractViewHolder<ChatAwbLeftViewModel> {

    public static final int COUNT_MAX_PRODUCT = 5;
    public static final int ACTION_BY_USER = 1;
    public static final int ACTION_BY_SELLER = 2;
    public static final int ACTION_BY_ADMIN = 3;
    public static final int ACTION_BY_SYSTEM = 4;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_awb_left;

    DetailResChatFragmentListener.View mainView;
    View layoutDate, layoutTitle;
    TextView tvMessage, tvDate, tvUserTitle, tvUsername, tvTitle, tvTitleAttachment;
    RecyclerView rvAttachment;
    Button btnTrack;
    ChatProductGeneralAdapter adapter;

    public ChatAwbLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvTitleAttachment = (TextView) itemView.findViewById(R.id.tv_title_attachment);
        rvAttachment = (RecyclerView) itemView.findViewById(R.id.rv_attachment);
        layoutTitle = itemView.findViewById(R.id.layout_title);
        tvUserTitle = (TextView) layoutTitle.findViewById(R.id.tv_user_title);
        tvUsername = (TextView) layoutTitle.findViewById(R.id.tv_username);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
        btnTrack = (Button) itemView.findViewById(R.id.btn_track);
    }

    @Override
    public void bind(final ChatAwbLeftViewModel element) {
        tvTitle.setText(String.format(
                        MainApplication.getAppContext().getResources().getString(R.string.string_common_chat_title),
                        element.getConversation().getAction().getTitle()));
        if (element.getConversation().getAttachment() == null || element.getConversation().getAttachment().size() == 0) {
            rvAttachment.setVisibility(View.GONE);
            tvTitleAttachment.setVisibility(View.GONE);
        } else {
            tvTitleAttachment.setVisibility(View.VISIBLE);
            rvAttachment.setVisibility(View.VISIBLE);
            adapter = new ChatProductGeneralAdapter(mainView, itemView.getContext(), element.getConversation().getAttachment(), COUNT_MAX_PRODUCT);
            rvAttachment.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvAttachment.setAdapter(adapter);
        }
        rvAttachment.setHasFixedSize(true);
        String date = element.getConversation().getCreateTime().getTimestamp();
        tvDate.setText(date);
        if (element.getConversation().getShippingDetail() != null) {
            tvMessage.setText(String.format(
                    MainApplication.getAppContext().getString(R.string.string_awb_format),
                    element.getConversation().getShippingDetail().getAwbNumber(),
                    element.getConversation().getShippingDetail().getName()));
        }
        layoutTitle.setVisibility(View.VISIBLE);
        btnTrack.setVisibility(element.getConversation().getButton().getTrackAwb() == 1 ? View.VISIBLE : View.GONE);

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

        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.doTrackShipping(
                        String.valueOf(element.getConversation().getShippingDetail().getId()),
                        element.getConversation().getShippingDetail().getAwbNumber());
            }
        });
    }

}
