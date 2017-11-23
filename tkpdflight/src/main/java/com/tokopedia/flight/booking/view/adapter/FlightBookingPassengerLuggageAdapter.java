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
 * @author by alvarisi on 11/17/17.
 */

public class FlightBookingPassengerLuggageAdapter extends RecyclerView.Adapter<FlightBookingPassengerLuggageAdapter.ViewHolder> {
    private List<SimpleViewModel> simpleViewModels;
    private OnAdapterInteractionListener interactionListener;

    public interface OnAdapterInteractionListener {

        void onItemClickListener(int id);
    }

    public FlightBookingPassengerLuggageAdapter() {
        simpleViewModels = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_booking_passenger_luggage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(simpleViewModels.get(holder.getAdapterPosition()));
    }

    public void setInteractionListener(OnAdapterInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public int getItemCount() {
        return simpleViewModels.size();
    }

    public void setSimpleViewModels(List<SimpleViewModel> simpleViewModels) {
        this.simpleViewModels = simpleViewModels;
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
            if (viewModel != null) {
                labelTextView.setText(String.valueOf(viewModel.getLabel()));
                descriptionTextView.setText(String.valueOf(viewModel.getDescription()));
                descriptionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (interactionListener != null) {
                            interactionListener.onItemClickListener(getAdapterPosition());
                        }
                    }
                });
            }

        }
    }
}
