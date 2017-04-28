package com.tokopedia.seller.opportunity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.viewmodel.FilterViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OptionViewModel;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * Created by nisie on 4/10/17.
 */

public class OpportunityFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_PARENT = 100;
    private static final int VIEW_CHILD = 101;

    public interface CategoryListener {
        void onFilterSelected(int position, FilterViewModel filterViewModel);

        void onFilterExpanded(int position, FilterViewModel filterViewModel);

        void onFilterSelected(int position, String optionViewModel);
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ParentViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.text);
        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public ChildViewHolder(View itemView) {
            super(itemView);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }

    private FilterViewModel filterViewModel;
    private final CategoryListener listener;

    public static OpportunityFilterAdapter createInstance(CategoryListener listener) {
        return new OpportunityFilterAdapter(listener);
    }

    public OpportunityFilterAdapter(CategoryListener listener) {
        this.listener = listener;
        this.filterViewModel = new FilterViewModel();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_PARENT:
                return new ParentViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listview_opportunity_category_parent, parent, false));
            case VIEW_CHILD:
                return new ChildViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listview_opportunity_shipping, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (filterViewModel.getListChild().get(position).getListChild().size() > 0)
            return VIEW_PARENT;
        else
            return VIEW_CHILD;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_PARENT:
                bindParentViewHolder((ParentViewHolder) holder, position);
                break;
            case VIEW_CHILD:
                bindChildViewHolder((ChildViewHolder) holder, position);
                break;
        }
    }

    private void bindChildViewHolder(ChildViewHolder holder, final int position) {
        holder.checkBox.setText(filterViewModel.getListChild().get(position).getName());
        holder.checkBox.setChecked(filterViewModel.getListChild().get(position).isSelected());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFilterSelected(filterViewModel.getPosition(), filterViewModel.getListChild().get(position).getName());
            }
        });

    }

    private void bindParentViewHolder(ParentViewHolder holder, final int position) {
        holder.title.setText(filterViewModel.getListChild().get(position).getName());

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterViewModel.getListChild().get(position).isExpanded()) {
                    collapseGroup(position);
                } else {
                    expandGroup(position);
                }
                listener.onFilterExpanded(position, filterViewModel);
            }
        });

    }

    private void expandGroup(int position) {
        filterViewModel.getListChild().get(position).setExpanded(true);
        filterViewModel.getListChild().addAll(position + 1,
                filterViewModel.getListChild().get(position).getListChild());
        notifyDataSetChanged();

    }

    private void collapseGroup(int position) {
        filterViewModel.getListChild().get(position).setExpanded(false);
        for (int i = position + +getAllOpenChild(position); i > position; i--) {
            if (filterViewModel.getListChild().get(i).isExpanded()) {
                collapseGroup(i);
            }
            filterViewModel.getListChild().remove(i);
        }
        notifyDataSetChanged();
    }

    private int getAllOpenChild(int position) {
        int size = filterViewModel.getListChild().get(position).getListChild().size();
        for (OptionViewModel viewModel : filterViewModel.getListChild().get(position).getListChild()) {
            if (viewModel.getListChild().size() > 0 &&
                    viewModel.isExpanded())
                size += viewModel.getListChild().size();
        }
        return size;
    }

    public void setData(FilterViewModel filterViewModel) {
        this.filterViewModel = filterViewModel;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filterViewModel.getListChild().size();
    }

}
