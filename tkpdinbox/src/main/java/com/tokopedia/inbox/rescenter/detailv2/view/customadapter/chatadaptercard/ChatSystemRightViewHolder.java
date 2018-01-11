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
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemRightViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatSystemRightViewHolder extends AbstractViewHolder<ChatSystemRightViewModel> {

    public static final int COUNT_MAX_PRODUCT = 5;
    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_system_right;

    DetailResChatFragmentListener.View mainView;
    View layoutDate;
    TextView tvMessage, tvDate, tvTitle, tvReasonTitle, tvReason, tvAttachment;
    RecyclerView rvAttachment;
    ChatProductGeneralAdapter adapter;

    public ChatSystemRightViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
        tvReasonTitle = itemView.findViewById(R.id.tv_reason_title);
        tvReason = itemView.findViewById(R.id.tv_reason);
        tvAttachment = itemView.findViewById(R.id.tv_attachment);
        rvAttachment = itemView.findViewById(R.id.rv_attachment);
    }

    @Override
    public void bind(ChatSystemRightViewModel element) {
        if (element.getConversation().getAction().getTitle() != null)
            tvTitle.setText(
                    String.format(
                            MainApplication.getAppContext().getResources().getString(R.string.string_common_chat_title),
                            element.getConversation().getAction().getTitle()));
        if (element.getConversation().getSolution().getName() != null)
            tvMessage.setText(MethodChecker.fromHtml(element.getConversation().getSolution().getName()));
        String date = element.getConversation().getCreateTime().getTimestamp();
        tvDate.setText(date);
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
