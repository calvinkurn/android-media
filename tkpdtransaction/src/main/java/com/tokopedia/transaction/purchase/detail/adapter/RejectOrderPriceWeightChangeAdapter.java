package com.tokopedia.transaction.purchase.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 1/10/18. Tokopedia
 */

public class RejectOrderPriceWeightChangeAdapter extends RecyclerView.Adapter<RejectOrderPriceWeightChangeAdapter.RejectOrderWeightPriceViewHolder> {

    private List<WrongProductPriceWeightEditable> productPriceWeightData;

    private List<WrongProductPriceWeightEditable> originalPriceWeightData;

    private RejectOrderPriceWeightAdapterListener listener;

    public RejectOrderPriceWeightChangeAdapter(
            List<WrongProductPriceWeightEditable> productPriceWeightData,
            RejectOrderPriceWeightAdapterListener listener) {
        this.productPriceWeightData = productPriceWeightData;
        this.originalPriceWeightData = new ArrayList<>();
        for (int i = 0; i < productPriceWeightData.size(); i++) {
            originalPriceWeightData.add(
                    new WrongProductPriceWeightEditable(productPriceWeightData.get(i))
            );
        }
        this.listener = listener;
    }

    @Override
    public RejectOrderWeightPriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_reject_price_weight_adapter, parent, false);
        return new RejectOrderWeightPriceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RejectOrderWeightPriceViewHolder holder, int position) {
        holder.productName.setText(productPriceWeightData.get(position).getProductName());
        holder.productPrice.setText(productPriceWeightData.get(position).getProductPrice());
        holder.productWeight.setText(productPriceWeightData.get(position).getProductWeight());
        ImageHandler.LoadImage(holder.productImage, productPriceWeightData.get(position).getProductImage());
        holder.productLayout.setOnClickListener(
                onProductLayoutClickedListener(productPriceWeightData.get(position))
        );
    }

    public List<WrongProductPriceWeightEditable> getProductPriceWeightData() {
        return productPriceWeightData;
    }

    public boolean isEdited() {
        boolean isEdited = false;
        for (int i = 0; i < productPriceWeightData.size(); i++) {
            if(!productPriceWeightData.get(i).equals(originalPriceWeightData.get(i)))
                isEdited = true;
        }
        return isEdited;
    }

    @Override
    public int getItemCount() {
        return productPriceWeightData.size();
    }

    class RejectOrderWeightPriceViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup productLayout;

        private TextView productName;

        private TextView productPrice;

        private TextView productWeight;

        private ImageView productImage;

        RejectOrderWeightPriceViewHolder(View itemView) {
            super(itemView);
            productLayout = itemView.findViewById(R.id.product_layout);
            productName = itemView.findViewById(R.id.order_detail_product_name);
            productPrice = itemView.findViewById(R.id.order_detail_product_price);
            productWeight = itemView.findViewById(R.id.order_detail_weight);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }

    private View.OnClickListener onProductLayoutClickedListener(
            final WrongProductPriceWeightEditable data
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProductChoosen(data);
            }
        };
    }

    public interface RejectOrderPriceWeightAdapterListener {

        void onProductChoosen(WrongProductPriceWeightEditable model);

    }

}
