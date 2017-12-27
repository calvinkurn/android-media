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
import com.tokopedia.inbox.rescenter.detailv2.view.activity.TrackShippingActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProductAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProductGeneralAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatAwbRightViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatAwbRightViewHolder extends AbstractViewHolder<ChatAwbRightViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_awb_right;

    public static final int COUNT_MAX_PRODUCT = 5;

    DetailResChatFragmentListener.View mainView;
    View layoutDate;
    TextView tvMessage, tvDate, tvTitle, tvTitleAttachment;
    RecyclerView rvAttachment;
    ChatProductGeneralAdapter adapter;
    Button btnTrack, btnEdit;

    public ChatAwbRightViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvTitleAttachment = (TextView) itemView.findViewById(R.id.tv_title_attachment);
        rvAttachment = (RecyclerView) itemView.findViewById(R.id.rv_attachment);
        layoutDate = itemView.findViewById(R.id.layout_date);
        tvDate = (TextView) layoutDate.findViewById(R.id.tv_date);
        btnTrack = (Button) itemView.findViewById(R.id.btn_track);
        btnEdit = (Button) itemView.findViewById(R.id.btn_edit);
    }

    @Override
    public void bind(final ChatAwbRightViewModel element) {
        if (element.getConversation().getAction().getTitle() != null) {
            tvTitle.setText(
                String.format(
                        MainApplication.getAppContext().getResources().getString(R.string.string_common_chat_title),
                        element.getConversation().getAction().getTitle()));
        }
        if (element.getConversation().getShippingDetail() != null) {
            tvMessage.setText(String.format(
                    MainApplication.getAppContext().getString(R.string.string_awb_format),
                    element.getConversation().getShippingDetail().getAwbNumber(),
                    element.getConversation().getShippingDetail().getName()));
        }
        if (element.getConversation().getAttachment() == null || element.getConversation().getAttachment().size() == 0) {
            rvAttachment.setVisibility(View.GONE);
            tvTitleAttachment.setVisibility(View.GONE);
        } else {
            rvAttachment.setVisibility(View.VISIBLE);
            tvTitleAttachment.setVisibility(View.VISIBLE);
            adapter = new ChatProductGeneralAdapter(mainView, itemView.getContext(), element.getConversation().getAttachment(), COUNT_MAX_PRODUCT);
            rvAttachment.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvAttachment.setAdapter(adapter);
        }
        rvAttachment.setHasFixedSize(true);
        String date = element.getConversation().getCreateTime().getTimestamp();
        tvDate.setText(date);

        btnEdit.setVisibility(element.getConversation().getButton().getEditAwb() == 1 ? View.VISIBLE : View.GONE);
        btnTrack.setVisibility(element.getConversation().getButton().getTrackAwb() == 1 ? View.VISIBLE : View.GONE);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.doEditAwb(
                        String.valueOf(element.getConversation().getResConvId()),
                        String.valueOf(element.getConversation().getShippingDetail().getId()),
                        element.getConversation().getShippingDetail().getAwbNumber()
                );
            }
        });

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
