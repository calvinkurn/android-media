package com.tokopedia.inbox.rescenter.inboxv2.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;

import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxAdapter extends RecyclerView.Adapter<ResoInboxAdapter.Holder> {
    private ResoInboxFragmentListener.View mainView;
    private List<InboxItemViewModel> itemList;
    private Context context;

    public ResoInboxAdapter(Context context, ResoInboxFragmentListener.View mainView, List<InboxItemViewModel> itemList) {
        this.itemList = itemList;
        this.context = context;
        this.mainView = mainView;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_reso_inbox, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        InboxItemViewModel item = itemList.get(pos);
        bindView(holder, item);
        bindViewListener(holder, item);
    }

    private void bindView(Holder holder, InboxItemViewModel item) {
        if (TextUtils.isEmpty(item.getInboxMessage())) {
            holder.ffTitleNotif.setVisibility(View.GONE);
        } else {
            holder.ffTitleNotif.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(item.getInboxMessage());
            holder.tvTitle.setTextColor(Color.parseColor(item.getInboxMessageTextColor()));
            holder.ffTitleNotif.setBackgroundColor(Color.parseColor(item.getInboxMessageBackgroundColor()));
        }

        holder.tvInvoice.setText(item.getInvoiceNumber());
        holder.ivNotification.setVisibility(item.isNotificationShow() ? View.VISIBLE : View.GONE);

        holder.tvUsernameTitle.setText(item.getNameTitle());
        holder.tvUsername.setText(item.getUserName());
        holder.tvAutoExecute.setText(item.getAutoDoneText());
        holder.tvLastReply.setText(item.getLastReplyText());
        holder.tvFreeReturn.setText(item.getFreeReturnText());

        if (item.getImageList() != null) {
            holder.ffProduct.setVisibility(View.VISIBLE);
            holder.tvMoreImage.setText(item.getExtraImageCountText());
            holder.rvProduct.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ProductAdapter adapter = new ProductAdapter(context, item.getImageList());
            holder.rvProduct.setAdapter(adapter);
        } else {
            holder.ffProduct.setVisibility(View.GONE);
        }
    }

    private void bindViewListener(Holder holder, final InboxItemViewModel item) {
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.onItemClicked(item.getId(), item.getSellerName(), item.getCustomerName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvInvoice, tvUsernameTitle, tvUsername,
                tvAutoExecute, tvLastReply, tvFreeReturn, tvMoreImage,
                tvSeeConversation;
        ImageView ivNotification;
        RecyclerView rvProduct;
        FrameLayout ffTitleNotif, ffProduct;
        LinearLayout llItem;
        public Holder(View itemView) {
            super(itemView);
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
    }
}
