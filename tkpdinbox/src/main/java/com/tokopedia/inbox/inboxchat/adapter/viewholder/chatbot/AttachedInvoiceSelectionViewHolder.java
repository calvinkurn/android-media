package com.tokopedia.inbox.inboxchat.adapter.viewholder.chatbot;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.invoiceattachment.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.invoiceattachment.AttachInvoiceSingleViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.invoiceattachment.mapper.AttachInvoiceMapper;

import java.util.List;

/**
 * Created by Hendri on 28/03/18.
 */

public class AttachedInvoiceSelectionViewHolder extends
        AbstractViewHolder<AttachInvoiceSelectionViewModel> {
    private static final int ADDITIONAL_GET_ALL_BUTTON = 1;
    @LayoutRes
    public static final int LAYOUT = R.layout.item_chat_invoice_attach_selection;
    private ChatRoomContract.View selectedListener;
    private AttachedInvoiceSelectionViewHolder.AttachedInvoicesItemsAdapter singleItemAdapter;
    private RecyclerView invoiceSelection;

    public AttachedInvoiceSelectionViewHolder(View itemView, ChatRoomContract.View
            selectedListener) {
        super(itemView);
        this.selectedListener = selectedListener;
        singleItemAdapter = new AttachedInvoicesItemsAdapter();
        invoiceSelection = itemView.findViewById(R.id.attach_invoice_chat_invoice_selection);
        invoiceSelection.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        invoiceSelection.setAdapter(singleItemAdapter);
    }

    @Override
    public void bind(AttachInvoiceSelectionViewModel element) {
        singleItemAdapter.setList(element.getList());
    }

    private class AttachedInvoicesItemsAdapter extends RecyclerView
            .Adapter<AttachedInvoiceSingleItemViewHolder> {
        List<AttachInvoiceSingleViewModel> list;

        @Override
        public AttachedInvoiceSingleItemViewHolder onCreateViewHolder(ViewGroup parent, int
                viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_carousel_invoice_attach, parent, false);
            return new AttachedInvoiceSingleItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AttachedInvoiceSingleItemViewHolder holder, final int
                position) {
            if (position < list.size()) {
                holder.bind(list.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedListener.onInvoiceSelected(
                                AttachInvoiceMapper.selectedInvoiceViewModelToSelectedInvoice
                                        (list.get(position))
                        );
                    }
                });
            } else if (position == list.size()) {
                holder.bind(new AttachInvoiceSingleViewModel(true));
            }
        }

        @Override
        public int getItemCount() {
            if (list == null) return 0;
            return list.size() + ADDITIONAL_GET_ALL_BUTTON;
        }

        public List<AttachInvoiceSingleViewModel> getList() {
            return list;
        }

        public void setList(List<AttachInvoiceSingleViewModel> list) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    private class AttachedInvoiceSingleItemViewHolder extends RecyclerView.ViewHolder {

        private TextView invoiceNo;
        private TextView invoiceDate;
        private TextView productName;
        private TextView productDesc;
        private TextView invoiceStatus;
        private TextView totalAmount;
        private ImageView productImage;
        private View invoiceContainer;
        private View buttonContainer;
        private ImageView searchAllButton;

        public AttachedInvoiceSingleItemViewHolder(View itemView) {
            super(itemView);
            invoiceNo = itemView.findViewById(R.id.attach_invoice_item_invoice_no);
            invoiceDate = itemView.findViewById(R.id.attach_invoice_item_invoice_date);
            productName = itemView.findViewById(R.id.attach_invoice_item_product_name);
            productDesc = itemView.findViewById(R.id.attach_invoice_item_product_desc);
            invoiceStatus = itemView.findViewById(R.id.attach_invoice_item_invoice_status);
            totalAmount = itemView.findViewById(R.id.attach_invoice_item_invoice_total);
            productImage = itemView.findViewById(R.id.attach_invoice_item_product_image);
            invoiceContainer = itemView.findViewById(R.id.container_all_invoice_attach);
            searchAllButton = itemView.findViewById(R.id.all_invoice_button);
            buttonContainer = itemView.findViewById(R.id
                    .container_invoice_attach_get_all_invoice_button);
            searchAllButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedListener.showSearchInvoiceScreen();
                }
            });
        }

        public void bind(AttachInvoiceSingleViewModel element) {
            if (element.isSearchAllButton()) {
                invoiceContainer.setVisibility(View.GONE);
                buttonContainer.setVisibility(View.VISIBLE);
            } else {
                invoiceContainer.setVisibility(View.VISIBLE);
                buttonContainer.setVisibility(View.GONE);
                invoiceNo.setText(element.getCode());
                if (!TextUtils.isEmpty(element.getImageUrl())) {
                    productImage.setVisibility(View.VISIBLE);
                    ImageHandler.loadImageAndCache(productImage, element.getImageUrl());
                } else {
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
}
