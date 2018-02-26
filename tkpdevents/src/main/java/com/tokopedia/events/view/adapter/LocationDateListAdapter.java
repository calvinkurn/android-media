package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.viewmodel.LocationDateModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 16/01/18.
 */

public class LocationDateListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LocationDateModel> dataSet;
    private Context mContext;
    private EventBookTicketPresenter mPresenter;

    public LocationDateListAdapter(List<LocationDateModel> data, Context context, EventBookTicketPresenter presenter) {
        dataSet = data;
        this.mContext = context;
        this.mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.locationdate_item_layout, parent, false);
        return new LocationDateHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((LocationDateHolder) holder).setLocationDate(position, dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDataSet(List<LocationDateModel> data) {
        dataSet = data;
    }

    public class LocationDateHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_location_bts)
        TextView tvLocation;
        @BindView(R2.id.tv_day_time)
        TextView tvDayTime;
        @BindView(R2.id.location_date_item)
        View locationDateItem;

        LocationDateModel valueItem;
        int mPosition;

        private LocationDateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setLocationDate(int position, LocationDateModel value) {
            this.valueItem = value;
            this.mPosition = position;
            tvLocation.setText(valueItem.getmLocation());
            tvDayTime.setText(valueItem.getDate());
        }

        @OnClick(R2.id.location_date_item)
        void onClickFilterItem() {
            mPresenter.onClickLocationDate(valueItem, mPosition);
            notifyItemChanged(mPosition);
        }
    }
}
