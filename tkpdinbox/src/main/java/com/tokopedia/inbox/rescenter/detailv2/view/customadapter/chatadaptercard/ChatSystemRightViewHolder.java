package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemRightViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatSystemRightViewHolder extends AbstractViewHolder<ChatSystemRightViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_system_right;

    DetailResChatFragmentListener.View mainView;
    View layoutDate;
    TextView tvMessage, tvDate, tvtitle;
    ChatProveAdapter adapter;

    public ChatSystemRightViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvtitle = (TextView) itemView.findViewById(R.id.tv_title);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(ChatSystemRightViewModel element) {
        if (!element.getConversation().getMessage().isEmpty()) {
            tvtitle.setText(MethodChecker.fromHtml(element.getConversation().getMessage()));
        }
        tvMessage.setText(MethodChecker.fromHtml(element.getConversation().getSolution().getName()));
        String date = DateFormatUtils.formatDateForResoChatV2(element.getConversation().getCreateTime().getTimestamp());
        tvDate.setText(date);
    }

}
