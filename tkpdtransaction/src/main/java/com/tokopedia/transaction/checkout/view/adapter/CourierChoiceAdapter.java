package com.tokopedia.transaction.checkout.view.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class CourierChoiceAdapter extends RecyclerView.Adapter<CourierChoiceAdapter.CourierViewHolder> {

    private ViewListener viewListener;
    private List<CourierItemData> couriers;

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public void setCouriers(List<CourierItemData> couriers) {
        this.couriers = couriers;
    }

    @Override
    public CourierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item_courier, parent, false);

        return new CourierViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CourierViewHolder holder, int position) {
        CourierItemData courierItemData = couriers.get(position);
        holder.tvCourierName.setText(courierItemData.getName());
        holder.tvPrice.setText(
                holder.tvPrice.getContext().getResources().getString(
                        R.string.label_shipment_type_format, courierItemData.getDeliveryPrice()));
        if (courierItemData.getDeliverySchedule() != null) {
            holder.tvDeliverySchedule.setText(courierItemData.getDeliverySchedule());
            holder.tvDeliverySchedule.setVisibility(View.VISIBLE);
        } else {
            holder.tvDeliverySchedule.setVisibility(View.GONE);
        }

        if (courierItemData.getEstimatedTimeDelivery() != null) {
            holder.tvDeliveryTimeRange.setText(courierItemData.getEstimatedTimeDelivery());
            holder.tvDeliveryTimeRange.setVisibility(View.VISIBLE);
        } else {
            holder.tvDeliveryTimeRange.setVisibility(View.GONE);
        }
        renderTypeface(holder, courierItemData);

        holder.itemView.setOnClickListener(getItemClickListener(courierItemData, position));
    }

    private void renderTypeface(CourierViewHolder holder, CourierItemData courierItemData) {
        if (courierItemData.isSelected()) {
            holder.rbSelected.setChecked(true);
            holder.tvCourierName.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            holder.tvPrice.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            holder.tvDeliveryTimeRange.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            holder.tvDeliverySchedule.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        } else {
            holder.rbSelected.setChecked(false);
            holder.tvCourierName.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            holder.tvPrice.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            holder.tvDeliveryTimeRange.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            holder.tvDeliverySchedule.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        }
    }

    private View.OnClickListener getItemClickListener(final CourierItemData courierItemData, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CourierItemData viewModel : couriers) {
                    if (viewModel.getId().equals(courierItemData.getId())) {
                        if (couriers.size() > position && position >= 0) {
                            if (!viewModel.isSelected()) {
                                viewModel.setSelected(true);
                            }
                            viewListener.onCourierItemClick(couriers.get(position));
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
        return couriers.size();
    }

    public interface ViewListener {
        void onCourierItemClick(CourierItemData courierItemData);
    }

    static class CourierViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_courier_name)
        TextView tvCourierName;
        @BindView(R2.id.tv_price)
        TextView tvPrice;
        @BindView(R2.id.tv_delivery_time_range)
        TextView tvDeliveryTimeRange;
        @BindView(R2.id.rb_selected)
        RadioButton rbSelected;
        @BindView(R2.id.tv_delivery_schedule)
        TextView tvDeliverySchedule;

        CourierViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}