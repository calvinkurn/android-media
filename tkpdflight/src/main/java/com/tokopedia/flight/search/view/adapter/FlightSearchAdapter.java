package com.tokopedia.flight.search.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.binder.BaseEmptyDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.BaseRetryDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.DataBindAdapter;
import com.tokopedia.abstraction.base.view.adapter.binder.EmptyDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.NoResultDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.RetryDataBinder;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.abstraction.utils.MethodChecker;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.util.FlightAirlineIconUtil;
import com.tokopedia.flight.search.util.DurationUtil;
import com.tokopedia.flight.search.view.model.Duration;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchAdapter extends BaseListAdapter<FlightSearchViewModel> {

    private OnBaseFlightSearchAdapterListener onBaseFlightSearchAdapterListener;
    private String errorMessage;

    public interface OnBaseFlightSearchAdapterListener{
        void onResetFilterClicked();
        void onDetailClicked(FlightSearchViewModel flightSearchViewModel);
    }

    public FlightSearchAdapter(Context context, OnBaseListV2AdapterListener<FlightSearchViewModel> onBaseListV2AdapterListener,
                               OnBaseFlightSearchAdapterListener onBaseFlightSearchAdapterListener) {
        super(context, onBaseListV2AdapterListener);
        this.onBaseFlightSearchAdapterListener = onBaseFlightSearchAdapterListener;
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
            tvPrice.setText(flightSearchViewModel.getFare().getAdult());
            setDuration(flightSearchViewModel);
            setAirline(flightSearchViewModel);
            View.OnClickListener detailClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBaseFlightSearchAdapterListener.onDetailClicked(flightSearchViewModel);
                }
            };
            tvPrice.setOnClickListener(detailClickListener);
            containerDetail.setOnClickListener(detailClickListener);

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
            Duration duration = DurationUtil.convertFormMinute(flightSearchViewModel.getDurationMinute());
            String durationString = DurationUtil.getReadableString(itemView.getContext(),duration);
            if(flightSearchViewModel.getTotalTransit() > 0){
                tvDuration.setText(itemView.getContext().getString(R.string.flight_label_duration_transit,
                        durationString, String.valueOf(flightSearchViewModel.getTotalTransit())));
            }else {
                tvDuration.setText(itemView.getContext().getString(R.string.flight_label_duration_direct,
                        durationString));
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

    @Nullable
    @Override
    protected NoResultDataBinder createEmptyViewBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(this, R.drawable.ic_flight_empty_state);
        emptyDataBinder.setEmptyContentText(context.getString(R.string.flight_there_is_no_flight_available));
        emptyDataBinder.setEmptyButtonItemText(context.getString(R.string.change_date));
        emptyDataBinder.setCallback(new BaseEmptyDataBinder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {

            }

            @Override
            public void onEmptyButtonClicked() {
                Toast.makeText(context, "Belum dihandle", Toast.LENGTH_LONG).show();
            }
        });
        return emptyDataBinder;
    }

    @Nullable
    @Override
    protected NoResultDataBinder createEmptyViewSearchBinder() {
        EmptyDataBinder emptyDataBinder = new EmptyDataBinder(this, R.drawable.ic_flight_empty_state);
        emptyDataBinder.setEmptyContentText(context.getString(R.string.flight_there_is_zero_flight_for_the_filter));
        emptyDataBinder.setEmptyButtonItemText(context.getString(R.string.reset_filter));
        emptyDataBinder.setCallback(new BaseEmptyDataBinder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {

            }

            @Override
            public void onEmptyButtonClicked() {
                onBaseFlightSearchAdapterListener.onResetFilterClicked();
            }
        });
        return emptyDataBinder;
    }

    @Nullable
    @Override
    protected RetryDataBinder createRetryDataBinder() {
        return new FlightBaseRetryDataBinder(this);
    }

    private class FlightBaseRetryDataBinder extends BaseRetryDataBinder{

        public FlightBaseRetryDataBinder(DataBindAdapter dataBindAdapter) {
            super(dataBindAdapter, R.drawable.ic_flight_empty_state);
        }

        @Override
        public void bindViewHolder(RetryDataBinder.ViewHolder holder, int position) {
            super.bindViewHolder(holder, position);
            if (holder instanceof ViewHolder) {
                TextView tvRetryDescription = ((ViewHolder) holder).getRetryDescription();
                if (TextUtils.isEmpty(errorMessage)) {
                    tvRetryDescription.setVisibility(View.GONE);
                } else {
                    tvRetryDescription.setText(errorMessage);
                    tvRetryDescription.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_search);
        return new FlightSearchViewHolder(view);
    }

}
