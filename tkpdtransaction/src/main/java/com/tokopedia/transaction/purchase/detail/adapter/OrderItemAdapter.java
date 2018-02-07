package com.tokopedia.transaction.purchase.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailView;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailItemData;

import java.util.List;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>{

    private List<OrderDetailItemData> orderItemData;
    private OrderDetailView mainView;

    public OrderItemAdapter(List<OrderDetailItemData> orderItemData, OrderDetailView mainView) {
        this.orderItemData = orderItemData;
        this.mainView = mainView;
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
        ImageHandler.LoadImage(holder.productImage, orderItemData.get(position).getImageUrl());
        holder.productLayout.setOnClickListener(
                onProductLayoutClickedListener(orderItemData.get(position))
        );
    }

    @Override
    public int getItemCount() {
        return orderItemData.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup productLayout;

        private TextView productName;

        private TextView productPrice;

        private TextView productQuantity;

        private TextView additionalNote;

        private ImageView productImage;

        OrderItemViewHolder(View itemView) {
            super(itemView);
            productLayout = (ViewGroup) itemView.findViewById(R.id.product_layout);
            productName = (TextView) itemView.findViewById(R.id.order_detail_product_name);
            productPrice = (TextView) itemView.findViewById(R.id.order_detail_product_price);
            productQuantity = (TextView) itemView.findViewById(R.id.order_detail_item_quantity);
            additionalNote = (TextView) itemView.findViewById(R.id.order_detail_notes);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }

    private View.OnClickListener onProductLayoutClickedListener(final OrderDetailItemData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductPass productPass = ProductPass.Builder.aProductPass()
                        .setProductId(data.getProductId())
                        .setProductName(data.getItemName())
                        .setProductImage(data.getImageUrl())
                        .setProductPrice(data.getPrice())
                        .build();
                mainView.goToProductInfo(productPass);
            }
        };
    }

}
