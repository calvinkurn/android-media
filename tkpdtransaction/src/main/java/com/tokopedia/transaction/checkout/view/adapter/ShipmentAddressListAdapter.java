package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipient_address_rb_selectable, parent, false);
        return new RecipientAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipientAddressViewHolder holder, int position) {
        ShipmentRecipientModel address = mAddressModelList.get(position);

        if (address.isSelected()) {
            holder.rbCheckAddress.setChecked(true);
        } else {
            holder.rbCheckAddress.setChecked(false);
        }

        holder.mTvRecipientName.setText(address.getRecipientName());
        holder.mTvRecipientAddress.setText(address.getRecipientAddress());
        holder.tvPhoneNumber.setText(address.getRecipientPhoneNumber());
        holder.tvTextAddressDescription.setText(address.getRecipientAddressDescription());
        if (address.isPrimerAddress()) {
            holder.tvAddressIdentifier.setText(address.getAddressIdentifier());
            holder.tvAddressIdentifier.setVisibility(View.VISIBLE);
        } else {
            holder.tvAddressIdentifier.setVisibility(View.GONE);
        }

        holder.mAddressContainer.setOnClickListener(new OnItemClickListener(position));
        holder.mLlRadioButtonAddressSelect.setOnClickListener(new OnItemClickListener(position));
        holder.tvChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onEditClick(mAddressModelList.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnClickListener(getItemClickListener(address, position));
    }

    private View.OnClickListener getItemClickListener(final ShipmentRecipientModel courierItemData,
                                                      final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ShipmentRecipientModel viewModel : mAddressModelList) {
                    if (viewModel.getId().equals(courierItemData.getId())) {
                        if (mAddressModelList.size() > position && position >= 0) {
                            if (!viewModel.isSelected()) {
                                viewModel.setSelected(true);
                            }
                            actionListener.onAddressContainerClicked(mAddressModelList.get(position));
                        }
                    } else {
                        viewModel.setSelected(false);
                    }
                }
                notifyDataSetChanged();
            }
        };
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
        @BindView(R2.id.tv_recipient_phone)
        TextView tvPhoneNumber;
        @BindView(R2.id.tv_text_address_description)
        TextView tvTextAddressDescription;
        @BindView(R2.id.tv_address_identifier)
        TextView tvAddressIdentifier;
        @BindView(R2.id.tv_change_address)
        TextView tvChangeAddress;
        @BindView(R2.id.rb_check_address)
        RadioButton rbCheckAddress;

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
//            actionListener.onAddressContainerClicked(mPosition);


            sRxBus.sendEvent(new Event(mAddressModelList.get(mPosition), "pos = " + mPosition));
        }

    }

    public interface ActionListener {
        void onAddressContainerClicked(ShipmentRecipientModel model);

        void onEditClick(ShipmentRecipientModel model);
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
