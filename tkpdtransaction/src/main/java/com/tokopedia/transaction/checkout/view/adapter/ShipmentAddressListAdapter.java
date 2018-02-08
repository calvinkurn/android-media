package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.utils.RxBus;

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

    private static RxBus sRxBus;

    private List<ShipmentRecipientModel> mAddressModelList;
    private Context mContext;
    private ActionListener actionListener;

    public ShipmentAddressListAdapter(ActionListener actionListener) {
        sRxBus = RxBus.instanceOf();
        this.actionListener = actionListener;
    }

    public void setAddressList(List<ShipmentRecipientModel> addressModelList) {
        mAddressModelList = new ArrayList<>(addressModelList);
    }

    @Override
    public RecipientAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipient_address, parent, false);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipientAddressViewHolder holder, int position) {
        ShipmentRecipientModel address = mAddressModelList.get(position);

        holder.mTvRecipientName.setText(address.getRecipientName());
        holder.mTvRecipientAddress.setText(address.getRecipientAddress());

        holder.mAddressContainer.setOnClickListener(new OnItemClickListener(position));
        holder.mLlRadioButtonAddressSelect.setOnClickListener(new OnItemClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mAddressModelList.size();
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

//            FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
//            Fragment fragment = CartSingleAddressFragment.newInstance(cartItemDataList);
//            String backStateName = fragment.getClass().getName();
//
//            boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
//            if (!isFragmentPopped) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
//                        .addToBackStack(backStateName)
//                        .commit();
//            }

            //TODO move above code to implementation on own host fragment actionListener examp: actionListener.onFoo();
            actionListener.onAddressContainerClicked(mPosition);


            sRxBus.sendEvent(new Event(mAddressModelList.get(mPosition), "pos = " + mPosition));
        }

    }

    public interface ActionListener {
        void onAddressContainerClicked(int position);
    }

    public class Event {
        Object obj;
        String msg;

        public Event(Object obj, String msg) {
            this.obj = obj;
            this.msg = msg;
        }

        public Object getObject() {
            return obj;
        }

        public String getMessage() {
            return msg;
        }
    }

}
