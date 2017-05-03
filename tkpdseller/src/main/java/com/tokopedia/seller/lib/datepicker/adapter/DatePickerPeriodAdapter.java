package com.tokopedia.seller.lib.datepicker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.model.PeriodRangeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 12/2/2016.
 */

public class DatePickerPeriodAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_DATA = 100;

    private List<PeriodRangeModel> data;
    private int selectedPosition;

    public void setData(List<PeriodRangeModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public DatePickerPeriodAdapter() {
        data = new ArrayList<>();
        selectedPosition = 0;
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_DATA:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_date_picker_periode, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_DATA:
                bindProduct((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else {
            return VIEW_DATA;
        }
    }

    private void bindProduct(final ViewHolder holder, int position) {
        PeriodRangeModel periodRangeModel = data.get(position);
        holder.radioButton.setChecked(position == selectedPosition);
        holder.titleTextView.setText(periodRangeModel.getLabel());
        holder.contentTextView.setText(periodRangeModel.getDescription(holder.contentTextView.getContext()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(holder.getAdapterPosition());
            }
        });
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectItem(holder.getAdapterPosition());
            }
        });
    }

    private void onSelectItem(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public PeriodRangeModel getSelectedDate() {
        return data.get(selectedPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RadioButton radioButton;
        public TextView titleTextView;
        public TextView contentTextView;

        public ViewHolder(View view) {
            super(view);
            radioButton = (RadioButton) view.findViewById(R.id.radio_button);
            titleTextView = (TextView) view.findViewById(R.id.text_view_title);
            contentTextView = (TextView) view.findViewById(R.id.text_view_content);
        }
    }
}