package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressShipmentAdapterData;

import java.util.List;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressShipmentAdapter extends RecyclerView.Adapter
        <RecyclerView.ViewHolder>{

    private static final int MULTIPLE_ADDRESS_SHIPMENT_HEADER_LAYOUT =
            R.layout.multiple_address_header;
    private static final int MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT =
            R.layout.multiple_address_shipment_footer;
    private static final int MULTIPLE_ADDRESS_ADAPTER_SHIPMENT_LAYOUT =
            R.layout.multiple_address_shipment_adapter;
    private static final int HEADER_SIZE = 1;
    private static final int FOOTER_SIZE = 1;

    private List<MultipleAddressShipmentAdapterData> addressDataList;

    public MultipleAddressShipmentAdapter(List<MultipleAddressShipmentAdapterData> addressDataList) {
        this.addressDataList = addressDataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return MULTIPLE_ADDRESS_SHIPMENT_HEADER_LAYOUT;
        else if (position > addressDataList.size()) return MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT;
        else return MULTIPLE_ADDRESS_ADAPTER_SHIPMENT_LAYOUT;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == MULTIPLE_ADDRESS_SHIPMENT_HEADER_LAYOUT)
            return new MultipleAddressHeaderViewHolder(itemView);
        else if (viewType == MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT)
            return new MultipleAddressShipmentFooterViewHolder(itemView);
        else return new MultipleShippingAddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MultipleShippingAddressViewHolder) {
            MultipleShippingAddressViewHolder itemViewHolder = (MultipleShippingAddressViewHolder) holder;
            MultipleAddressShipmentAdapterData data = addressDataList.get(position - 1);
            MultipleAddressItemData itemData = data.getItemData();
            itemViewHolder.senderName.setText(data.getSenderName());
            ImageHandler.LoadImage(itemViewHolder.productImage, data.getProductImageUrl());
            itemViewHolder.productPrice.setText(data.getProductPrice());
            itemViewHolder.productWeight.setText(itemData.getProductWeight());
            itemViewHolder.productQty.setText(itemData.getProductQty());
            itemViewHolder.notesField.setText(itemData.getProductNotes());
            itemViewHolder.addressTitle.setText(itemData.getAddressTitle());
            itemViewHolder.addressReceiverName.setText(itemData.getAddressReceiverName());
            itemViewHolder.address.setText(itemData.getAddress());
            itemViewHolder.subTotalAmount.setText(data.getSubTotalAmount());
        } else if(holder instanceof MultipleAddressShipmentFooterViewHolder)
            ((MultipleAddressShipmentFooterViewHolder) holder).chooseCourierButton
                    .setOnClickListener(onChooseCourierClicked());
    }


    @Override
    public int getItemCount() {
        return HEADER_SIZE + addressDataList.size() + FOOTER_SIZE;
    }

    class MultipleShippingAddressViewHolder extends RecyclerView.ViewHolder {

        private TextView senderName;

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

        private TextView chooseCourierButton;

        private ViewGroup subTotalLayout;

        private TextView subTotalAmount;

        MultipleShippingAddressViewHolder(View itemView) {
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

    class MultipleAddressHeaderViewHolder extends RecyclerView.ViewHolder {

        MultipleAddressHeaderViewHolder(View itemView) {
            super(itemView);

        }
    }

    class MultipleAddressShipmentFooterViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup chooseCourierButton;

        MultipleAddressShipmentFooterViewHolder(View itemView) {
            super(itemView);

            chooseCourierButton = itemView.findViewById(R.id.choose_courier_button);


        }
    }

    private View.OnClickListener onChooseCourierClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

}
