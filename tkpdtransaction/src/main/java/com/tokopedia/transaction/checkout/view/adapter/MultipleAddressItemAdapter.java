package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressItemData;

import java.util.List;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressItemAdapter extends RecyclerView.Adapter
        <MultipleAddressItemAdapter.MultipleAddressItemViewHolder>{

    private List<MultipleAddressItemData> itemDataList;

    private MultipleAddressItemAdapterListener listener;

    private MultipleAddressAdapterData productData;

    MultipleAddressItemAdapter(MultipleAddressAdapterData productData,
                               List<MultipleAddressItemData> itemDataList,
                               MultipleAddressItemAdapterListener listener) {
        this.itemDataList = itemDataList;
        this.listener = listener;
        this.productData = productData;
    }

    @Override
    public MultipleAddressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multiple_address_item_adapter, parent, false);
        return new MultipleAddressItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MultipleAddressItemViewHolder holder, int position) {
        MultipleAddressItemData itemData = itemDataList.get(position);
        holder.shippingIndex.setText(
                holder.shippingIndex.getText().toString().replace(
                        "#", String.valueOf(position + 1)
                )
        );
        holder.productWeight.setText(itemData.getProductWeight());
        holder.productQty.setText(itemData.getProductQty());
        holder.notesForSeller.setText(itemData.getProductNotes());
        holder.addressTitle.setText(itemData.getAddressTitle());
        holder.addressReceiverName.setText(itemData.getAddressReceiverName());
        holder.address.setText(itemData.getAddress());
        holder.editButton.setOnClickListener(onEditOrderClickedListener(itemData));
        holder.deleteButton.setOnClickListener(onDeleteOrderClickedListener(position));
        holder.addressLayout.setOnClickListener(
                onAddressLayoutClickedListener(itemData)
        );
        if(itemDataList.size() == 1) holder.deleteButton.setVisibility(View.GONE);
        else holder.deleteButton.setVisibility(View.VISIBLE);
        if(position == itemDataList.size() - 1) holder.borderLine.setVisibility(View.GONE);
        else holder.borderLine.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    class MultipleAddressItemViewHolder extends RecyclerView.ViewHolder {

        private TextView shippingIndex;

        private ImageView editButton;

        private ImageView deleteButton;

        private TextView notesForSeller;

        private TextView productWeight;

        private TextView productQty;

        private ViewGroup addressLayout;

        private TextView addressTitle;

        private TextView addressReceiverName;

        private TextView address;

        private View borderLine;

        private TextView phoneNumber;

        MultipleAddressItemViewHolder(View itemView) {
            super(itemView);

            shippingIndex = itemView.findViewById(R.id.shipping_index);

            editButton = itemView.findViewById(R.id.edit_button);

            deleteButton = itemView.findViewById(R.id.delete_button);

            notesForSeller = itemView.findViewById(R.id.notes_for_seller);

            productWeight = itemView.findViewById(R.id.product_weight);

            productQty = itemView.findViewById(R.id.product_qty);

            notesForSeller = itemView.findViewById(R.id.notes_for_seller);

            addressLayout = itemView.findViewById(R.id.address_layout);

            addressTitle = itemView.findViewById(R.id.tv_text_address_description);

            addressReceiverName = itemView.findViewById(R.id.tv_recipient_name);

            address = itemView.findViewById(R.id.tv_recipient_address);

            borderLine = itemView.findViewById(R.id.border_line);

            phoneNumber = itemView.findViewById(R.id.tv_recipient_phone);
            phoneNumber.setVisibility(View.GONE);
        }

    }

    private View.OnClickListener onEditOrderClickedListener(final MultipleAddressItemData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditItemChoosen(productData, data);
            }
        };
    }

    private View.OnClickListener onDeleteOrderClickedListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemDataList.remove(position);
                notifyDataSetChanged();
            }
        };
    }

    private View.OnClickListener onAddressLayoutClickedListener(MultipleAddressItemData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    public interface MultipleAddressItemAdapterListener {

        void onEditItemChoosen(MultipleAddressAdapterData productData,
                               MultipleAddressItemData addressData);

    }

}
