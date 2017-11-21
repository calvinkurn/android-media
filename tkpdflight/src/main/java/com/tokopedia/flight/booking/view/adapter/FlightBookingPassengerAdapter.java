package com.tokopedia.flight.booking.view.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
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
        private AppCompatTextView tvPassengerBirthdate;
        private AppCompatTextView tvLuggageDepartureLabel;
        private AppCompatTextView tvLuggageDeparture;
        private AppCompatTextView tvLuggageReturnLabel;
        private AppCompatTextView tvLuggageReturn;
        private AppCompatTextView tvMealDepartureLabel;
        private AppCompatTextView tvMealDeparture;
        private AppCompatTextView tvMealReturnLabel;
        private AppCompatTextView tvMealReturn;

        private LinearLayout luggageDepartureLayout;
        private LinearLayout luggageReturnLayout;
        private LinearLayout mealDepartureLayout;
        private LinearLayout mealReturnLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }


        private void findViews(View view) {
            tvHeaderTitle = (AppCompatTextView) view.findViewById(R.id.tv_header_title);
            tvChangePassengerData = (AppCompatTextView) view.findViewById(R.id.tv_change_passenger_data);
            passengerDetailLayout = (LinearLayout) view.findViewById(R.id.passenger_detail_layout);
            tvPassengerName = (AppCompatTextView) view.findViewById(R.id.tv_passenger_name);
            tvPassengerBirthdate = (AppCompatTextView) view.findViewById(R.id.tv_passenger_birthdate);
            tvLuggageDepartureLabel = (AppCompatTextView) view.findViewById(R.id.tv_luggage_departure_label);
            tvLuggageDeparture = (AppCompatTextView) view.findViewById(R.id.tv_luggage_departure);
            tvLuggageReturnLabel = (AppCompatTextView) view.findViewById(R.id.tv_luggage_return_label);
            tvLuggageReturn = (AppCompatTextView) view.findViewById(R.id.tv_luggage_return);
            tvMealDepartureLabel = (AppCompatTextView) view.findViewById(R.id.tv_meal_departure_label);
            tvMealDeparture = (AppCompatTextView) view.findViewById(R.id.tv_meal_departure);
            tvMealReturnLabel = (AppCompatTextView) view.findViewById(R.id.tv_meal_return_label);
            tvMealReturn = (AppCompatTextView) view.findViewById(R.id.tv_meal_return);
            luggageDepartureLayout = (LinearLayout) view.findViewById(R.id.luggage_departure_layout);
            luggageReturnLayout = (LinearLayout) view.findViewById(R.id.luggage_return_layout);
            mealDepartureLayout = (LinearLayout) view.findViewById(R.id.meal_departure_layout);
            mealReturnLayout = (LinearLayout) view.findViewById(R.id.meal_return_layout);
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
                tvChangePassengerData.setText("Ubah");
                tvPassengerName.setText(String.valueOf(viewModel.getPassengerName()));
                tvPassengerBirthdate.setText(String.valueOf(FlightDateUtil.formatDate(
                        FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, viewModel.getPassengerBirthdate()
                )));

                if (viewModel.getDepartureLugage() != null) {
                    luggageDepartureLayout.setVisibility(View.VISIBLE);
                    tvLuggageDepartureLabel.setText("Luggage departure");
                    tvLuggageDeparture.setText(viewModel.getDepartureLugage().getWeightFmt());
                } else {
                    luggageDepartureLayout.setVisibility(View.GONE);
                }

                if (viewModel.getDepartureMeals() != null && viewModel.getDepartureMeals().size() > 0) {
                    mealDepartureLayout.setVisibility(View.VISIBLE);
                    tvMealDepartureLabel.setText("Meal Departure");
                    tvMealDeparture.setText(TextUtils.join(",", viewModel.getDepartureMeals()));
                } else {
                    mealDepartureLayout.setVisibility(View.GONE);
                }

                if (viewModel.isSingleRoute()) {
                    luggageReturnLayout.setVisibility(View.GONE);
                    mealReturnLayout.setVisibility(View.GONE);
                } else {
                    if (viewModel.getReturnLugage() != null) {
                        luggageReturnLayout.setVisibility(View.VISIBLE);
                        tvLuggageReturnLabel.setText("Luggage Return");
                        tvLuggageReturn.setText(viewModel.getReturnLugage().getWeightFmt());
                    } else {
                        luggageReturnLayout.setVisibility(View.GONE);
                    }

                    if (viewModel.getReturnMeals() != null && viewModel.getReturnMeals().size() > 0) {
                        mealReturnLayout.setVisibility(View.VISIBLE);
                        tvMealReturnLabel.setText("Meal Departure");
                        tvMealReturn.setText(TextUtils.join(",", viewModel.getReturnMeals()));
                    } else {
                        mealReturnLayout.setVisibility(View.GONE);
                    }
                }

            } else {
                passengerDetailLayout.setVisibility(View.GONE);
                tvChangePassengerData.setText("Isi Data");
            }
        }
    }
}
