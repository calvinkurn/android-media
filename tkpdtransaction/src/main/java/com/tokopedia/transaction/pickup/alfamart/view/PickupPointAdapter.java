package com.tokopedia.transaction.pickup.alfamart.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.pickup.alfamart.domain.model.Store;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointAdapter extends RecyclerView.Adapter<PickupPointAdapter.PickupPointViewHolder> {

    private ArrayList<Store> stores;
    private Listener listener;

    public PickupPointAdapter(ArrayList<Store> stores, Listener listener) {
        this.stores = stores;
        this.listener = listener;
    }

    @Override
    public PickupPointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item_pickup_point, parent, false);

        return new PickupPointViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PickupPointViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public interface Listener {
        void onItemClick(Store store);
    }

    protected class PickupPointViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        PickupPointViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (stores.size() > getAdapterPosition() && getAdapterPosition() >= 0) {
                listener.onItemClick(stores.get(getAdapterPosition()));
            }
        }
    }
}
