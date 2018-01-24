package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressShippingAdapterData;

import java.util.List;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleShippingAddressAdapter extends RecyclerView.Adapter
        <MultipleShippingAddressAdapter.MultipleShippingAddressViewHolder>{

    private List<MultipleAddressShippingAdapterData> addressDataList;

    public MultipleShippingAddressAdapter(List<MultipleAddressShippingAdapterData> addressDataList) {
        this.addressDataList = addressDataList;
    }

    @Override
    public MultipleShippingAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View
                .inflate(parent.getContext(), R.layout.multiple_address_shipping_adapter, parent);
        return new MultipleShippingAddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MultipleShippingAddressViewHolder holder, int position) {
        MultipleAddressShippingAdapterData data = addressDataList.get(position);
        holder.senderName.setText(data.getSenderName());
        ImageHandler.LoadImage(holder.productImage, data.getProductImageUrl());
        holder.productPrice.setText(data.getProductPrice());
        holder.productWeight.setText(data.getProductWeight());
        holder.productQty.setText(data.getProductQty());
        holder.notesField.setText(data.getNotesField());
        holder.addressTitle.setText(data.getAddressTitle());
        holder.addressReceiverName.setText(data.getAddressReceiverName());
        holder.address.setText(data.getAddress());
        holder.subTotalAmount.setText(data.getSubTotalAmount());
    }


    @Override
    public int getItemCount() {
        return addressDataList.size();
    }

    class MultipleShippingAddressViewHolder extends RecyclerView.ViewHolder {

        private TextView senderName;

        private ImageView productImage;

        private TextView productPrice;

        private TextView productWeight;

        private TextView productQty;

        private EditText notesField;

        private ViewGroup addressLayout;

        private TextView addressTitle;

        private TextView addressReceiverName;

        private TextView address;

        private TextView chooseCourierButton;

        private ViewGroup subTotalLayout;

        private TextView subTotalAmount;

        public MultipleShippingAddressViewHolder(View itemView) {
            super(itemView);

            senderName = itemView.findViewById(R.id.sender_name);

            productImage = itemView.findViewById(R.id.product_image);

            productPrice = itemView.findViewById(R.id.product_price);

            productWeight = itemView.findViewById(R.id.product_weight);

            productQty = itemView.findViewById(R.id.product_qty);

            notesField = itemView.findViewById(R.id.notes_field);

            addressLayout = itemView.findViewById(R.id.address_layout);

            addressTitle = itemView.findViewById(R.id.address_title);

            addressReceiverName = itemView.findViewById(R.id.address_receiver_name);

            address = itemView.findViewById(R.id.address);

            chooseCourierButton = itemView.findViewById(R.id.choose_courier_button);

            subTotalLayout = itemView.findViewById(R.id.sub_total_layout);

            subTotalAmount = itemView.findViewById(R.id.sub_total_amount);
        }
    }

}
