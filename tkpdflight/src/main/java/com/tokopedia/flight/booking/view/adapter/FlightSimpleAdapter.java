package com.tokopedia.flight.booking.view.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/21/17.
 */

public class FlightSimpleAdapter extends RecyclerView.Adapter<FlightSimpleAdapter.ViewHolder> {
    private List<SimpleViewModel> viewModels;

    public FlightSimpleAdapter() {
        viewModels = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(viewModels.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public void setViewModels(List<SimpleViewModel> viewModels) {
        this.viewModels = viewModels;
    }

    public void addViewModels(List<SimpleViewModel> viewModels) {
        this.viewModels = viewModels;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView labelTextView;
        private AppCompatTextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            labelTextView = (AppCompatTextView) itemView.findViewById(R.id.tv_label);
            descriptionTextView = (AppCompatTextView) itemView.findViewById(R.id.tv_description);
        }

        public void bind(SimpleViewModel viewModel) {
            labelTextView.setText(viewModel.getLabel());
            descriptionTextView.setText(viewModel.getDescription());
        }
    }
}
