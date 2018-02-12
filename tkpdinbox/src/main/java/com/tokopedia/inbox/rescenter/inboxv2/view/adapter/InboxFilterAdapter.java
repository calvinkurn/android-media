package com.tokopedia.inbox.rescenter.inboxv2.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxFilterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 29/01/18.
 */

public class InboxFilterAdapter extends RecyclerView.Adapter<InboxFilterAdapter.Holder> {

    private Context context;
    private ResoInboxFilterModel inboxFilterModel;

    public InboxFilterAdapter(ResoInboxFilterModel inboxFilterModel) {
        this.inboxFilterModel = inboxFilterModel;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_inbox_filter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        FilterViewModel viewModel = inboxFilterModel.getFilterViewModelList().get(pos);
        bindView(holder, viewModel);
        bindViewListener(holder, viewModel);
    }

    private void bindView(Holder holder, FilterViewModel viewModel) {
        holder.tvFilter.setText(viewModel.getTypeNameDetail());
        holder.cbFilter.setSelected(this.inboxFilterModel.getSelectedFilterList().contains(viewModel.getOrderValue()));
        holder.cbFilter.setChecked(this.inboxFilterModel.getSelectedFilterList().contains(viewModel.getOrderValue()));
    }

    private void bindViewListener(Holder holder, final FilterViewModel viewModel) {
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inboxFilterModel.getSelectedFilterList().contains(viewModel.getOrderValue())) {
                    removeSelectedItem(viewModel.getOrderValue());
                } else {
                    addSelectedItem(viewModel.getOrderValue());
                }
            }
        });

        holder.cbFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inboxFilterModel.getSelectedFilterList().contains(viewModel.getOrderValue())) {
                    removeSelectedItem(viewModel.getOrderValue());
                } else {
                    addSelectedItem(viewModel.getOrderValue());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return inboxFilterModel.getFilterViewModelList().size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        CheckBox cbFilter;
        TextView tvFilter;

        public Holder(View itemView) {
            super(itemView);
            llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
            cbFilter = (CheckBox) itemView.findViewById(R.id.checkbox_filter);
            tvFilter = (TextView) itemView.findViewById(R.id.tv_filter);
        }
    }

    public void removeSelectedItem(int orderValue) {
        List<Integer> newList = new ArrayList<>();
        for (Integer oldOrderValueList : inboxFilterModel.getSelectedFilterList()) {
            if (oldOrderValueList != orderValue) {
                newList.add(oldOrderValueList);
            }
        }
        inboxFilterModel.setSelectedFilterList(newList);
        notifyDataSetChanged();
    }

    public void addSelectedItem(int orderValue) {
        this.inboxFilterModel.getSelectedFilterList().add(orderValue);
        notifyDataSetChanged();
    }

    public ResoInboxFilterModel getInboxFilterModel() {
        return inboxFilterModel;
    }
}
