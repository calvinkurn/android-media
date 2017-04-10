package com.tokopedia.seller.opportunity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.viewmodel.FilterItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 4/7/17.
 */

public class OpportunityFilterAdapter extends RecyclerView.Adapter<OpportunityFilterAdapter.ViewHolder> {

    private final Context context;

    public interface FilterListener {
        void onFilterClicked(int filterItemViewModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView filterTitle;
        ImageView redDot;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            filterTitle = (TextView) itemView.findViewById(R.id.title_filter);
            redDot = (ImageView) itemView.findViewById(R.id.red_circle);
            mainView = itemView.findViewById(R.id.main_view);
        }
    }

    List<FilterItemViewModel> list;
    FilterListener listener;

    public static OpportunityFilterAdapter createInstance(Context context, FilterListener listener) {
        return new OpportunityFilterAdapter(context, listener);
    }

    public OpportunityFilterAdapter(Context context, FilterListener listener) {
        list = new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_opportunity_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list.get(position).isSelected())
            holder.mainView.setBackgroundColor(MethodChecker.getColor(context, R.color.white));
        else
            holder.mainView.setBackgroundColor(MethodChecker.getColor(context, R.color.transparent));

        if (list.get(position).isActive())
            holder.redDot.setVisibility(View.VISIBLE);
        else
            holder.redDot.setVisibility(View.INVISIBLE);

        holder.filterTitle.setText(list.get(position).getFilterTitleName());
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (FilterItemViewModel item : list) {
                    item.setSelected(false);
                }
                list.get(position).setSelected(true);
                notifyDataSetChanged();
                listener.onFilterClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<FilterItemViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
