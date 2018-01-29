package com.tokopedia.flight.booking.view.adapter;

import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/21/17.
 */

public class FlightSimpleAdapter extends RecyclerView.Adapter<FlightSimpleAdapter.ViewHolder> {
    private static final int PARAM_EMPTY_MARGIN = 0;
    private List<SimpleViewModel> viewModels;
    private boolean isArrowVisible;
    private boolean isClickable;
    private boolean isTitleBold;
    private boolean isTitleOnly;
    private boolean isTitleHalfView;
    private boolean isContentAllignmentLeft;
    private OnAdapterInteractionListener interactionListener;

    @ColorInt
    private int contentColorValue;

    public FlightSimpleAdapter() {
        viewModels = new ArrayList<>();
        isArrowVisible = false;
        isClickable = false;
        isTitleBold = false;
        isTitleOnly = false;
        isContentAllignmentLeft = false;
        isTitleHalfView = true;
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

    public void setViewModel(SimpleViewModel simpleViewModel) {
        this.viewModels.add(simpleViewModel);
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

    public void setTitleBold(boolean isTitleBold) {
        this.isTitleBold = isTitleBold;
    }

    public void setTitleOnly(boolean isTitleOnly) {
        this.isTitleOnly = isTitleOnly;
    }

    public void setTitleHalfView(boolean titleHalfView) {
        isTitleHalfView = titleHalfView;
    }

    public void setInteractionListener(OnAdapterInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public void setContentAllignmentLeft(boolean contentAllignmentLeft) {
        isContentAllignmentLeft = contentAllignmentLeft;
    }

    public interface OnAdapterInteractionListener {
        void onItemClick(int adapterPosition, SimpleViewModel viewModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView contentTextView;
        private ImageView arrowImageView;
        private LinearLayout containerLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            contentTextView = (TextView) itemView.findViewById(R.id.tv_content);
            arrowImageView = (ImageView) itemView.findViewById(R.id.iv_arrow);
            containerLinearLayout = (LinearLayout) itemView.findViewById(R.id.container);
        }

        public void bind(final SimpleViewModel viewModel) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titleTextView.getLayoutParams();

            titleTextView.setText(viewModel.getLabel());
            contentTextView.setText(viewModel.getDescription());
            contentTextView.setVisibility(isTitleOnly ? View.GONE : View.VISIBLE);
            arrowImageView.setVisibility(isArrowVisible ? View.VISIBLE : View.GONE);
            if (contentColorValue != 0) {
                contentTextView.setTextColor(contentColorValue);
            }

            if (isTitleBold) {
                titleTextView.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                titleTextView.setTypeface(Typeface.DEFAULT);
            }

            if (isTitleHalfView) {
                layoutParams.width = 0;
                layoutParams.weight = 1;
                titleTextView.setLayoutParams(layoutParams);
            } else {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.weight = 0;
                layoutParams.setMargins(
                        PARAM_EMPTY_MARGIN,
                        PARAM_EMPTY_MARGIN,
                        10,
                        PARAM_EMPTY_MARGIN
                );
                titleTextView.setLayoutParams(layoutParams);
                titleTextView.setMinWidth(150);
            }

            if (isContentAllignmentLeft) {
                contentTextView.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
            }

            containerLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (interactionListener != null) {
                        interactionListener.onItemClick(getAdapterPosition(), viewModel);
                    }
                }
            });
            if (isClickable) {
                containerLinearLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.selectable_background_tokopedia));
            } else {
                containerLinearLayout.setBackground(null);
            }
        }
    }
}
