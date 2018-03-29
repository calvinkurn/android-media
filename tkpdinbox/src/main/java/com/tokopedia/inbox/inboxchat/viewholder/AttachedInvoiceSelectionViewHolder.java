package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachinvoice.view.viewholder.InvoiceViewHolder;
import com.tokopedia.inbox.inboxchat.listener.AttachedInvoiceOnSelectedListener;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSingleViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.mapper.AttachInvoiceMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hendri on 28/03/18.
 */

public class AttachedInvoiceSelectionViewHolder  extends AbstractViewHolder<AttachInvoiceSelectionViewModel> {

    @LayoutRes
    public static final int LAYOUT = 2;
    private ChatRoomContract.View selectedListener;
    private AttachedInvoiceSelectionViewHolder.AttachedInvoicesItemsAdapter singleItemAdapter;

    public AttachedInvoiceSelectionViewHolder(View itemView,ChatRoomContract.View selectedListener) {
        super(itemView);
        this.selectedListener = selectedListener;
        singleItemAdapter = new AttachedInvoicesItemsAdapter();
    }

    @Override
    public void bind(AttachInvoiceSelectionViewModel element) {
        singleItemAdapter.setList(element.getList());
    }

    private class AttachedInvoicesItemsAdapter extends RecyclerView.Adapter<AttachedInvoiceSingleItemViewHolder>{
        List<AttachInvoiceSingleViewModel> list;
        @Override
        public AttachedInvoiceSingleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_carousel_invoice_attach, parent, false);
            return new AttachedInvoiceSingleItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AttachedInvoiceSingleItemViewHolder holder, int position) {
            holder.bind(list.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedListener.onInvoiceSelected(
                            AttachInvoiceMapper.selectedInvoiceViewModelToSelectedInvoice(list.get(getAdapterPosition()))
                    );
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public List<AttachInvoiceSingleViewModel> getList() {
            return list;
        }

        public void setList(List<AttachInvoiceSingleViewModel> list) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    private class AttachedInvoiceSingleItemViewHolder extends RecyclerView.ViewHolder{

        private TextView invoiceNo;
        private TextView invoiceDate;
        private TextView productName;
        private TextView productDesc;
        private TextView invoiceStatus;
        private TextView totalAmount;
        private ImageView productImage;

        public AttachedInvoiceSingleItemViewHolder(View itemView) {
            super(itemView);
            invoiceNo = itemView.findViewById(R.id.attach_invoice_item_invoice_no);
            invoiceDate = itemView.findViewById(R.id.attach_invoice_item_invoice_date);
            productName = itemView.findViewById(R.id.attach_invoice_item_product_name);
            productDesc = itemView.findViewById(R.id.attach_invoice_item_product_desc);
            invoiceStatus = itemView.findViewById(R.id.attach_invoice_item_invoice_status);
            totalAmount = itemView.findViewById(R.id.attach_invoice_item_invoice_total);
            productImage = itemView.findViewById(R.id.attach_invoice_item_product_image);
        }

        public void bind(AttachInvoiceSingleViewModel element){
            invoiceNo.setText(element.getCode());
            if(!TextUtils.isEmpty(element.getImageUrl())) {
                productImage.setVisibility(View.VISIBLE);
                ImageHandler.loadImageAndCache(productImage, element.getImageUrl());
            }
            else {
                productImage.setVisibility(View.GONE);
            }
            invoiceDate.setText(element.getCreatedTime());
            productName.setText(element.getTitle());
            productDesc.setText(element.getDescription());
            invoiceStatus.setText(element.getStatus());
            totalAmount.setText(element.getAmount());
        }
    }
}
