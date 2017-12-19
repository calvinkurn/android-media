package com.tokopedia.flight.detail.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.utils.image.ImageHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteInfoViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.search.data.cloud.model.response.Amenity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityViewHolder extends AbstractViewHolder<FlightDetailRouteViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_detail_facility;

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
    public void bind(FlightDetailRouteViewModel route) {
        adapterInfo.addData(route.getInfos());
        setDefaultAmenities(route);
        airlineName.setText(route.getAirlineName());
        airlineCode.setText(String.format("%s - %s", route.getAirlineCode(), route.getFlightNumber()));
        ImageHandler.loadImageWithoutPlaceholder(imageAirline, route.getAirlineLogo(), R.drawable.ic_airline_default);
        setRefundableInfo(route);
    }

    private void setRefundableInfo(FlightDetailRouteViewModel route) {
        if (route.isRefundable()) {
            refundableInfo.setText(R.string.flight_label_refundable_info);
            refundableInfo.setVisibility(View.VISIBLE);
        } else {
            refundableInfo.setText(R.string.flight_label_non_refundable_info);
            refundableInfo.setVisibility(View.GONE);
        }
    }

    public void setDefaultAmenities(FlightDetailRouteViewModel flightDetailRouteViewModel) {
        if (flightDetailRouteViewModel.getAmenities() != null && flightDetailRouteViewModel.getAmenities().size() > 0) {
            while (flightDetailRouteViewModel.getAmenities().size() % 3
                    != 0) {
                Amenity amenity = new Amenity();
                amenity.setDefault(true);
                flightDetailRouteViewModel.getAmenities().add(amenity);
            }
            adapterAmenity.addData(flightDetailRouteViewModel.getAmenities());
        }
    }

    private class ListInfoAdapter extends RecyclerView.Adapter<FlightDetailFacilityInfoViewHolder> {
        List<FlightDetailRouteInfoViewModel> infoList;

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

        public void addData(List<FlightDetailRouteInfoViewModel> infos) {
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
