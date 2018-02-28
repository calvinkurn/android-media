package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListAdapter
        extends RecyclerView.Adapter<ShipmentAddressListAdapter.RecipientAddressViewHolder> {

    private static final String TAG = ShipmentAddressListAdapter.class.getSimpleName();

    private static final int PRIME_ADDRESS = 2;

    private List<RecipientAddressModel> mAddressModelList;
    private Context mContext;
    private ActionListener mActionListener;

    public ShipmentAddressListAdapter(ActionListener actionListener) {
        mActionListener = actionListener;
        mAddressModelList = new ArrayList<>();
    }

    public void setAddressList(List<RecipientAddressModel> addressModelList) {
        mAddressModelList = new ArrayList<>(addressModelList);
    }

    @Override
    public RecipientAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recipient_address_rb_selectable, parent, false);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipientAddressViewHolder holder, int position) {
        RecipientAddressModel address = mAddressModelList.get(position);

        holder.mTvAddressName.setText(address.getAddressName());
        holder.mTvAddressStatus.setVisibility(address.getAddressStatus() == PRIME_ADDRESS ?
                View.VISIBLE : View.GONE);
        holder.mTvRecipientName.setText(address.getRecipientName());
        holder.mTvRecipientAddress.setText(getFullAddress(address.getAddressStreet(),
                address.getDestinationDistrictName(), address.getAddressCityName(),
                address.getAddressProvinceName()));
        holder.mTvRecipientPhone.setText(address.getRecipientPhoneNumber());

        holder.mRbCheckAddress.setChecked(address.isSelected());
        holder.mTvChangeAddress.setVisibility(View.VISIBLE);
        holder.mTvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.onEditClick(mAddressModelList.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnClickListener(getItemClickListener(address, position));
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size();
    }

    private String getFullAddress(String street, String district, String city, String province) {
        return street + ", " + district + ", " + city + ", " + province;
    }

    private View.OnClickListener getItemClickListener(final RecipientAddressModel recipientAddressModel,
                                                      final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recipientAddressModel.isSelected()) {
                    for (RecipientAddressModel viewModel : mAddressModelList) {
                        if (viewModel.getId().equals(recipientAddressModel.getId())) {
                            if (mAddressModelList.size() > position && position >= 0) {
                                viewModel.setSelected(!viewModel.isSelected());
                                mActionListener.onAddressContainerClicked(mAddressModelList.get(position));
                            }
                        } else {
                            viewModel.setSelected(false);
                        }
                    }

                    notifyDataSetChanged();
                }
            }
        };
    }

    class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

        TextView mTvAddressName;
        TextView mTvAddressStatus;
        TextView mTvRecipientName;
        TextView mTvRecipientAddress;
        TextView mTvRecipientPhone;

        TextView mTvChangeAddress;
        RadioButton mRbCheckAddress;

        RelativeLayout mRlAddressContainer;
        LinearLayout mLlRadioButtonContainer;

        RecipientAddressViewHolder(View view) {
            super(view);

            mTvAddressName = view.findViewById(R.id.tv_address_name);
            mTvAddressStatus = view.findViewById(R.id.tv_address_status);
            mTvRecipientName = view.findViewById(R.id.tv_recipient_name);
            mTvRecipientAddress = view.findViewById(R.id.tv_recipient_address);
            mTvRecipientPhone = view.findViewById(R.id.tv_recipient_phone);

            mTvChangeAddress = view.findViewById(R.id.tv_change_address);
            mRbCheckAddress = view.findViewById(R.id.rb_check_address);

            mRlAddressContainer = view.findViewById(R.id.rl_address_container);
            mLlRadioButtonContainer = view.findViewById(R.id.ll_radio_button_container);
        }

    }

    /**
     * Implemented by adapter host fragment
     */
    public interface ActionListener {
        /**
         * Executed when address container is clicked
         *
         * @param model RecipientAddressModel
         */
        void onAddressContainerClicked(RecipientAddressModel model);

        /**
         * Executed when edit address button is clicked
         *
         * @param model RecipientAddressModel
         */
        void onEditClick(RecipientAddressModel model);
    }

}
