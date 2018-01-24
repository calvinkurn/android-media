package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressAdapterData;

import java.util.List;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressAdapter extends RecyclerView.Adapter
        <MultipleAddressAdapter.MultipleAddressViewHolder>{


    private List<MultipleAddressAdapterData> addressData;

    public MultipleAddressAdapter(List<MultipleAddressAdapterData> addressData) {
        this.addressData = addressData;
    }

    @Override
    public MultipleAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multiple_address_adapter, parent, false);
        return new MultipleAddressViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(MultipleAddressViewHolder holder, int position) {
        MultipleAddressAdapterData data = addressData.get(position);
        holder.senderName.setText(data.getSenderName());
        holder.productName.setText(data.getProductName());
        holder.productPrice.setText(data.getProductPrice());
        ImageHandler.LoadImage(holder.productImage, data.getProductImageUrl());
        holder.shippingDestinationList.setLayoutManager(new LinearLayoutManager(holder.context));
        holder.shippingDestinationList.setAdapter(
                new MultipleAddressItemAdapter(data.getItemListData())
        );
    }

    @Override
    public int getItemCount() {
        return addressData.size();
    }

    class MultipleAddressViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        private TextView senderName;

        private ImageView productImage;

        private TextView productName;

        private TextView productPrice;

        private RecyclerView shippingDestinationList;

        MultipleAddressViewHolder(Context context, View itemView) {
            super(itemView);

            this.context = context;

            senderName = itemView.findViewById(R.id.sender_name);

            productImage = itemView.findViewById(R.id.product_image);

            productName = itemView.findViewById(R.id.product_name);

            productPrice = itemView.findViewById(R.id.product_price);

            shippingDestinationList = itemView.findViewById(R.id.shipping_destination_list);

        }
    }

/*    private static final int MULTIPLE_ADDRESS_HEADER_ADAPTER_LAYOUT =
            R.layout.multiple_address_adapter;
    private static final int MULTIPLE_ADDRESS_ITEM_ADAPTER_LAYOUT =
            R.layout.multiple_address_item_adapter;
    private static final int MULTIPLE_ADDRESS_FOOTER_ADAPTER_LAYOUT =
            R.layout.multiple_address_footer_adapter;

    private List<MultipleAddressAdapterData> addressOrderDataList;

    public MultipleAddressAdapter(List<MultipleAddressAdapterData> addressOrderDataList) {
        this.addressOrderDataList = addressOrderDataList;
    }

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
            MultipleAddressItemData itemData = addressOrderData.getItemListData()
                    .get(position - HEADER_SIZE);
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
            itemViewHolder.editButton.setOnClickListener(onEditOrderClickedListener(itemData));
            itemViewHolder.deleteButton.setOnClickListener(onDeleteOrderClickedListener(itemData));
            itemViewHolder.addressLayout.setOnClickListener(
                    onAddressLayoutClickedListener(itemData)
            );
        } else {
            ((FooterViewHolder) holder).addNewShipmentButton
                    .setOnClickListener(onAddNewShipmentButtonClickedListener(
                            addressOrderData.getItemListData().get(0))
                    );
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

        private ImageView deleteButton;

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

            editButton = itemView.findViewById(R.id.edit_button);

            deleteButton = itemView.findViewById(R.id.delete_button);

        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup addNewShipmentButton;

        FooterViewHolder(View itemView) {
            super(itemView);

            addNewShipmentButton = itemView.findViewById(R.id.add_new_shipment_button);

        }
    }

    private View.OnClickListener onEditOrderClickedListener(MultipleAddressItemData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private View.OnClickListener onDeleteOrderClickedListener(MultipleAddressItemData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    private View.OnClickListener onAddNewShipmentButtonClickedListener(MultipleAddressItemData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }*/

}
