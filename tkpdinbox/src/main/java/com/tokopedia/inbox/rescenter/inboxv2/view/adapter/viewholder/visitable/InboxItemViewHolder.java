package com.tokopedia.inbox.rescenter.inboxv2.view.adapter.viewholder.visitable;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.ProductAdapter;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;

/**
 * Created by yfsx on 01/02/18.
 */

public class InboxItemViewHolder extends AbstractViewHolder<InboxItemViewModel> {
    private Context context;
    private ResoInboxFragmentListener.View mainView;
    private TextView tvTitle, tvInvoice, tvUsernameTitle, tvUsername,
            tvAutoExecute, tvLastReply, tvFreeReturn, tvMoreImage,
            tvSeeConversation;
    private ImageView ivNotification;
    private RecyclerView rvProduct;
    private FrameLayout ffTitleNotif, ffProduct;
    private LinearLayout llItem;

    @LayoutRes
    public static final int LAYOUT = R.layout.item_reso_inbox;

    public InboxItemViewHolder(View itemView, ResoInboxFragmentListener.View mainView) {
        super(itemView);
        this.context = itemView.getContext();
        this.mainView = mainView;
        ffTitleNotif = (FrameLayout) itemView.findViewById(R.id.ff_title_notif);
        ivNotification = (ImageView) itemView.findViewById(R.id.ic_notification);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvInvoice = (TextView) itemView.findViewById(R.id.tv_invoice);
        tvUsernameTitle = (TextView) itemView.findViewById(R.id.tv_username_title);
        tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
        tvAutoExecute = (TextView) itemView.findViewById(R.id.tv_auto_execute);
        tvLastReply = (TextView) itemView.findViewById(R.id.tv_last_reply);
        tvFreeReturn = (TextView) itemView.findViewById(R.id.tv_free_return);
        tvMoreImage = (TextView) itemView.findViewById(R.id.tv_more_image);
        tvSeeConversation = (TextView) itemView.findViewById(R.id.tv_see_conversation);
        rvProduct = (RecyclerView) itemView.findViewById(R.id.rv_product);
        ffProduct = (FrameLayout) itemView.findViewById(R.id.ff_product);
        llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
    }

    @Override
    public void bind(InboxItemViewModel element) {
        bindView(element);
        bindViewListener(element);
    }
    
    private void bindView(InboxItemViewModel item) {
        if (TextUtils.isEmpty(item.getInboxMessage())) {
            ffTitleNotif.setVisibility(View.GONE);
        } else {
            ffTitleNotif.setVisibility(View.VISIBLE);
            tvTitle.setText(item.getInboxMessage());
            tvTitle.setTextColor(Color.parseColor(item.getInboxMessageTextColor()));
            ffTitleNotif.setBackgroundColor(Color.parseColor(item.getInboxMessageBackgroundColor()));
        }

        tvInvoice.setText(item.getInvoiceNumber());
        ivNotification.setVisibility(item.isNotificationShow() ? View.VISIBLE : View.GONE);

        tvUsernameTitle.setText(item.getNameTitle());
        tvUsername.setText(item.getUserName());
        tvAutoExecute.setText(buildAutoDoneText(item.getAutoDoneText()));
        tvLastReply.setText(item.getLastReplyText());
        tvFreeReturn.setText(item.getFreeReturnText());

        if (!TextUtils.isEmpty(item.getAutoDoneBackgroundColor())) {
            tvAutoExecute.setTextColor(Color.parseColor(item.getAutoDoneTextColor()));
            tvAutoExecute.setBackground(MethodChecker.getDrawable(context, R.drawable.bg_title_inbox));
            tvAutoExecute.setBackgroundColor(Color.parseColor(item.getAutoDoneBackgroundColor()));
        } else {
            tvAutoExecute.setTextColor(MethodChecker.getColor(context, R.color.black_70));
            tvAutoExecute.setBackground(MethodChecker.getDrawable(context, R.drawable.bg_title_inbox));
            tvAutoExecute.setBackgroundColor(MethodChecker.getColor(context, R.color.transparent));
        }

        if (item.getImageList() != null) {
            ffProduct.setVisibility(View.VISIBLE);
            tvMoreImage.setText(item.getExtraImageCountText());
            rvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ProductAdapter adapter = new ProductAdapter(context, item.getImageList());
            rvProduct.setAdapter(adapter);
        } else {
            ffProduct.setVisibility(View.GONE);
        }
    }
    
    private void bindViewListener(final InboxItemViewModel item) {
        llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.onItemClicked(item.getResId(), item.getSellerName(), item.getCustomerName());
            }
        });
    }

    private String buildAutoDoneText(String text) {
        return " " + text + " ";
    }
    
}
