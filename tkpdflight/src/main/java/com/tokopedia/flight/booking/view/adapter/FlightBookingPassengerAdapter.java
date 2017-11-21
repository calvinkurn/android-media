package com.tokopedia.flight.booking.view.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageRouteViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealRouteViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingPassengerAdapter extends RecyclerView.Adapter<FlightBookingPassengerAdapter.ViewHolder> {
    private List<FlightBookingPassengerViewModel> viewModels;
    private OnClickListener listener;

    public interface OnClickListener {
        void onChangePassengerData(FlightBookingPassengerViewModel viewModel);
    }

    public FlightBookingPassengerAdapter() {
        viewModels = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_booking_passenger, parent, false);
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

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void addPassengers(List<FlightBookingPassengerViewModel> viewModels) {
        this.viewModels = viewModels;
        notifyDataSetChanged();
    }

    public List<FlightBookingPassengerViewModel> getPassengers() {
        return viewModels;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvHeaderTitle;
        private AppCompatTextView tvChangePassengerData;
        private LinearLayout passengerDetailLayout;
        private AppCompatTextView tvPassengerName;
        private RecyclerView rvPassengerDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View view) {
            tvHeaderTitle = (AppCompatTextView) view.findViewById(R.id.tv_header_title);
            tvChangePassengerData = (AppCompatTextView) view.findViewById(R.id.tv_change_passenger_data);
            passengerDetailLayout = (LinearLayout) view.findViewById(R.id.passenger_detail_layout);
            tvPassengerName = (AppCompatTextView) view.findViewById(R.id.tv_passenger_name);
            rvPassengerDetail = (RecyclerView) view.findViewById(R.id.rv_list_details);
        }

        public void bind(final FlightBookingPassengerViewModel viewModel) {
            tvHeaderTitle.setText(String.valueOf(viewModel.getHeaderTitle()));
            tvChangePassengerData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onChangePassengerData(viewModel);
                    }
                }
            });
            if (viewModel.getPassengerName() != null) {
                passengerDetailLayout.setVisibility(View.VISIBLE);
                tvChangePassengerData.setText(itemView.getContext().getString(R.string.flight_booking_passenger_change_label));
                tvPassengerName.setText(String.valueOf(viewModel.getPassengerName()));
                List<SimpleViewModel> simpleViewModels = new ArrayList<>();
                if (viewModel.getPassengerName() != null && viewModel.getPassengerName().length() > 0) {
                    simpleViewModels.add(new SimpleViewModel(itemView.getContext().getString(R.string.flight_booking_list_passenger_birthdate_label), String.valueOf(FlightDateUtil.formatDate(
                            FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, viewModel.getPassengerBirthdate()
                    ))));
                }

                if (viewModel.getFlightBookingLuggageRouteViewModels() != null) {
                    for (FlightBookingLuggageRouteViewModel flightBookingLuggageRouteViewModel : viewModel.getFlightBookingLuggageRouteViewModels()) {
                        simpleViewModels.add(new SimpleViewModel(
                                itemView.getContext().getString(R.string.flight_booking_list_passenger_luggage_label) + flightBookingLuggageRouteViewModel.getRoute().getDepartureAirport() + " - " + flightBookingLuggageRouteViewModel.getRoute().getArrivalAirport(), String.valueOf(FlightDateUtil.formatDate(
                                FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, TextUtils.join(" + ", flightBookingLuggageRouteViewModel.getLuggage())
                        ))));
                    }
                }

                if (viewModel.getFlightBookingMealRouteViewModels() != null && viewModel.getFlightBookingMealRouteViewModels().size() > 0) {
                    for (FlightBookingMealRouteViewModel flightBookingMealRouteViewModel : viewModel.getFlightBookingMealRouteViewModels()) {
                        simpleViewModels.add(new SimpleViewModel(
                                itemView.getContext().getString(R.string.flight_booking_list_passenger_meals_label) + flightBookingMealRouteViewModel.getRoute().getDepartureAirport() + " - " + flightBookingMealRouteViewModel.getRoute().getArrivalAirport(), String.valueOf(FlightDateUtil.formatDate(
                                FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, TextUtils.join(" + ", flightBookingMealRouteViewModel.getMealViewModels())
                        ))));
                    }
                }

                FlightSimpleAdapter adapter = new FlightSimpleAdapter();
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
                rvPassengerDetail.setLayoutManager(layoutManager);
                rvPassengerDetail.setHasFixedSize(true);
                rvPassengerDetail.setNestedScrollingEnabled(false);
                rvPassengerDetail.setAdapter(adapter);
                adapter.setViewModels(simpleViewModels);
                adapter.notifyDataSetChanged();

            } else {
                passengerDetailLayout.setVisibility(View.GONE);
                tvChangePassengerData.setText(itemView.getContext().getString(R.string.flight_booking_passenger_fill_data_label));
            }
        }
    }
}
