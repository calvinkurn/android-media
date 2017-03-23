package com.tokopedia.ride.bookingride.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.SeatViewModel;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alvarisi on 3/23/17.
 */

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private List<SeatViewModel> seatViewModels;

    private OnItemClickListener onItemClickListener;

    public void setSeatViewModels(List<SeatViewModel> seatViewModels) {
        this.seatViewModels = seatViewModels;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(SeatViewModel seatViewModel);
    }

    public SeatAdapter(Context context) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        seatViewModels = Collections.emptyList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.mLayoutInflater.inflate(R.layout.seat_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.titleTextView.setText(String.valueOf(seatViewModels.get(position).getSeat()));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(seatViewModels.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return seatViewModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //        @BindView(R2.id.tv_title)
        TextView titleTextView;
        //        @BindView(R2.id.item_container)
        LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            container = (LinearLayout) itemView.findViewById(R.id.item_container);
        }
    }
}
