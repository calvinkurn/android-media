package com.tokopedia.flight.search.view.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.abstraction.utils.MethodChecker;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.util.FlightAirlineIconUtil;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;

import java.util.List;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchAdapter extends BaseListV2Adapter<FlightSearchViewModel> {

    public interface ListenerOnDetailClicked{
        void onDetailClicked(FlightSearchViewModel flightSearchViewModel);
    }

    ListenerOnDetailClicked listenerOnDetailClicked;

    public void setListenerOnDetailClicked(ListenerOnDetailClicked listenerOnDetailClicked) {
        this.listenerOnDetailClicked = listenerOnDetailClicked;
    }

    public FlightSearchAdapter(OnBaseListV2AdapterListener<FlightSearchViewModel> onBaseListV2AdapterListener) {
        super(onBaseListV2AdapterListener);
    }

    public FlightSearchAdapter(@Nullable List<FlightSearchViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightSearchViewModel> onBaseListV2AdapterListener) {
        super(data, rowPerPage, onBaseListV2AdapterListener);
    }

    public class FlightSearchViewHolder extends BaseViewHolder<FlightSearchViewModel> {

        TextView tvDeparture;
        TextView tvArrival;
        TextView tvAirline;
        ImageView logoAirline;
        TextView tvPrice;
        TextView tvDuration;
        TextView airlineRefundableInfo;
        TextView savingPrice;
        TextView arrivalAddDay;
        View containerDetail;

        public FlightSearchViewHolder(View itemView) {
            super(itemView);
            tvDeparture = (TextView) itemView.findViewById(R.id.departure_time);
            tvArrival = (TextView) itemView.findViewById(R.id.arrival_time);
            tvAirline = (TextView) itemView.findViewById(R.id.tv_airline);
            logoAirline = (ImageView) itemView.findViewById(R.id.image_airline);
            airlineRefundableInfo = (TextView) itemView.findViewById(R.id.airline_refundable_info);
            tvPrice = (TextView) itemView.findViewById(R.id.total_price);
            tvDuration = (TextView) itemView.findViewById(R.id.flight_time);
            savingPrice = (TextView) itemView.findViewById(R.id.saving_price);
            arrivalAddDay = (TextView) itemView.findViewById(R.id.arrival_add_day);
            containerDetail = itemView.findViewById(R.id.container_detail);
        }

        @Override
        public void bindObject(final FlightSearchViewModel flightSearchViewModel) {
            tvDeparture.setText(String.format("%s %s", flightSearchViewModel.getDepartureTime(), flightSearchViewModel.getDepartureAirport()));
            tvArrival.setText(String.format("%s %s", flightSearchViewModel.getArrivalTime(), flightSearchViewModel.getArrivalAirport()));
            tvPrice.setText(flightSearchViewModel.getTotal());
            setDuration(flightSearchViewModel);
            setAirline(flightSearchViewModel);
            containerDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerOnDetailClicked != null){
                        listenerOnDetailClicked.onDetailClicked(flightSearchViewModel);
                    }
                }
            });
            setRefundableInfo(flightSearchViewModel);
            setSavingPrice(flightSearchViewModel);
            setArrivalAddDay(flightSearchViewModel);
        }

        private void setArrivalAddDay(FlightSearchViewModel flightSearchViewModel) {
            if(flightSearchViewModel.getAddDayArrival() > 0) {
                arrivalAddDay.setVisibility(View.VISIBLE);
                arrivalAddDay.setText(itemView.getContext().getString(R.string.flight_label_duration_add_day, flightSearchViewModel.getAddDayArrival()));
            }else{
                arrivalAddDay.setVisibility(View.GONE);
            }
        }

        void setDuration(FlightSearchViewModel flightSearchViewModel) {
            if(flightSearchViewModel.getTotalTransit() > 0){
                tvDuration.setText(itemView.getContext().getString(R.string.flight_label_duration_transit,
                        flightSearchViewModel.getDuration(), String.valueOf(flightSearchViewModel.getTotalTransit())));
            }else {
                tvDuration.setText(flightSearchViewModel.getDuration());
            }
        }

        private void setSavingPrice(FlightSearchViewModel flightSearchViewModel) {
            if(TextUtils.isEmpty(flightSearchViewModel.getBeforeTotal())){
                savingPrice.setVisibility(View.GONE);
            }else{
                savingPrice.setVisibility(View.VISIBLE);
                savingPrice.setText(MethodChecker.fromHtml(getString(R.string.flight_label_saving_price_html, flightSearchViewModel.getBeforeTotal())));
            }
        }

        private void setAirline(FlightSearchViewModel flightSearchViewModel) {
            if(flightSearchViewModel.getAirlineList().size() > 1) {
                logoAirline.setImageResource(R.drawable.ic_plane_flight);
                tvAirline.setText(R.string.flight_label_multi_maskapai);
            }else if(flightSearchViewModel.getAirlineList().size() == 1){
                logoAirline.setImageResource(FlightAirlineIconUtil.getImageResource(flightSearchViewModel.getAirlineList().get(0).getId()));
                tvAirline.setText(flightSearchViewModel.getAirlineList().get(0).getName());
            }
        }

        private void setRefundableInfo(FlightSearchViewModel flightSearchViewModel) {
            if(flightSearchViewModel.isRefundable() == RefundableEnum.NOT_REFUNDABLE){
                airlineRefundableInfo.setVisibility(View.GONE);
            }else{
                airlineRefundableInfo.setVisibility(View.VISIBLE);
                airlineRefundableInfo.setText(flightSearchViewModel.isRefundable().getValueRes());
            }
        }
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_search);
        return new FlightSearchViewHolder(view);
    }
}
