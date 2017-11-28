package com.tokopedia.flight.booking.view.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.design.label.LabelView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealMetaViewModel;
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

        private LabelView headerLabel;
        private LinearLayout passengerDetailLayout;
        private AppCompatTextView tvPassengerName;
        private RecyclerView rvPassengerDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View view) {
            headerLabel = (LabelView) view.findViewById(R.id.header_label);
            passengerDetailLayout = (LinearLayout) view.findViewById(R.id.passenger_detail_layout);
            tvPassengerName = (AppCompatTextView) view.findViewById(R.id.tv_passenger_name);
            rvPassengerDetail = (RecyclerView) view.findViewById(R.id.rv_list_details);
        }

        public void bind(final FlightBookingPassengerViewModel viewModel) {
            headerLabel.setTitle(String.valueOf(viewModel.getHeaderTitle()));
            headerLabel.setContentColorValue(itemView.getResources().getColor(R.color.colorPrimary));
            headerLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onChangePassengerData(viewModel);
                    }
                }
            });
            if (viewModel.getPassengerName() != null) {
                passengerDetailLayout.setVisibility(View.VISIBLE);
                headerLabel.setContent(itemView.getContext().getString(R.string.flight_booking_passenger_change_label));
                String passengerName = viewModel.getPassengerName();
                if (viewModel.getPassengerTitle() != null && viewModel.getPassengerTitle().length() > 0) {
                    passengerName = String.format("%s %s", viewModel.getPassengerTitle(), passengerName);
                }
                tvPassengerName.setText(String.valueOf(passengerName));
                List<SimpleViewModel> simpleViewModels = new ArrayList<>();
                if (viewModel.getPassengerBirthdate() != null && viewModel.getPassengerBirthdate().length() > 0) {
                    simpleViewModels.add(new SimpleViewModel(itemView.getContext().getString(R.string.flight_booking_list_passenger_birthdate_label), String.valueOf(FlightDateUtil.formatDate(
                            FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, viewModel.getPassengerBirthdate()
                    ))));
                }

                if (viewModel.getFlightBookingLuggageMetaViewModels() != null) {
                    for (FlightBookingLuggageMetaViewModel flightBookingLuggageRouteViewModel : viewModel.getFlightBookingLuggageMetaViewModels()) {
                        ArrayList<String> selectedLuggages = new ArrayList<>();
                        for (FlightBookingLuggageViewModel flightBookingLuggageViewModel : flightBookingLuggageRouteViewModel.getLuggages()) {
                            selectedLuggages.add(flightBookingLuggageViewModel.getWeightFmt() + " - " + flightBookingLuggageViewModel.getPriceFmt());
                        }
                        simpleViewModels.add(new SimpleViewModel(
                                itemView.getContext().getString(R.string.flight_booking_list_passenger_luggage_label) + flightBookingLuggageRouteViewModel.getDescription(), TextUtils.join(" + ", selectedLuggages)
                        ));
                    }
                }

                if (viewModel.getFlightBookingMealMetaViewModels() != null && viewModel.getFlightBookingMealMetaViewModels().size() > 0) {
                    for (FlightBookingMealMetaViewModel flightBookingMealRouteViewModel : viewModel.getFlightBookingMealMetaViewModels()) {
                        simpleViewModels.add(new SimpleViewModel(
                                itemView.getContext().getString(R.string.flight_booking_list_passenger_meals_label), TextUtils.join(" + ", flightBookingMealRouteViewModel.getMealViewModels())
                        ));
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
                headerLabel.setContent(itemView.getContext().getString(R.string.flight_booking_passenger_fill_data_label));
            }
        }
    }
}
