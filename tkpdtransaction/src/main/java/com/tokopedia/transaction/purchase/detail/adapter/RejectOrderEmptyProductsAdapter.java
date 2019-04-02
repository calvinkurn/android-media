package com.tokopedia.transaction.purchase.detail.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyProductEditable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 1/10/18. Tokopedia
 */

public class RejectOrderEmptyProductsAdapter extends RecyclerView.Adapter<RejectOrderEmptyProductsAdapter.RejectOrderEmptyProductViewHolder> {

    private List<EmptyProductEditable> emptyProductEditables;

    private String orderId;

    public RejectOrderEmptyProductsAdapter(String orderId,
                                           List<EmptyProductEditable> emptyProductEditables) {
        this.orderId = orderId;
        this.emptyProductEditables = emptyProductEditables;
    }

    @Override
    public RejectOrderEmptyProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_reject_empty_product_adapter, parent, false);
        return new RejectOrderEmptyProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RejectOrderEmptyProductViewHolder holder, int position) {
        holder.productName.setText(emptyProductEditables.get(position).getProductName());
        holder.productPrice.setText(emptyProductEditables.get(position).getProductPrice());
        ImageHandler.LoadImage(
                holder.productImage,
                emptyProductEditables.get(position).getProductImage()
        );
        holder.stockEmptyCheckBox.setOnCheckedChangeListener(null);
        holder.stockEmptyCheckBox.setChecked(emptyProductEditables.get(position).isSelected());
        if(emptyProductEditables.get(position).isSelected())
            holder.emptyStockCard.setVisibility(View.VISIBLE);
        else holder.emptyStockCard.setVisibility(View.INVISIBLE);
        holder.stockEmptyCheckBox.setOnCheckedChangeListener(
                onCheckBoxClickedListener(holder.emptyStockCard, emptyProductEditables.get(position))
        );
    }

    @Override
    public int getItemCount() {
        return emptyProductEditables.size();
    }

    class RejectOrderEmptyProductViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;

        private TextView productPrice;

        private ImageView productImage;

        private CheckBox stockEmptyCheckBox;

        private CardView emptyStockCard;

        RejectOrderEmptyProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.order_detail_product_name);
            productPrice = itemView.findViewById(R.id.order_detail_product_price);
            productImage = itemView.findViewById(R.id.product_image);
            stockEmptyCheckBox = itemView.findViewById(R.id.stock_epmty_checkbox);
            emptyStockCard = itemView.findViewById(R.id.empty_stock_label);
        }
    }

    public TKPDMapParam<String, String> getMappedProductParameter() {
        List<String> productIdList = new ArrayList<>();
        for(int i = 0; i < emptyProductEditables.size(); i++) {
            if(emptyProductEditables.get(i).isSelected()) {
                productIdList.add(emptyProductEditables.get(i).getProductId());
            }
        }
        if(productIdList.isEmpty()) {
            return new TKPDMapParam<>();
        } else {
            TKPDMapParam<String, String> productListParam = new TKPDMapParam<>();
            productListParam.put("list_product_id", generateProductIdList(productIdList));
            productListParam.put("order_id", orderId);
            productListParam.put("reason_code", "1");
            return productListParam;
        }
    }

    private String generateProductIdList(List<String> productIdList) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < productIdList.size(); i++) {
            builder.append(productIdList.get(i));
            if(i != productIdList.size() - 1) builder.append("~");
        }
        return builder.toString();
    }

    private CompoundButton.OnCheckedChangeListener onCheckBoxClickedListener(
            final CardView emptyStockCardView,
            final EmptyProductEditable emptyProductEditable
    ) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) emptyStockCardView.setVisibility(View.VISIBLE);
                else emptyStockCardView.setVisibility(View.INVISIBLE);
                emptyProductEditable.setSelected(checked);
            }
        };
    }

}
