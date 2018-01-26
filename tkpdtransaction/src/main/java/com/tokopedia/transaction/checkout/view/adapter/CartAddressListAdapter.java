package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.SingleAddressRecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CartAddressListAdapter
        extends RecyclerView.Adapter<CartAddressListAdapter.RecipientAddressViewHolder> {

    private List<SingleAddressRecipientAddressModel> mAddressModelList;
    private Context mContext;

    @Inject
    CartAddressListAdapter() {
        mAddressModelList = new ArrayList<>();
    }

    public void setProducts(List<SingleAddressRecipientAddressModel> addressModelList) {
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
        SingleAddressRecipientAddressModel address = mAddressModelList.get(position);

        holder.mTvRecipientName.setText(address.getRecipientName());
        holder.mTvRecipientAddress.setText(address.getRecipientAddress());

        holder.mLlRadioButtonAddressSelect.setOnClickListener(new OnItemClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size();
    }

    class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

        TextView mTvRecipientName;
        TextView mTvRecipientAddress;
        LinearLayout mLlRadioButtonAddressSelect;

        RecipientAddressViewHolder(View view) {
            super(view);
            mTvRecipientName = view.findViewById(R.id.tv_recipient_name);
            mTvRecipientAddress = view.findViewById(R.id.tv_recipient_address);
            mLlRadioButtonAddressSelect = view.findViewById(R.id.ll_address_radio_button_container);
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
