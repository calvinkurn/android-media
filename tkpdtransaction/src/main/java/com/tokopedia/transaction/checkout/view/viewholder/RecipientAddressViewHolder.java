package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.view.customview.PickupPointLayout;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

    private static final int PRIME_ADDRESS = 2;

    private TextView mTvAddressStatus;
    private TextView mTvAddressName;
    private TextView mTvRecipientName;
    private TextView mTvRecipientAddress;
    private TextView mTvRecipientPhone;
    private TextView mTvAddOrChangeAddress;
    private PickupPointLayout mPickupPointLayout;
    private TextView mTvChangeAddress;

    private SingleAddressShipmentAdapter.ActionListener mActionListener;

    public RecipientAddressViewHolder(View itemView,
                                      SingleAddressShipmentAdapter.ActionListener actionListener) {
        super(itemView);

        mTvAddressStatus = itemView.findViewById(R.id.tv_address_status);
        mTvAddressName = itemView.findViewById(R.id.tv_address_name);
        mTvRecipientName = itemView.findViewById(R.id.tv_recipient_name);
        mTvRecipientAddress = itemView.findViewById(R.id.tv_recipient_address);
        mTvRecipientPhone = itemView.findViewById(R.id.tv_recipient_phone);
        mTvAddOrChangeAddress = itemView.findViewById(R.id.tv_add_or_change_address);
        mPickupPointLayout = itemView.findViewById(R.id.pickup_point_layout);
        mTvChangeAddress = itemView.findViewById(R.id.tv_change_address);

        mActionListener = actionListener;
    }

    public void bindViewHolder(RecipientAddressModel recipientAddress) {
        mTvAddressStatus.setVisibility(recipientAddress.getAddressStatus() == PRIME_ADDRESS ?
                View.VISIBLE : View.GONE);
        mTvAddressName.setText(recipientAddress.getAddressName());
        mTvRecipientName.setText(recipientAddress.getRecipientName());
        mTvRecipientAddress.setText(getFullAddress(recipientAddress));
        mTvRecipientPhone.setText(recipientAddress.getRecipientPhoneNumber());

        mTvAddOrChangeAddress.setOnClickListener(addOrChangeAddressListener());

        renderPickupPoint(mPickupPointLayout, recipientAddress);
        mTvChangeAddress.setVisibility(View.GONE);
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
                mActionListener.onAddOrChangeAddress();
            }
        };
    }

    private PickupPointLayout.ViewListener pickupPointListener(
            final RecipientAddressModel recipientAddress) {

        return new PickupPointLayout.ViewListener() {
            @Override
            public void onChoosePickupPoint() {
                mActionListener.onChoosePickupPoint(recipientAddress);
            }

            @Override
            public void onClearPickupPoint(Store oldStore) {
                mActionListener.onClearPickupPoint(recipientAddress);
            }

            @Override
            public void onEditPickupPoint(Store oldStore) {
                mActionListener.onEditPickupPoint(recipientAddress);
            }
        };
    }

}
