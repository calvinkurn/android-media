package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.transaction.R;

import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class CourierChoiceAdapter extends RecyclerView.Adapter<CourierChoiceAdapter.CourierViewHolder> {

    private ViewListener viewListener;

    public CourierChoiceAdapter(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public CourierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item_shipment, parent, false);

        return new CourierViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourierViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface ViewListener {
        void onCourierItemClick();
    }

    protected class CourierViewHolder extends RecyclerView.ViewHolder {

        CourierViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}