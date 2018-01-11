package com.tokopedia.flight.review.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPassengerViewHolder extends AbstractViewHolder<FlightDetailPassenger> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_review_passenger;
    private ReviewPassengerDetailAdapter reviewPassengerDetailAdapter;
    private TextView passengerNumber;
    private TextView passengerName;
    private TextView passengerCategory;
    private RecyclerView recyclerViewPassengerDetail;

    @Override
    public void bind(FlightDetailPassenger flightDetailPassenger) {
        passengerNumber.setText(String.format("%d.", getAdapterPosition() + 1));
        passengerName.setText(flightDetailPassenger.getPassengerName());
        passengerCategory.setText(getPassengerType(flightDetailPassenger.getPassengerType()));
        reviewPassengerDetailAdapter.addData(flightDetailPassenger.getInfoPassengerList());
    }

    private String getPassengerType(int flightDetailPassenger) {
        switch (flightDetailPassenger) {
            case FlightBookingPassenger.ADULT:
                return getString(R.string.flight_label_adult_review);
            case FlightBookingPassenger.CHILDREN:
                return getString(R.string.flight_label_child_review);
            case FlightBookingPassenger.INFANT:
                return getString(R.string.flight_label_infant_review);
            default:
                return "";
        }
    }

    public FlightBookingReviewPassengerViewHolder(View layoutView) {
        super(layoutView);
        passengerNumber = (TextView) layoutView.findViewById(R.id.passenger_number);
        passengerName = (TextView) layoutView.findViewById(R.id.passenger_name);
        passengerCategory = (TextView) layoutView.findViewById(R.id.passenger_category);
        recyclerViewPassengerDetail = (RecyclerView) layoutView.findViewById(R.id.recycler_view_passenger_detail);

        recyclerViewPassengerDetail.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        reviewPassengerDetailAdapter = new ReviewPassengerDetailAdapter();
        recyclerViewPassengerDetail.setAdapter(reviewPassengerDetailAdapter);
    }

    private class ReviewPassengerDetailAdapter extends RecyclerView.Adapter<PassengerDetailViewHolder> {
        List<SimpleViewModel> infoList;

        public ReviewPassengerDetailAdapter() {
            infoList = new ArrayList<>();
        }

        @Override
        public PassengerDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_detail_passenger_info, parent, false);
            return new PassengerDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PassengerDetailViewHolder holder, int position) {
            holder.bindData(infoList.get(position));
        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }

        public void addData(List<SimpleViewModel> infos) {
            infoList.clear();
            infoList.addAll(infos);
            notifyDataSetChanged();
        }
    }

    private class PassengerDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView titleInfo;
        private TextView descInfo;

        public PassengerDetailViewHolder(View itemView) {
            super(itemView);
            titleInfo = (TextView) itemView.findViewById(R.id.title_info);
            descInfo = (TextView) itemView.findViewById(R.id.desc_info);
        }

        public void bindData(SimpleViewModel info) {
            titleInfo.setText(info.getDescription());
            descInfo.setText(info.getLabel().trim());
        }
    }
}
