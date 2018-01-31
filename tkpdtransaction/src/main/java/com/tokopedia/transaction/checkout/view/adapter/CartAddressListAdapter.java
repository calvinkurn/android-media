package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.data.ShippingRecipientModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public class CartAddressListAdapter
        extends RecyclerView.Adapter<CartAddressListAdapter.RecipientAddressViewHolder> {

    private List<ShippingRecipientModel> mAddressModelList;
    private Context mContext;

    public CartAddressListAdapter() {
        mAddressModelList = new ArrayList<>();
    }

    public void setAddressList(List<ShippingRecipientModel> addressModelList) {
        mAddressModelList.addAll(addressModelList);
    }

    @Override
    public RecipientAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipient_address, parent, false);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipientAddressViewHolder holder, int position) {
        ShippingRecipientModel address = mAddressModelList.get(position);

        holder.mTvRecipientName.setText(address.getRecipientName());
        holder.mTvRecipientAddress.setText(address.getRecipientAddress());

        holder.mLlRadioButtonAddressSelect.setOnClickListener(new OnItemClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size();
    }

    class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_recipient_name) TextView mTvRecipientName;
        @BindView(R2.id.tv_recipient_address) TextView mTvRecipientAddress;
        @BindView(R2.id.ll_address_radio_button_container) LinearLayout mLlRadioButtonAddressSelect;

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

        }

    }

}
