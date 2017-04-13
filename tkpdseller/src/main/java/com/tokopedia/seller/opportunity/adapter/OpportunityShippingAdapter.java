package com.tokopedia.seller.opportunity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.viewmodel.ShippingTypeViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 4/7/17.
 */

public class OpportunityShippingAdapter extends RecyclerView.Adapter<OpportunityShippingAdapter.ViewHolder> {

    ArrayList<ShippingTypeViewModel> list;

    public interface ShippingListener {
        void onShippingSelected(int position, ShippingTypeViewModel shippingTypeViewModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }

    ShippingListener listener;

    public static OpportunityShippingAdapter createInstance(ShippingListener listener) {
        return new OpportunityShippingAdapter(listener);
    }

    public OpportunityShippingAdapter(ShippingListener listener) {
        this.listener = listener;
        this.list = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_opportunity_shipping, parent, false));
    }

    @Override

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.checkBox.setText(list.get(position).getShippingTypeName());

        holder.checkBox.setChecked(list.get(position).isSelected());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setSelected(holder.checkBox.isChecked());

                for (ShippingTypeViewModel viewModel : list) {
                    if (viewModel != list.get(position))
                        viewModel.setSelected(false);
                }

                listener.onShippingSelected(position,list.get(position));
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<ShippingTypeViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
