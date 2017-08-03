package com.tokopedia.ride.completetrip.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.completetrip.view.viewmodel.TokoCashProduct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alvarisi on 7/10/17.
 */

public class TokoCashProductAdapter extends RecyclerView.Adapter<TokoCashProductAdapter.ViewHolder> {

    private Activity activity;

    private List<TokoCashProduct> productList;
    private OnActionProductAdapterListener interactionListener;

    public void setInteractionListener(OnActionProductAdapterListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public interface OnActionProductAdapterListener {
        void onItemSelected(TokoCashProduct product);
    }

    public TokoCashProductAdapter(Activity activity) {
        this.activity = activity;
        this.productList = new ArrayList<>();
    }

    public TokoCashProductAdapter(Activity activity,
                                  List<TokoCashProduct> operatorList) {
        this.activity = activity;
        this.productList = operatorList != null ? operatorList : new ArrayList<TokoCashProduct>();
    }

    @Override
    public TokoCashProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.view_tokocash_product_row, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.titleTextView.setText(productList.get(holder.getAdapterPosition()).getTitle());
        if (holder.getAdapterPosition() % 2 == 0) {
            holder.container.setBackgroundColor(activity.getResources().getColor(R.color.grey_200));
        } else {
            holder.container.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interactionListener != null)
                    interactionListener.onItemSelected(productList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_title)
        TextView titleTextView;
        @BindView(R2.id.container)
        RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setProducts(List<TokoCashProduct> operatorQuery) {
        this.productList = operatorQuery;
    }
}