package com.tokopedia.seller.goldmerchant.featured.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.goldmerchant.featured.constant.FeaturedProductType;
import com.tokopedia.seller.goldmerchant.featured.helper.ItemTouchHelperAdapter;
import com.tokopedia.seller.goldmerchant.featured.helper.OnStartDragListener;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.viewholder.FeaturedProductViewHolder;

import java.util.Collections;
import java.util.List;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductAdapter extends BaseMultipleCheckListAdapter<FeaturedProductModel> implements ItemTouchHelperAdapter, FeaturedProductViewHolder.PostDataListener {

    private final OnStartDragListener mDragStartListener;
    private UseCaseListener useCaseListener;

    public FeaturedProductAdapter(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
    }

    public void setUseCaseListener(UseCaseListener useCaseListener) {
        this.useCaseListener = useCaseListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FeaturedProductModel.TYPE:
                return new FeaturedProductViewHolder(getLayoutView(parent, R.layout.item_gm_featured_product), mDragStartListener, this);
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

    @Override
    public void postData() {
        if (useCaseListener != null) {
            useCaseListener.postData(getData());
        }
    }

    @Override
    public int getFeaturedProductType() {
        if(useCaseListener != null){
            return useCaseListener.getFeaturedProductType();
        }else{
            return FeaturedProductType.DEFAULT_DISPLAY;
        }
    }

    public void removeSelections() {
        for (int i = 0; i < data.size(); i++) {
            FeaturedProductModel featuredProductModel = data.get(i);
            if (hashSet.contains(featuredProductModel.getId())) {
                hashSet.remove(featuredProductModel.getId());

                data.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public interface UseCaseListener {
        void postData(List<FeaturedProductModel> data);

        int getFeaturedProductType();
    }
}
