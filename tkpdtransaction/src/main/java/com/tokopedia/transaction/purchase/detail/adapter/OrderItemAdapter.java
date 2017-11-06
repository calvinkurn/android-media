package com.tokopedia.transaction.purchase.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.OrderDetailItemData;

import java.util.List;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>{

    private List<OrderDetailItemData> orderItemData;

    public OrderItemAdapter(List<OrderDetailItemData> orderItemData) {
        this.orderItemData = orderItemData;
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_adapter, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {
        holder.productName.setText(orderItemData.get(position).getItemName());
        holder.productPrice.setText(orderItemData.get(position).getPrice());
        holder.productQuantity.setText(orderItemData.get(position).getItemQuantity());
        holder.additionalNote.setText(orderItemData.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return orderItemData.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;

        private TextView productPrice;

        private TextView productQuantity;

        private TextView additionalNote;

        OrderItemViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.order_detail_product_name);
            productPrice = (TextView) itemView.findViewById(R.id.order_detail_product_price);
            productQuantity = (TextView) itemView.findViewById(R.id.order_detail_item_quantity);
            additionalNote = (TextView) itemView.findViewById(R.id.order_detail_notes);
        }
    }

}
