package com.tokopedia.transaction.checkout.view.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.CartSingleAddressFragment;
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

    private static final String TAG = CartAddressListAdapter.class.getSimpleName();

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

        holder.mAddressContainer.setOnClickListener(new OnItemClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size();
    }

    class RecipientAddressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_recipient_name) TextView mTvRecipientName;
        @BindView(R2.id.tv_recipient_address) TextView mTvRecipientAddress;
        @BindView(R2.id.ll_address_radio_button_container) LinearLayout mLlRadioButtonAddressSelect;
        @BindView(R2.id.rl_shipment_recipient_address_header) RelativeLayout mAddressContainer;

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

            FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.container);

            if (fragment == null || !(fragment instanceof CartSingleAddressFragment)) {
                fragmentManager.beginTransaction().replace(R.id.container,
                        CartSingleAddressFragment.newInstance()).commit();
            }
        }

    }

}
