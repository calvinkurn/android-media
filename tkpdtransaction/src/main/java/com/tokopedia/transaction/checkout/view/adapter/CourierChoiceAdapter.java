package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

    public CourierChoiceAdapter(List<CourierItemData> couriers, ViewListener viewListener) {
        this.couriers = couriers;
        this.viewListener = viewListener;
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
        holder.tvPrice.setText(courierItemData.getPrice());
        holder.tvDeliveryTimeRange.setText(courierItemData.getDeliveryTimeRange());

        if (courierItemData.isSelected()) {
            holder.imgBtCheck.setImageResource(R.drawable.ic_check_circle_green);
        } else {
            holder.imgBtCheck.setImageResource(android.R.color.transparent);
        }

        holder.itemView.setOnClickListener(getItemClickListener(courierItemData, position));
    }

    private View.OnClickListener getItemClickListener(final CourierItemData courierItemData, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CourierItemData viewModel : couriers) {
                    if (viewModel.getId().equals(courierItemData.getId())) {
                        if (couriers.size() > position && position >= 0) {
                            if (viewModel.isSelected()) {
                                viewModel.setSelected(false);
                            } else {
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
        @BindView(R2.id.btn_check)
        ImageButton imgBtCheck;
        @BindView(R2.id.tv_delivery_time_range)
        TextView tvDeliveryTimeRange;

        CourierViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}