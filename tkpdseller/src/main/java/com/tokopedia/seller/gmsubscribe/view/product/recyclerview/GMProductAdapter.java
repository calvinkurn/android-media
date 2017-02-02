package com.tokopedia.seller.gmsubscribe.view.product.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmsubscribe.view.product.viewmodel.GMProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public class GMProductAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int GM_PRODUCT = 100;
    private final List<GMProductViewModel> data;
    private final GMProductAdapterCallback listener;

    public GMProductAdapter(GMProductAdapterCallback listener) {
        data = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GM_PRODUCT:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_gmsubscribe_product, parent, false);
                return new GMProductViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }



    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (data.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return GM_PRODUCT;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == data.size();
    }

    public void bindData(final GMProductViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.selectedProductId(
                        Integer.valueOf(data.get(position).getProductId())
                );
                GMProductAdapter.this.notifyDataSetChanged();
            }
        });
        holder.renderData(data.get(position),
                Integer.valueOf(data.get(position).getProductId())
                        == listener.getSelectedProductId()
        );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case GM_PRODUCT:
                bindData((GMProductViewHolder) viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(List<GMProductViewModel> lists){
        data.addAll(lists);
        notifyDataSetChanged();
    }
}
