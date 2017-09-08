package com.tokopedia.seller.goldmerchant.featured.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.goldmerchant.featured.helper.ItemTouchHelperAdapter;
import com.tokopedia.seller.goldmerchant.featured.helper.OnStartDragListener;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.viewholder.FeaturedProductViewHolder;

import java.util.Collections;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductAdapter extends BaseListAdapter<FeaturedProductModel> implements ItemTouchHelperAdapter {

    private final OnStartDragListener mDragStartListener;

    public FeaturedProductAdapter(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FeaturedProductModel.TYPE:
                return new FeaturedProductViewHolder(getLayoutView(parent, R.layout.item_gm_featured_product), mDragStartListener);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (getItemViewType(position)) {
            case FeaturedProductModel.TYPE:
                ((FeaturedProductViewHolder) holder).bindData(data.get(position));
                break;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(data, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }
}
