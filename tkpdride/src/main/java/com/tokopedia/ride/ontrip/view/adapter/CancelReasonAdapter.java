package com.tokopedia.ride.ontrip.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.ride.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by alvarisi on 6/14/17.
 */

public class CancelReasonAdapter extends RecyclerView.Adapter<CancelReasonAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private List<String> reasons;
    private String selectedReasons;
    private Context context;

    private CancelReasonAdapter.OnItemClickListener onItemClickListener;

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(CancelReasonAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setSelectedReason(String reason) {
        this.selectedReasons = reason;
    }

    public interface OnItemClickListener {
        void onItemClicked(String reason);
    }

    public CancelReasonAdapter(Context context) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.reasons = Collections.emptyList();
        this.context = context;
    }

    @Override
    public CancelReasonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.mLayoutInflater.inflate(R.layout.view_cancel_reason_row, parent, false);
        return new CancelReasonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (!TextUtils.isEmpty(selectedReasons) && selectedReasons.equalsIgnoreCase(reasons.get(holder.getAdapterPosition()))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.titleTextView.setTextColor(context.getColor(R.color.tkpd_main_green));
            } else {
                holder.titleTextView.setTextColor(context.getResources().getColor(R.color.tkpd_main_green));
            }
            holder.titleTextView.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.titleTextView.setTextColor(context.getColor(R.color.label_text_color));
            } else {
                holder.titleTextView.setTextColor(context.getResources().getColor(R.color.label_text_color));
            }
            holder.titleTextView.setTypeface(Typeface.DEFAULT);
        }
        holder.titleTextView.setText(String.valueOf(reasons.get(holder.getAdapterPosition())));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getAdapterPosition() < getItemCount()) {
                    onItemClickListener.onItemClicked(reasons.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reasons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            container = (LinearLayout) itemView.findViewById(R.id.item_container);
        }
    }
}