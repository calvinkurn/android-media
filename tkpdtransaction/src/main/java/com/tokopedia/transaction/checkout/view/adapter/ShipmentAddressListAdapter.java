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
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public class ShipmentAddressListAdapter
        extends RecyclerView.Adapter<ShipmentAddressListAdapter.RecipientAddressViewHolder> {

    private static final String TAG = ShipmentAddressListAdapter.class.getSimpleName();

    private List<ShipmentRecipientModel> mAddressModelList;
    private Context mContext;
    private ActionListener mActionListener;

    public ShipmentAddressListAdapter(ActionListener actionListener) {
        this.mActionListener = actionListener;
    }

    public void setAddressList(List<ShipmentRecipientModel> addressModelList) {
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
        ShipmentRecipientModel address = mAddressModelList.get(position);

        holder.mRbCheckAddress.setChecked(address.isSelected());
        holder.mTvRecipientName.setText(address.getRecipientName());
        holder.mTvRecipientAddress.setText(address.getRecipientAddress());
        holder.mTvPhoneNumber.setText(address.getRecipientPhoneNumber());
        holder.mTvTextAddressDescription.setText(address.getRecipientAddressDescription());

        holder.mTvAddressIdentifier.setText(address.getAddressIdentifier());
        holder.mTvAddressIdentifier.setVisibility(address.isPrimerAddress() ? View.VISIBLE : View.GONE);

        holder.mAddressContainer.setOnClickListener(new OnItemClickListener(position));
        holder.mLlRadioButtonAddressSelect.setOnClickListener(new OnItemClickListener(position));
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

    private View.OnClickListener getItemClickListener(final ShipmentRecipientModel courierItemData,
                                                      final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ShipmentRecipientModel viewModel : mAddressModelList) {
                    if (viewModel.getId().equals(courierItemData.getId())) {
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
        };
    }

    class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_recipient_name)
        TextView mTvRecipientName;
        @BindView(R2.id.tv_recipient_address)
        TextView mTvRecipientAddress;
        @BindView(R2.id.ll_address_radio_button_container)
        LinearLayout mLlRadioButtonAddressSelect;
        @BindView(R2.id.rl_shipment_recipient_address_header)
        RelativeLayout mAddressContainer;
        @BindView(R2.id.tv_recipient_phone)
        TextView mTvPhoneNumber;
        @BindView(R2.id.tv_text_address_description)
        TextView mTvTextAddressDescription;
        @BindView(R2.id.tv_address_identifier)
        TextView mTvAddressIdentifier;
        @BindView(R2.id.tv_change_address)
        TextView mTvChangeAddress;
        @BindView(R2.id.rb_check_address)
        RadioButton mRbCheckAddress;

        RecipientAddressViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    private class OnItemClickListener implements View.OnClickListener {

        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            String msg = String.format("Address list was clicked at %s position", mPosition);
            Log.d(TAG, msg);
            // TODO add an implementation on own host fragment mActionListener
        }

    }

    /**
     * Implemented by adapter host fragment
     */
    public interface ActionListener {
        /**
         * Executed when address container is clicked
         * @param model ShipmentRecipientModel
         */
        void onAddressContainerClicked(ShipmentRecipientModel model);

        /**
         * Executed when edit address button is clicked
         * @param model ShipmentRecipientModel
         */
        void onEditClick(ShipmentRecipientModel model);
    }

}
