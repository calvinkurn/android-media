package com.tokopedia.tkpdpdp.estimasiongkir.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.tkpdpdp.estimasiongkir.ShippingServiceModel;

import java.util.ArrayList;
import java.util.List;

public class ServiceProductAdapter extends RecyclerView.Adapter<ServiceProductAdapter.ServiceProductViewHolder> {
    private List<ShippingServiceModel.Product> products;

    public ServiceProductAdapter() {
        products = new ArrayList<>();
    }

    public void replaceProducts(List<ShippingServiceModel.Product> products){
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceProductViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ServiceProductViewHolder extends RecyclerView.ViewHolder {
        public ServiceProductViewHolder(View itemView) {
            super(itemView);
        }
    }
}
