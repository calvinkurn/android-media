package com.tokopedia.seller.shop.open.view.adapter.expandableadapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.tokopedia.seller.shop.open.view.adapter.expandableadapter.listeners.OnGroupClickListener;

/**
 * ViewHolder for the ExpandableGroup
 * <p>
 * The current implementation does now allow for sub {@link View} of the parent view to trigger
 * a collapse / expand. *Only* click events on the parent {@link View} will trigger a collapse or
 * expand
 */
public abstract class GroupViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    private OnGroupClickListener listener;

    public GroupViewHolder(View itemView) {
        super(itemView);
        if (isGroupClickable()) {
            itemView.setOnClickListener(this);
        }
    }

    protected boolean isGroupClickable(){
        return true;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onGroupClick(getAdapterPosition());
        }
    }

    public void setOnGroupClickListener(OnGroupClickListener listener) {
        this.listener = listener;
    }

    public void expand() {
    }

    public void collapse() {
    }
}
