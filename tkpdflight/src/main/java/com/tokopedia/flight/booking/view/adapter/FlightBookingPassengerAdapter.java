package com.tokopedia.flight.booking.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.List;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingPassengerAdapter extends BaseAdapter {
    private List<Visitable> viewModels;
    private FlightBookingPassengerTypeFactory typeFactory;
    private Activity activity;

    public FlightBookingPassengerAdapter(FlightBookingPassengerAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
        typeFactory = adapterTypeFactory;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(visitables.get(position));
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return visitables.get(position).type(typeFactory);
    }


    /*public FlightBookingPassengerAdapter(FlightBookingPassengerTypeFactory typeFactory, Activity activity) {
        this.typeFactory = typeFactory;
        this.activity = activity;
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
            if (viewModel.getPassengerFirstName() != null) {
                passengerDetailLayout.setVisibility(View.VISIBLE);
                headerLabel.setContent(itemView.getContext().getString(R.string.flight_booking_passenger_change_label));
                String passengerName = viewModel.getPassengerFirstName() + " " + viewModel.getPassengerLastName();
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
                    for (FlightBookingAmenityMetaViewModel flightBookingLuggageRouteViewModel : viewModel.getFlightBookingLuggageMetaViewModels()) {
                        ArrayList<String> selectedLuggages = new ArrayList<>();
                        for (FlightBookingAmenityViewModel flightBookingLuggageViewModel : flightBookingLuggageRouteViewModel.getAmenities()) {
                            selectedLuggages.add(flightBookingLuggageViewModel.getTitle() + " - " + flightBookingLuggageViewModel.getPrice());
                        }
                        simpleViewModels.add(new SimpleViewModel(
                                itemView.getContext().getString(R.string.flight_booking_list_passenger_luggage_label) + flightBookingLuggageRouteViewModel.getDescription(), TextUtils.join(" + ", selectedLuggages)
                        ));
                    }
                }

                if (viewModel.getFlightBookingMealMetaViewModels() != null && viewModel.getFlightBookingMealMetaViewModels().size() > 0) {
                    for (FlightBookingAmenityMetaViewModel flightBookingMealRouteViewModel : viewModel.getFlightBookingMealMetaViewModels()) {
                        simpleViewModels.add(new SimpleViewModel(
                                itemView.getContext().getString(R.string.flight_booking_list_passenger_meals_label), TextUtils.join(" + ", flightBookingMealRouteViewModel.getAmenities())
                        ));
                    }
                }

                FlightSimpleAdapter adapter = new FlightSimpleAdapter();
                adapter.setTitleBold(true);
                adapter.setDescriptionTextColor(activity.getResources().getColor(R.color.font_black_secondary_54));
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
    }*/
}
