package com.tokopedia.flight.search.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.binder.BaseEmptyDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.BaseRetryDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.DataBindAdapter;
import com.tokopedia.abstraction.base.view.adapter.binder.EmptyDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.NoResultDataBinder;
import com.tokopedia.abstraction.base.view.adapter.binder.RetryDataBinder;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.adapter.viewholder.FlightSearchViewHolder;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchAdapter extends BaseListAdapter<FlightSearchViewModel> {

    private OnBaseFlightSearchAdapterListener onBaseFlightSearchAdapterListener;
    private String errorMessage;

    public FlightSearchAdapter(Context context, OnBaseListV2AdapterListener<FlightSearchViewModel> onBaseListV2AdapterListener,
                               OnBaseFlightSearchAdapterListener onBaseFlightSearchAdapterListener) {
        super(context, onBaseListV2AdapterListener);
        this.onBaseFlightSearchAdapterListener = onBaseFlightSearchAdapterListener;
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
                onBaseFlightSearchAdapterListener.onChangeDateClicked();
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

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_search);
        return new FlightSearchViewHolder(view, onBaseFlightSearchAdapterListener);
    }

    public interface OnBaseFlightSearchAdapterListener {
        void onResetFilterClicked();

        void onChangeDateClicked();

        void onDetailClicked(FlightSearchViewModel flightSearchViewModel);
    }

    private class FlightBaseRetryDataBinder extends BaseRetryDataBinder {

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

}
