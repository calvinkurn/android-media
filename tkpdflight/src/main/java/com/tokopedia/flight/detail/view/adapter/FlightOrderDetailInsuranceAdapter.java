package com.tokopedia.flight.detail.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.flight.orderlist.domain.model.FlightInsurance;

import java.util.List;

public class FlightOrderDetailInsuranceAdapter extends RecyclerView.Adapter<FlightOrderDetailInsuranceAdapter.ViewHolder> {
    private List<FlightInsurance> insurances;

    public FlightOrderDetailInsuranceAdapter(List<FlightInsurance> insurances) {
        this.insurances = insurances;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return insurances.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
