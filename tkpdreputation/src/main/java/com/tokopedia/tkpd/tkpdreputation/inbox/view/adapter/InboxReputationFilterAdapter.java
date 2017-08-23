package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.OptionViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterAdapter
        extends RecyclerView.Adapter<InboxReputationFilterAdapter.ViewHolder> {

    public interface FilterListener {
        void onFilterSelected(OptionViewModel optionViewModel);
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
                        else
                            viewModel.setSelected(false);
                    }
                    if(getAdapterPosition() != -1)
                    listener.onFilterSelected(listOption.get(getAdapterPosition()));
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
    public InboxReputationFilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(InboxReputationFilterAdapter.ViewHolder holder,
                                 int position) {
        holder.checkBox.setText(listOption.get(position).getName());
        holder.checkBox.setChecked(listOption.get(position).isSelected());

    }

    @Override
    public int getItemCount() {
        return listOption.size();
    }
}
