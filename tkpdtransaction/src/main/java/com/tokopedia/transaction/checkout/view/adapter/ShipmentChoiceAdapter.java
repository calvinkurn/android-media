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
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentItemData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentChoiceAdapter extends RecyclerView.Adapter<ShipmentChoiceAdapter.ShipmentViewHolder> {

    private static final String FONT_FAMILY_SANS_SERIF = "sans-serif";
    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";
    private ViewListener viewListener;
    private List<ShipmentItemData> shipments;

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public void setShipments(List<ShipmentItemData> shipments) {
        this.shipments = shipments;
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

        if (shipmentItemData.getSinglePriceRange() != null) {
            holder.tvPriceRange.setText(holder.tvShipmentType.getContext().getResources().getString(
                    R.string.label_shipment_type_format, shipmentItemData.getSinglePriceRange()));
            holder.tvPriceRange.setVisibility(View.VISIBLE);
        } else if (shipmentItemData.getMultiplePriceRange() != null) {
            holder.tvPriceRange.setText(holder.tvShipmentType.getContext().getResources().getString(
                    R.string.label_shipment_type_format, shipmentItemData.getMultiplePriceRange()));
            holder.tvPriceRange.setVisibility(View.VISIBLE);
        } else {
            holder.tvPriceRange.setVisibility(View.GONE);
        }

        holder.tvDeliveryTimeRange.setText(shipmentItemData.getDeliveryTimeRange());
        holder.itemView.setOnClickListener(getItemClickListener(shipmentItemData, position));

        if (shipmentItemData.isSelected()) {
            holder.imgBtCheck.setVisibility(View.VISIBLE);
        } else {
            holder.imgBtCheck.setVisibility(View.GONE);
        }

        if (position == shipments.size() - 1) {
            holder.vSeparator.setVisibility(View.GONE);
        } else {
            holder.vSeparator.setVisibility(View.VISIBLE);
        }

        renderTypeface(holder, shipmentItemData);
    }

    private View.OnClickListener getItemClickListener(final ShipmentItemData shipmentItemData,
                                                      final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ShipmentItemData viewModel : shipments) {
                    if (viewModel.getServiceId() == shipmentItemData.getServiceId()) {
                        if (shipments.size() > position && position >= 0) {
                            if (!viewModel.isSelected()) {
                                viewModel.setSelected(true);
                            }
                            viewListener.onShipmentItemClick(shipments.get(position));
                        }
                    } else {
                        viewModel.setSelected(false);
                    }
                }
                notifyDataSetChanged();
            }
        };
    }

    private void renderTypeface(ShipmentViewHolder holder, ShipmentItemData shipmentItemData) {
        if (shipmentItemData.isSelected()) {
            holder.tvShipmentType.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
            holder.tvPriceRange.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
            holder.tvDeliveryTimeRange.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
        } else {
            holder.tvShipmentType.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF, Typeface.NORMAL));
            holder.tvPriceRange.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF, Typeface.NORMAL));
            holder.tvDeliveryTimeRange.setTypeface(
                    Typeface.create(FONT_FAMILY_SANS_SERIF, Typeface.NORMAL));
        }
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
