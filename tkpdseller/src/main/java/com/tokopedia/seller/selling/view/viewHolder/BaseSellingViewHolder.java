package com.tokopedia.seller.selling.view.viewHolder;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Erry on 7/25/2016.
 */
public abstract class BaseSellingViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseSellingViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindDataModel(Context context, T model);

    public abstract void setOnItemClickListener(OnItemClickListener clickListener);

    public interface OnItemClickListener {
        void onItemClicked(int position);
        void onLongClicked(int position);
    }
}
