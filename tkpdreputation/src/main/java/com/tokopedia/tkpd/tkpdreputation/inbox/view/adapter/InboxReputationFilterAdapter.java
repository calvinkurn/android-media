package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterViewModel;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterAdapter
        extends RecyclerView.Adapter<InboxReputationFilterAdapter.ViewHolder> {

    private static final int VIEW_FILTER = 100;

    public interface FilterListener {

        void onFilterSelected(int position, String optionViewModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFilterSelected(filterViewModel.getPosition(),
                            filterViewModel.getListChild().get(getAdapterPosition()).getName());
                }
            });
        }
    }

    private FilterViewModel filterViewModel;
    private final FilterListener listener;


    public static InboxReputationFilterAdapter createInstance(FilterListener listener) {
        return new InboxReputationFilterAdapter(listener);
    }

    private InboxReputationFilterAdapter(FilterListener listener) {
        this.listener = listener;
        this.filterViewModel = new FilterViewModel();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.checkBox.setText(filterViewModel.getListChild().get(position).getName());
        holder.checkBox.setChecked(filterViewModel.getListChild().get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return filterViewModel.getListChild().size();
    }

    public void setData(FilterViewModel filterViewModel) {
        this.filterViewModel = filterViewModel;
        notifyDataSetChanged();
    }
}
