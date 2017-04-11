package com.tokopedia.topads.sdk.base.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author by errysuprayogi on 4/10/17.
 */

public abstract class AbstractAdsViewHolder<T> extends RecyclerView.ViewHolder {


    private Context context;
    private T itemData;

    public AbstractAdsViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    public T getItemData() {
        return itemData;
    }

    public void setItemData(T itemData) {
        this.itemData = itemData;
    }

    public Context getContext() {
        return context;
    }

    public abstract void populateViewHolder(T data, int pos);

    public abstract void attachOnClickListeners(AbstractAdsViewHolder viewHolder, T item, int pos);
}
