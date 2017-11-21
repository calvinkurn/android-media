package com.tokopedia.flight.detail.view.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.util.FlightAirlineIconUtil;
import com.tokopedia.flight.search.data.cloud.model.response.Amenity;
import com.tokopedia.flight.search.data.cloud.model.response.Info;
import com.tokopedia.flight.search.data.cloud.model.response.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityViewHolder extends BaseViewHolder<Route> {

    public static final int NUMBER_OF_COLUMN_AMENITY = 3;
    private final ListInfoAdapter adapterInfo;
    private final RecyclerView listInfo;
    private final RecyclerView gridAmenity;
    private final AmenityAdapter adapterAmenity;
    private final ImageView imageAirline;
    private final TextView airlineName;
    private final TextView airlineCode;
    private TextView refundableInfo;

    public FlightDetailFacilityViewHolder(View itemView) {
        super(itemView);
        listInfo = (RecyclerView) itemView.findViewById(R.id.recycler_view_info);
        gridAmenity = (RecyclerView) itemView.findViewById(R.id.recycler_view_amenity);
        imageAirline = (ImageView) itemView.findViewById(R.id.airline_icon);
        refundableInfo = (TextView) itemView.findViewById(R.id.airline_refundable_info);
        airlineName = (TextView) itemView.findViewById(R.id.airline_name);
        airlineCode = (TextView) itemView.findViewById(R.id.airline_code);

        listInfo.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        adapterInfo = new ListInfoAdapter();
        listInfo.setAdapter(adapterInfo);
        gridAmenity.setLayoutManager(new GridLayoutManager(itemView.getContext(), NUMBER_OF_COLUMN_AMENITY));
        gridAmenity.addItemDecoration(new ItemGridDecorationDivider(itemView.getContext(), ItemGridDecorationDivider.GRID, NUMBER_OF_COLUMN_AMENITY));
        adapterAmenity = new AmenityAdapter();
        gridAmenity.setAdapter(adapterAmenity);
    }

    @Override
    public void bindObject(Route route) {
        adapterInfo.addData(route.getInfos());
        setDefaultAmenities(route);
        airlineName.setText(route.getAirlineName());
        airlineCode.setText(String.format("%s - %s", route.getAirline(), route.getFlightNumber()));
        imageAirline.setImageResource(FlightAirlineIconUtil.getImageResource(route.getAirline()));
        setRefundableInfo(route);
    }

    private void setRefundableInfo(Route route) {
        if (route.getRefundable()) {
            refundableInfo.setText(R.string.flight_label_refundable_info);
            refundableInfo.setVisibility(View.VISIBLE);
        } else {
            refundableInfo.setText(R.string.flight_label_non_refundable_info);
            refundableInfo.setVisibility(View.GONE);
        }
    }

    public void setDefaultAmenities(Route defaultAmenities) {
        while (defaultAmenities.getAmenities().size() % 3
                != 0) {
            Amenity amenity = new Amenity();
            amenity.setDefault(true);
            defaultAmenities.getAmenities().add(amenity);
        }
        adapterAmenity.addData(defaultAmenities.getAmenities());
    }

    private class ListInfoAdapter extends RecyclerView.Adapter<FlightDetailFacilityInfoViewHolder> {
        List<Info> infoList;

        public ListInfoAdapter() {
            infoList = new ArrayList<>();
        }

        @Override
        public FlightDetailFacilityInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_detail_facility_info, parent, false);
            return new FlightDetailFacilityInfoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FlightDetailFacilityInfoViewHolder holder, int position) {
            holder.bindData(infoList.get(position));
        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }

        public void addData(List<Info> infos) {
            infoList.clear();
            infoList.addAll(infos);
            notifyDataSetChanged();
        }
    }

    private class AmenityAdapter extends RecyclerView.Adapter<FlightDetailFacilityAmenityViewHolder> {

        List<Amenity> amenityList;

        public AmenityAdapter() {
            amenityList = new ArrayList<>();
        }

        @Override
        public FlightDetailFacilityAmenityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_detail_facility_amenity, parent, false);
            return new FlightDetailFacilityAmenityViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FlightDetailFacilityAmenityViewHolder holder, int position) {
            holder.bindData(amenityList.get(position));
        }

        @Override
        public int getItemCount() {
            return amenityList.size();
        }

        public void addData(List<Amenity> amenities) {
            amenityList.clear();
            amenityList.addAll(amenities);
            notifyDataSetChanged();
        }
    }
}
