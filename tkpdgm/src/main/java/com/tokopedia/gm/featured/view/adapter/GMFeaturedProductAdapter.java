package com.tokopedia.gm.featured.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.gm.featured.constant.GMFeaturedProductType;
import com.tokopedia.gm.featured.helper.ItemTouchHelperAdapter;
import com.tokopedia.gm.featured.helper.OnStartDragListener;
import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;
import com.tokopedia.gm.featured.view.adapter.viewholder.GMFeaturedProductViewHolder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductAdapter extends BaseMultipleCheckListAdapter<GMFeaturedProductModel> implements ItemTouchHelperAdapter, GMFeaturedProductViewHolder.PostDataListener {

    private static final String TAG = "GMFeaturedProductAdapter";
    private final OnStartDragListener mDragStartListener;
    private UseCaseListener useCaseListener;

    public GMFeaturedProductAdapter(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
    }

    public void setUseCaseListener(UseCaseListener useCaseListener) {
        this.useCaseListener = useCaseListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GMFeaturedProductModel.TYPE:
                return new GMFeaturedProductViewHolder(getLayoutView(parent, R.layout.item_gm_featured_product), mDragStartListener, this);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (getItemViewType(position)) {
            case GMFeaturedProductModel.TYPE:
                ((GMFeaturedProductViewHolder) holder).bindData(data.get(position));
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
            return GMFeaturedProductType.DEFAULT_DISPLAY;
        }
    }

    public void clearSelections() {
        hashSet.clear();
    }

    public void removeSelections() {
        int i = 0;
        for(Iterator<GMFeaturedProductModel> iterator = data.iterator(); iterator.hasNext(); ){
            GMFeaturedProductModel GMFeaturedProductModel = iterator.next();
            if (hashSet.contains(GMFeaturedProductModel.getId())) {
                hashSet.remove(GMFeaturedProductModel.getId());

                iterator.remove();
            }
            i++;
        }
        notifyDataSetChanged();
    }

    public int getSelectedSize() {
        return hashSet.size();
    }

    public interface UseCaseListener {
        void postData(List<GMFeaturedProductModel> data);

        int getFeaturedProductType();
    }
}
