package com.tokopedia.inbox.rescenter.product.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.product.view.model.ListProductViewItem;
import com.tokopedia.inbox.rescenter.product.view.presenter.ListProductFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public class ListProductAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_SHIPPING_ITEM = 100;

    private final ListProductFragmentView fragmentView;
    private List<ListProductViewItem> arraylist;
    private Context context;

    public ListProductAdapter(ListProductFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        this.arraylist = new ArrayList<>();
    }

    public void setArraylist(List<ListProductViewItem> arraylist) {
        this.arraylist = arraylist;
    }

    public List<ListProductViewItem> getArraylist() {
        return arraylist;
    }

    @SuppressWarnings("WeakerAccess")
    public class ListProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;

        public ListProductViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.iv_product_image);
            productName = (TextView) itemView.findViewById(R.id.tv_product_name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_SHIPPING_ITEM:
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.recyclerview_complaint_product_item_2, parent, false);
                return new ListProductViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_SHIPPING_ITEM:
                bindShippingViewHolder((ListProductViewHolder) viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    private void bindShippingViewHolder(ListProductViewHolder holder, int position) {
        context = holder.itemView.getContext();
        final ListProductViewItem item = arraylist.get(position);
        renderData(holder, item);
    }

    private void renderData(ListProductViewHolder holder, ListProductViewItem item) {
        ImageHandler.LoadImage(holder.productImage, item.getProductImageUrl());
        holder.productName.setText(item.getProductName());
        holder.itemView.setOnClickListener(
                new OnProductClick(
                        item.getResCenterProductID(),
                        item.getProductName()
                ));
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (arraylist.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_SHIPPING_ITEM;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == arraylist.size();
    }

    @Override
    public int getItemCount() {
        return arraylist.size() + super.getItemCount();
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
            fragmentView.setOnProductItemClick(productID, productName);
        }
    }
}
