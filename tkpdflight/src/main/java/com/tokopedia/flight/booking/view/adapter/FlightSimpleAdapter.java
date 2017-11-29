package com.tokopedia.flight.booking.view.adapter;

import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.label.LabelView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/21/17.
 */

public class FlightSimpleAdapter extends RecyclerView.Adapter<FlightSimpleAdapter.ViewHolder> {
    private List<SimpleViewModel> viewModels;
    private boolean isArrowVisible;
    private boolean isClickable;
    private OnAdapterInteractionListener interactionListener;

    @ColorInt
    private int contentColorValue;

    public interface OnAdapterInteractionListener {
        void onItemClick(int adapterPosition, SimpleViewModel viewModel);
    }

    public FlightSimpleAdapter() {
        viewModels = new ArrayList<>();
        isArrowVisible = false;
        isClickable = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(viewModels.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public void setViewModels(List<SimpleViewModel> viewModels) {
        this.viewModels = viewModels;
    }

    public void setDescriptionTextColor(@ColorInt int colorInt) {
        contentColorValue = colorInt;
    }

    public void setArrowVisible(boolean isArrowVisible) {
        this.isArrowVisible = isArrowVisible;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public void setInteractionListener(OnAdapterInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LabelView labelTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            labelTextView = (LabelView) itemView.findViewById(R.id.header_label);
        }

        public void bind(final SimpleViewModel viewModel) {
            labelTextView.setTitle(viewModel.getLabel());
            labelTextView.setContent(viewModel.getDescription());
            labelTextView.setVisibleArrow(isArrowVisible);
            if (contentColorValue != 0) {
                labelTextView.setContentColorValue(contentColorValue);
            }
            labelTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (interactionListener != null) {
                        interactionListener.onItemClick(getAdapterPosition(), viewModel);
                    }
                }
            });
            if (isClickable) {
                labelTextView.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.selectable_background_tokopedia));
            } else {
                labelTextView.setBackground(null);
            }
        }
    }
}
