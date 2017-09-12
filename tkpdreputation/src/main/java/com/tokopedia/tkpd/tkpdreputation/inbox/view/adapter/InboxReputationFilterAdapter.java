package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.filter.HeaderOptionViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.filter.OptionViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface FilterListener {
        void onFilterSelected(OptionViewModel optionViewModel);
    }

    private static final int VIEW_HEADER = 101;

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OptionViewModel viewModel : listOption) {
                        if (viewModel == listOption.get(getAdapterPosition()))
                            viewModel.setSelected(!viewModel.isSelected());
                        else if (viewModel.getKey() == listOption.get(getAdapterPosition()).getKey())
                            viewModel.setSelected(false);
                    }

                    if(listOption.get(getAdapterPosition()).isSelected()){
                        listener.onFilterSelected(listOption.get(getAdapterPosition()));
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    private final ArrayList<OptionViewModel> listOption;
    private final FilterListener listener;


    public static InboxReputationFilterAdapter createInstance(FilterListener listener,
                                                                      ArrayList<OptionViewModel>
                                                                      listOption) {
        return new InboxReputationFilterAdapter(listener, listOption);
    }

    private InboxReputationFilterAdapter(FilterListener listener,
                                         ArrayList<OptionViewModel> listOption) {
        this.listener = listener;
        this.listOption = listOption;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listview_filter_header, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listview_filter, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent,
                                 int position) {
        if (getItemViewType(position) == VIEW_HEADER) {
            HeaderViewHolder holder = (HeaderViewHolder) parent;
            holder.title.setText(listOption.get(position).getName());
        } else {
            ViewHolder holder = (ViewHolder) parent;
            holder.checkBox.setText(listOption.get(position).getName());
            holder.checkBox.setChecked(listOption.get(position).isSelected());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (listOption.get(position) instanceof HeaderOptionViewModel)
            return VIEW_HEADER;
        else return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listOption.size();
    }

    public void resetFilter() {
        for (OptionViewModel optionViewModel : listOption) {
            optionViewModel.setSelected(false);
        }
        notifyDataSetChanged();
    }
}
