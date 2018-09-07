package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/10/17.
 */

public class ComplaintProductAdapter extends RecyclerView.Adapter<ComplaintProductAdapter.ComplaintProductVH> {

    private final DetailResCenterFragmentView listener;
    private List<ProductItem> productItems;
    private boolean limit;

    public ComplaintProductAdapter(DetailResCenterFragmentView listener) {
        this.listener = listener;
        this.productItems = new ArrayList<>();
    }

    public static ComplaintProductAdapter createDefaultInstance(DetailResCenterFragmentView listener, List<ProductItem> productItems) {
        ComplaintProductAdapter adapter = new ComplaintProductAdapter(listener);
        adapter.setProductItems(productItems);
        adapter.setLimit(false);
        adapter.notifyDataSetChanged();
        return adapter;
    }

    public static ComplaintProductAdapter createLimitInstance(DetailResCenterFragmentView listener, List<ProductItem> productItems) {
        ComplaintProductAdapter adapter = new ComplaintProductAdapter(listener);
        adapter.setProductItems(productItems);
        adapter.setLimit(true);
        adapter.notifyDataSetChanged();
        return adapter;
    }

    public boolean isLimit() {
        return limit;
    }

    public void setLimit(boolean limit) {
        this.limit = limit;
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }

    @Override
    public ComplaintProductVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_complaint_product_item, parent, false);
        return new ComplaintProductVH(view);
    }

    @Override
    public void onBindViewHolder(ComplaintProductVH holder, int position) {
        ImageHandler.LoadImage(holder.productImage, getProductItems().get(position).getProductImageUrl());
        holder.productName.setText(getProductItems().get(position).getProductName());
        holder.itemView.setOnClickListener(
                new OnProductClick(
                        getProductItems().get(position).getProductID(),
                        getProductItems().get(position).getProductName()
                ));
    }

    @Override
    public int getItemCount() {
        if (isLimit()) {
            return getProductItems().size() < 3 ? getProductItems().size() : 3;
        } else {
            return getProductItems().size();
        }
    }

    public class ComplaintProductVH extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;

        public ComplaintProductVH(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.iv_product_image);
            productName = (TextView) itemView.findViewById(R.id.tv_product_name);
        }
    }

    private class OnProductClick implements View.OnClickListener {
        private final String productID;
        private final String productName;

        public OnProductClick(String productID, String productName) {
            this.productID = productID;
            this.productName = productName;
        }

        @Override
        public void onClick(View view) {
            listener.setOnActionProductClick(productID, productName);
        }
    }
}
