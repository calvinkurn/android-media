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

        mActionListener = actionListener;
    }

    public void bindViewHolder(RecipientAddressModel recipientAddressModel) {
        mTvAddressStatus.setVisibility(recipientAddressModel.getAddressStatus() == PRIME_ADDRESS ?
                View.VISIBLE : View.GONE);
        mTvAddressName.setText(recipientAddressModel.getAddressName());
        mTvRecipientName.setText(recipientAddressModel.getRecipientName());
        mTvRecipientAddress.setText(getFullAddress(recipientAddressModel));
        mTvRecipientPhone.setText(recipientAddressModel.getRecipientPhoneNumber());

        mTvAddOrChangeAddress.setOnClickListener(addOrChangeAddressListener());

        renderPickupPoint(mPickupPointLayout, recipientAddressModel);
    }

    private String getFullAddress(RecipientAddressModel recipientAddressModel) {
        return recipientAddressModel.getAddressStreet() + ", "
                + recipientAddressModel.getDestinationDistrictName() + ", "
                + recipientAddressModel.getAddressCityName() + ", "
                + recipientAddressModel.getAddressProvinceName();
    }

    private void renderPickupPoint(PickupPointLayout pickupPointLayout,
                                   final RecipientAddressModel recipientAddressModel) {

        pickupPointLayout.setListener(pickupPointListener(recipientAddressModel));

        if (recipientAddressModel.getStore() == null) {
            pickupPointLayout.unSetData(pickupPointLayout.getContext());
            pickupPointLayout.enableChooserButton(pickupPointLayout.getContext());
        } else {
            pickupPointLayout.setData(pickupPointLayout.getContext(),
                    recipientAddressModel.getStore());
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
            final RecipientAddressModel recipientAddressModel) {

        return new PickupPointLayout.ViewListener() {
            @Override
            public void onChoosePickupPoint() {
                mActionListener.onChoosePickupPoint(recipientAddressModel);
            }

            @Override
            public void onClearPickupPoint(Store oldStore) {
                mActionListener.onClearPickupPoint(recipientAddressModel);
            }

            @Override
            public void onEditPickupPoint(Store oldStore) {
                mActionListener.onEditPickupPoint(recipientAddressModel);
            }
        };
    }

}
