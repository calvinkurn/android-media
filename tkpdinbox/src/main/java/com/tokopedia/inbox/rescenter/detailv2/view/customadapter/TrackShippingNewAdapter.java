package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.TrackingHistoryDialogViewModel;

import java.util.List;

/**
 * Created by milhamj on 25/11/17.
 */

public class TrackShippingNewAdapter extends RecyclerView.Adapter<TrackShippingNewAdapter.TrackShippingHolder> {
    private Context context;
    private List<TrackingHistoryDialogViewModel> historyList;

    private TrackShippingNewAdapter(Context context, List<TrackingHistoryDialogViewModel> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    public static TrackShippingNewAdapter newInstance(Context context, List<TrackingHistoryDialogViewModel> historyList){
        TrackShippingNewAdapter adapter = new TrackShippingNewAdapter(context, historyList);
        adapter.addTrackingHistoryTitle();
        return adapter;
    }

    private void addTrackingHistoryTitle(){
        TrackingHistoryDialogViewModel trackingHistoryTitle = new TrackingHistoryDialogViewModel();
        trackingHistoryTitle.setStatus("Status");
        trackingHistoryTitle.setCity("Kota");
        trackingHistoryTitle.setDate("Waktu");
        historyList.add(0, trackingHistoryTitle);
        notifyItemInserted(0);
    }

    @Override
    public TrackShippingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_detail_track_shipping, viewGroup, false);
        return new TrackShippingHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackShippingHolder holder, int i) {
        if(i==0) {
            holder.status.setBackground(
                    MethodChecker.getDrawable(context, R.drawable.bg_track_shipping_dark));
            holder.status.setTypeface(Typeface.DEFAULT_BOLD);

            holder.city.setBackground(
                    MethodChecker.getDrawable(context, R.drawable.bg_track_shipping_dark));
            holder.city.setTypeface(Typeface.DEFAULT_BOLD);

            holder.time.setBackground(
                    MethodChecker.getDrawable(context, R.drawable.bg_track_shipping_dark));
            holder.time.setTypeface(Typeface.DEFAULT_BOLD);
        }
        TrackingHistoryDialogViewModel item = historyList.get(i);
        holder.status.setText(item.getStatus());
        holder.city.setText(item.getCity());
        holder.time.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class TrackShippingHolder extends RecyclerView.ViewHolder {
        TextView status;
        TextView city;
        TextView time;

        public TrackShippingHolder(View itemView) {
            super(itemView);
            this.status = (TextView) itemView.findViewById(R.id.tv_status);
            this.city = (TextView) itemView.findViewById(R.id.tv_city);
            this.time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
