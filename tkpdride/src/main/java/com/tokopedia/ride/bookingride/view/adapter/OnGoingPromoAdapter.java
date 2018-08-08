package com.tokopedia.ride.bookingride.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.bookingride.domain.model.Promo;

import java.util.Collections;
import java.util.List;

/**
 * Created by alvarisi on 5/5/17.
 */

public class OnGoingPromoAdapter extends RecyclerView.Adapter<OnGoingPromoAdapter.ViewHolder> {
    private List<Promo> promos;
    private final LayoutInflater layoutInflater;
    private OnAdapterInteractionListener interactionListener;

    public OnGoingPromoAdapter(Activity activity) {
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.promos = Collections.emptyList();
    }

    public interface OnAdapterInteractionListener {
        void onItemClicked(String promoCode);

        void onLinkBtnClicked(String url);
    }

    public void setInteractionListener(OnAdapterInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.row_promo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.titleTextView.setText(promos.get(position).getOffer());
        holder.promoTextView.setText(promos.get(position).getCode().toUpperCase());
        holder.promoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interactionListener != null) {
                    interactionListener.onItemClicked(promos.get(holder.getAdapterPosition()).getCode().toUpperCase());
                }
            }
        });
        holder.linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RideGATracking.eventClickReadOfferDetails(((BaseActivity)(holder.linkTextView.getContext())).getScreenName(),promos.get(holder.getAdapterPosition()).getCode().toUpperCase()); //21
                if (interactionListener != null) {
                    interactionListener.onLinkBtnClicked(promos.get(holder.getAdapterPosition()).getUrl());
                }
            }
        });

        holder.containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interactionListener != null) {
                    interactionListener.onItemClicked(promos.get(holder.getAdapterPosition()).getCode());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return promos.size();
    }

    public void setPromos(List<Promo> promos) {
        this.promos.clear();
        this.promos = promos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView promoTextView;
        TextView linkTextView;
        RelativeLayout containerLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            promoTextView = (TextView) itemView.findViewById(R.id.tv_promo_code);
            linkTextView = (TextView) itemView.findViewById(R.id.tv_link);
            containerLayout = (RelativeLayout) itemView.findViewById(R.id.container);
        }
    }
}
