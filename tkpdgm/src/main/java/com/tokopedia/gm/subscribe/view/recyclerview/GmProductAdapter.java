package com.tokopedia.gm.subscribe.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.gm.R;
import com.tokopedia.gm.subscribe.view.viewmodel.GmProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public class GmProductAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int GM_PRODUCT = 100;
    private final List<GmProductViewModel> data;
    private final GmProductAdapterCallback listener;
    private String packageName;

    public GmProductAdapter(GmProductAdapterCallback listener) {
        data = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GM_PRODUCT:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gm_subscribe_product, parent, false);
                return new GmProductViewHolder(view);
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

    public void bindData(final GmProductViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.selectedProductId(
                        Integer.valueOf(data.get(position).getProductId())
                );
                updatePackageName(position);
                GmProductAdapter.this.notifyDataSetChanged();
            }
        });
        boolean isSelected = Integer.valueOf(data.get(position).getProductId())
                .equals(listener.getSelectedProductId());
        holder.renderData(data.get(position),
                isSelected
        );
        if(isSelected){
            updatePackageName(position);
        }
    }

    private void updatePackageName(int position) {
        packageName = data.get(position).getName();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case GM_PRODUCT:
                bindData((GmProductViewHolder) viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    public void addItem(List<GmProductViewModel> lists) {
        data.addAll(lists);
        notifyDataSetChanged();
    }

    public void clearDatas() {
        data.clear();
        notifyDataSetChanged();
    }

    public String getProductSelection() {
        return packageName;
    }
}
