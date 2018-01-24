package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressItemData;

import java.util.List;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressAdapter extends RecyclerView.Adapter
        <RecyclerView.ViewHolder>{

    private static final int MULTIPLE_ADDRESS_HEADER_ADAPTER_LAYOUT =
            R.layout.multiple_address_header_adapter;
    private static final int MULTIPLE_ADDRESS_ITEM_ADAPTER_LAYOUT =
            R.layout.multiple_address_item_adapter;
    private static final int MULTIPLE_ADDRESS_FOOTER_ADAPTER_LAYOUT =
            R.layout.multiple_address_footer_adapter;
    private static final int HEADER_SIZE = 1;
    private static final int FOOTER_SIZE = 1;

    private MultipleAddressAdapterData addressOrderData;

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return MULTIPLE_ADDRESS_HEADER_ADAPTER_LAYOUT;
        else if (position > 0 && position < addressOrderData.getItemListData().size())
            return MULTIPLE_ADDRESS_ITEM_ADAPTER_LAYOUT;
        else return MULTIPLE_ADDRESS_FOOTER_ADAPTER_LAYOUT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), viewType, parent);
        if(viewType == MULTIPLE_ADDRESS_HEADER_ADAPTER_LAYOUT)
            return new HeaderViewHolder(view);
        else if(viewType == MULTIPLE_ADDRESS_ITEM_ADAPTER_LAYOUT)
            return new ItemViewHolder(view);
        else return new FooterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder)
            ((HeaderViewHolder) holder).senderName.setText(addressOrderData.getSenderName());
        else if(holder instanceof ItemViewHolder) {
            MultipleAddressItemData itemData = addressOrderData.getItemListData().get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.shippingIndex.append(String.valueOf(position)); //Index 0 has already been taken by header, so the first index for item will be 1
            ImageHandler.LoadImage(itemViewHolder.productImage, itemData.getProductImageUrl());
            itemViewHolder.productName.setText(itemData.getProductName());
            itemViewHolder.productPrice.setText(itemData.getProductPrice());
            itemViewHolder.productWeight.setText(itemData.getProductWeight());
            itemViewHolder.productQty.setText(itemData.getProductQty());
            itemViewHolder.notesField.setText(itemData.getProductNotes());
            itemViewHolder.addressTitle.setText(itemData.getAddressReceiverName());
            itemViewHolder.addressReceiverName.setText(itemData.getAddressReceiverName());
            itemViewHolder.address.setText(itemData.getAddress());
            itemViewHolder.
            itemViewHolder.addressLayout.setOnClickListener(onAddressLayoutClickedListener());
        }

    }

    @Override
    public int getItemCount() {
        return HEADER_SIZE + addressOrderData.getItemListData().size() + FOOTER_SIZE;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView senderName;

        HeaderViewHolder(View itemView) {
            super(itemView);

            senderName = itemView.findViewById(R.id.sender_name);

        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView shippingIndex;

        private ImageView productImage;

        private TextView productName;

        private TextView productPrice;

        private TextView productWeight;

        private TextView productQty;

        private EditText notesField;

        private ViewGroup addressLayout;

        private TextView addressTitle;

        private TextView addressReceiverName;

        private TextView address;

        private ImageView editButton;

        private ImageView deletButton;

        ItemViewHolder(View itemView) {
            super(itemView);

            shippingIndex = itemView.findViewById(R.id.shipping_index);

            productImage = itemView.findViewById(R.id.product_image);

            productName = itemView.findViewById(R.id.product_name);

            productPrice = itemView.findViewById(R.id.product_price);

            productWeight = itemView.findViewById(R.id.product_weight);

            productQty = itemView.findViewById(R.id.product_qty);

            notesField = itemView.findViewById(R.id.notes_field);

            addressLayout = itemView.findViewById(R.id.address_layout);

            addressTitle = itemView.findViewById(R.id.address_title);

            addressReceiverName = itemView.findViewById(R.id.address_receiver_name);

            address = itemView.findViewById(R.id.address);

            addressLayout = itemView.findViewById(R.id.address_layout);

            editButton = itemView.findViewById(R.id.edit_button);

            deletButton = itemView.findViewById(R.id.delete_button);

        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup addNewShipmentButton;

        FooterViewHolder(View itemView) {
            super(itemView);

            addNewShipmentButton = itemView.findViewById(R.id.add_new_shipment_button);

        }
    }

    private View.OnClickListener onEditOrderClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private View.OnClickListener onDeleteOrderClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private View.OnClickListener onAddressLayoutClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

}
