package com.tokopedia.transaction.pickuppoint.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.view.model.StoreViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointAdapter extends RecyclerView.Adapter<PickupPointAdapter.PickupPointViewHolder> {

    private ArrayList<StoreViewModel> stores;
    private Listener listener;

    public PickupPointAdapter(ArrayList<StoreViewModel> stores, Listener listener) {
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

        final StoreViewModel storeViewModel = stores.get(position);

        holder.tvStoreName.setText(storeViewModel.getStore().getStoreName());
        holder.tvStoreAddress.setText(storeViewModel.getStore().getAddress());

        if (storeViewModel.isChecked()) {
            holder.btnCheck.setImageResource(R.drawable.ic_check_circle_green);
        } else {
            holder.btnCheck.setImageResource(R.drawable.bg_circle_grey_2);
        }

        holder.tvShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemShowMapClick(storeViewModel.getStore());
            }
        });

        holder.btnCheck.setOnClickListener(getItemClickListener(storeViewModel, position));

        holder.cvContainer.setOnClickListener(getItemClickListener(storeViewModel, position));

    }

    private View.OnClickListener getItemClickListener(final StoreViewModel storeViewModel, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (StoreViewModel viewModel : stores) {
                    if (viewModel.getStore().getId() == storeViewModel.getStore().getId()) {
                        if (stores.size() > position && position >= 0) {
                            if (viewModel.isChecked()) {
                                viewModel.setChecked(false);
                                listener.onItemClick(stores.get(position).getStore(), false);
                            } else {
                                viewModel.setChecked(true);
                                listener.onItemClick(stores.get(position).getStore(), true);
                            }
                        }
                    } else {
                        viewModel.setChecked(false);
                    }
                }
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public interface Listener {
        void onItemClick(Store store, boolean selected);

        void onItemShowMapClick(Store store);
    }

    protected class PickupPointViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.cv_container)
        CardView cvContainer;
        @BindView(R2.id.btn_check)
        ImageButton btnCheck;
        @BindView(R2.id.tv_store_name)
        TextView tvStoreName;
        @BindView(R2.id.tv_store_address)
        TextView tvStoreAddress;
        @BindView(R2.id.tv_show_map)
        TextView tvShowMap;

        PickupPointViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}