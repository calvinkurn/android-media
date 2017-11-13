package com.tokopedia.flight.review;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPassengerViewHolder extends BaseViewHolder {

    private ReviewPassengerDetailAdapter reviewPassengerDetailAdapter;
    private TextView passengerNumber;
    private TextView passengerName;
    private TextView passengerCategory;
    private RecyclerView recyclerViewPassengerDetail;

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

    @Override
    public void bindObject(Object o) {
        passengerNumber.setText(String.format("%d.", getAdapterPosition() + 1));
    }

    private class ReviewPassengerDetailAdapter extends RecyclerView.Adapter<PassengerDetailViewHolder> {
        List<Object> infoList;

        public ReviewPassengerDetailAdapter() {
            infoList = new ArrayList<>();
        }

        @Override
        public PassengerDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_detail_facility_info, parent, false);
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

        public void addData(List<Object> infos) {
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

        public void bindData(Object info) {
        }
    }
}
