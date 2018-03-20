package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter.ActionListener;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.view.customview.PickupPointLayout;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

    private static final int PRIME_ADDRESS = 2;

    private TextView tvAddressStatus;
    private TextView tvAddressName;
    private TextView tvRecipientName;
    private TextView tvRecipientAddress;
    private TextView tvRecipientPhone;
    private TextView tvAddOrChangeAddress;
    private PickupPointLayout pickupPointLayout;
    private TextView tvChangeAddress;

    private ActionListener actionListener;

    public RecipientAddressViewHolder(View itemView, ActionListener actionListener) {
        super(itemView);

        this.actionListener = actionListener;

        tvAddressStatus = itemView.findViewById(R.id.tv_address_status);
        tvAddressName = itemView.findViewById(R.id.tv_address_name);
        tvRecipientName = itemView.findViewById(R.id.tv_recipient_name);
        tvRecipientAddress = itemView.findViewById(R.id.tv_recipient_address);
        tvRecipientPhone = itemView.findViewById(R.id.tv_recipient_phone);
        tvAddOrChangeAddress = itemView.findViewById(R.id.tv_add_or_change_address);
        pickupPointLayout = itemView.findViewById(R.id.pickup_point_layout);
        tvChangeAddress = itemView.findViewById(R.id.tv_change_address);
    }

    public void bindViewHolder(RecipientAddressModel recipientAddress) {
        tvAddressStatus.setVisibility(recipientAddress.getAddressStatus() == PRIME_ADDRESS ?
                View.VISIBLE : View.GONE);
        tvAddressName.setText(recipientAddress.getAddressName());
        tvRecipientName.setText(recipientAddress.getRecipientName());
        tvRecipientAddress.setText(getFullAddress(recipientAddress));
        tvRecipientPhone.setText(recipientAddress.getRecipientPhoneNumber());

        tvAddOrChangeAddress.setOnClickListener(addOrChangeAddressListener());

        renderPickupPoint(pickupPointLayout, recipientAddress);
        tvChangeAddress.setVisibility(View.GONE);
    }

    private String getFullAddress(RecipientAddressModel recipientAddress) {
        return recipientAddress.getAddressStreet() + ", "
                + recipientAddress.getDestinationDistrictName() + ", "
                + recipientAddress.getAddressCityName() + ", "
                + recipientAddress.getAddressProvinceName();
    }

    private void renderPickupPoint(PickupPointLayout pickupPointLayout,
                                   final RecipientAddressModel recipientAddress) {

        pickupPointLayout.setListener(pickupPointListener(recipientAddress));

        if (recipientAddress.getStore() == null) {
            pickupPointLayout.unSetData(pickupPointLayout.getContext());
            pickupPointLayout.enableChooserButton(pickupPointLayout.getContext());
        } else {
            pickupPointLayout.setData(pickupPointLayout.getContext(),
                    recipientAddress.getStore());
        }

    }

    private View.OnClickListener addOrChangeAddressListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onAddOrChangeAddress();
            }
        };
    }

    private PickupPointLayout.ViewListener pickupPointListener(
            final RecipientAddressModel recipientAddress) {

        return new PickupPointLayout.ViewListener() {
            @Override
            public void onChoosePickupPoint() {
                actionListener.onChoosePickupPoint(recipientAddress);
            }

            @Override
            public void onClearPickupPoint(Store oldStore) {
                actionListener.onClearPickupPoint(recipientAddress);
            }

            @Override
            public void onEditPickupPoint(Store oldStore) {
                actionListener.onEditPickupPoint(recipientAddress);
            }
        };
    }

}
