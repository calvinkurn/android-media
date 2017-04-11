package com.tokopedia.seller.opportunity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.viewmodel.CategoryViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 4/10/17.
 */

public class OpportunityCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_PARENT = 100;
    private static final int VIEW_CHILD = 101;


    public interface CategoryListener {
        void onCategorySelected(CategoryViewModel categoryViewModel);
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

    private ArrayList<CategoryViewModel> list;
    private final CategoryListener listener;

    public static OpportunityCategoryAdapter createInstance(CategoryListener listener) {
        return new OpportunityCategoryAdapter(listener);
    }

    public OpportunityCategoryAdapter(CategoryListener listener) {
        this.listener = listener;
        this.list = new ArrayList<>();
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
        if (list.get(position).getListChild().size() > 0)
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
        holder.checkBox.setText(list.get(position).getCategoryName());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setSelected(!list.get(position).isSelected());
                listener.onCategorySelected(list.get(position));
            }
        });
    }

    private void bindParentViewHolder(ParentViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getCategoryName());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isExpanded()) {
                    list.removeAll(list.get(position).getListChild());
                } else {
                    list.addAll(position + 1, list.get(position).getListChild());
                }

                removeChildren(list, position);
                notifyDataSetChanged();
            }
        });

    }

    private void removeChildren(ArrayList<CategoryViewModel> list, int position) {
        for (int i = list.size(); i == 0; i--) {
            if (i - 1 != position
                    && list.get(i - 1).isExpanded()) {
                list.removeAll(list.get(i - 1).getListChild());
            }
        }
    }

    public void setList(ArrayList<CategoryViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
