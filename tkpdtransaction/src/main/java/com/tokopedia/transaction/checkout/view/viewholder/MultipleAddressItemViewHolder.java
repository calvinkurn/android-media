package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;

import java.util.List;

/**
 * Created by kris on 3/14/18. Tokopedia
 */

public class MultipleAddressItemViewHolder extends RecyclerView.ViewHolder{

    private static final int SINGLE_DATA_SIZE = 1;

    private TextView shippingIndex;

    private ImageView editButton;

    private TextView pseudoEditButton;

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

    public MultipleAddressItemViewHolder(View itemView) {
        super(itemView);

        shippingIndex = itemView.findViewById(R.id.shipping_index);

        editButton = itemView.findViewById(R.id.edit_button);

        pseudoEditButton = itemView.findViewById(R.id.tv_change_address);

        deleteButton = itemView.findViewById(R.id.delete_button);

        notesForSeller = itemView.findViewById(R.id.notes_for_seller);

        productWeight = itemView.findViewById(R.id.product_weight);

        productQty = itemView.findViewById(R.id.product_qty);

        notesForSeller = itemView.findViewById(R.id.notes_for_seller);

        addressLayout = itemView.findViewById(R.id.address_layout);

        addressTitle = itemView.findViewById(R.id.tv_address_name);

        addressReceiverName = itemView.findViewById(R.id.tv_recipient_name);

        address = itemView.findViewById(R.id.tv_recipient_address);

        borderLine = itemView.findViewById(R.id.border_line);

        phoneNumber = itemView.findViewById(R.id.tv_recipient_phone);

        phoneNumber.setVisibility(View.GONE);
        
    }
    
    public void bindItemAdapterAddress(MultipleAddressItemData itemData,
                                       List<MultipleAddressItemData> itemDataList,
                                       View.OnClickListener onEditClickedListener,
                                       View.OnClickListener onDeleteClicekdListener,
                                       int position) {

        shippingIndex.setText(
                shippingIndex.getText().toString().replace(
                        "#", String.valueOf(itemData.getAddressPosition() + 1)
                )
        );
        productWeight.setText(itemData.getProductWeight());
        productQty.setText(itemData.getProductQty());
        notesForSeller.setText(itemData.getProductNotes());
        addressTitle.setText(itemData.getAddressTitle());
        addressReceiverName.setText(itemData.getAddressReceiverName());
        address.setText(itemData.getAddressStreet()
                + ", " + itemData.getAddressCityName()
                + ", " + itemData.getAddressProvinceName()
                + ", " + itemData.getRecipientPhoneNumber());
        pseudoEditButton.setVisibility(View.GONE);
        editButton.setOnClickListener(onEditClickedListener);
        deleteButton.setOnClickListener(onDeleteClicekdListener);
        addressLayout.setOnClickListener(
                onAddressLayoutClickedListener(itemData)
        );
        if(itemDataList.size() == SINGLE_DATA_SIZE) deleteButton.setVisibility(View.GONE);
        else deleteButton.setVisibility(View.VISIBLE);
        if(position == itemDataList.size() - 1) borderLine.setVisibility(View.GONE);
        else borderLine.setVisibility(View.VISIBLE);
        
    }

    private View.OnClickListener onAddressLayoutClickedListener(MultipleAddressItemData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }
}
