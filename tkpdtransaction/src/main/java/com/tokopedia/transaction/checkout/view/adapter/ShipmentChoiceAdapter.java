package com.tokopedia.transaction.checkout.view.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentChoiceAdapter extends RecyclerView.Adapter<ShipmentChoiceAdapter.ShipmentViewHolder> {

    private ViewListener viewListener;
    private List<ShipmentItemData> shipments;

    public ShipmentChoiceAdapter(List<ShipmentItemData> shipments, ViewListener viewListener) {
        this.shipments = shipments;
        this.viewListener = viewListener;
    }

    @Override
    public ShipmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item_shipment, parent, false);

        return new ShipmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ShipmentViewHolder holder, final int position) {
        ShipmentItemData shipmentItemData = shipments.get(position);
        holder.tvShipmentType.setText(shipmentItemData.getType());
        holder.tvPriceRange.setText(
                holder.tvShipmentType.getContext().getResources().getString(
                        R.string.label_shipment_type_format, shipmentItemData.getPriceRange()));
        holder.tvDeliveryTimeRange.setText(shipmentItemData.getDeliveryTimeRange());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() >= 0 && shipments.size() > holder.getAdapterPosition()) {
                    renderTypeface(holder);
                    holder.imgBtCheck.setVisibility(View.VISIBLE);
                    viewListener.onShipmentItemClick(shipments.get(holder.getAdapterPosition()));
                }
            }
        });
        if (position == shipments.size() - 1) {
            holder.vSeparator.setVisibility(View.GONE);
        } else {
            holder.vSeparator.setVisibility(View.VISIBLE);
        }
    }

    private void renderTypeface(ShipmentViewHolder holder) {
        holder.tvShipmentType.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        holder.tvPriceRange.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        holder.tvDeliveryTimeRange.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    }

    @Override
    public int getItemCount() {
        return shipments.size();
    }

    public interface ViewListener {
        void onShipmentItemClick(ShipmentItemData shipmentItemData);
    }

    static class ShipmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_shipment_type)
        TextView tvShipmentType;
        @BindView(R2.id.tv_price_range)
        TextView tvPriceRange;
        @BindView(R2.id.img_bt_check)
        ImageButton imgBtCheck;
        @BindView(R2.id.tv_delivery_time_range)
        TextView tvDeliveryTimeRange;
        @BindView(R2.id.v_separator)
        View vSeparator;

        ShipmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
