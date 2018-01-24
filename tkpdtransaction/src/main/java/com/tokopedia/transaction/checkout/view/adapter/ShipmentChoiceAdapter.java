package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentChoiceAdapter extends RecyclerView.Adapter<ShipmentChoiceAdapter.ShipmentViewHolder> {

    private Listener listener;

    public ShipmentChoiceAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public ShipmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item_shipment_choice, parent, false);

        return new ShipmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShipmentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface Listener {
        void onItemClick();
    }

    protected class ShipmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.rb_selected_item)
        RadioButton rbSelectedItem;
        @BindView(R2.id.tv_shipment_type)
        TextView tvShipmentType;
        @BindView(R2.id.tv_delivery_time_range)
        TextView tvDeliveryTimeRange;
        @BindView(R2.id.tv_price_range)
        TextView tvPriceRange;

        ShipmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
