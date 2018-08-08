package com.tokopedia.tkpdpdp.estimasiongkir.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.estimasiongkir.ShippingServiceModel;
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.viewholder.RatesEstimationServiceViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RatesEstimationServiceAdapter extends RecyclerView.Adapter<RatesEstimationServiceViewHolder> {
    private List<ShippingServiceModel> shippingServiceModels = new ArrayList<>();

    @NonNull
    @Override
    public RatesEstimationServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RatesEstimationServiceViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rates_estimation_service, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RatesEstimationServiceViewHolder holder, int position) {
        holder.bind(shippingServiceModels.get(position));
    }

    @Override
    public int getItemCount() {
        return shippingServiceModels.size();
    }

    public void updateShippingServices(List<ShippingServiceModel> shippingServiceModels){
        this.shippingServiceModels.clear();
        this.shippingServiceModels.addAll(shippingServiceModels);
        notifyDataSetChanged();
    }
}
