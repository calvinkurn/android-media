package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.item.DeletableItemView;
import com.tokopedia.discovery.R;

import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class ExpandableItemSelectedListAdapter extends
        RecyclerView.Adapter<ExpandableItemSelectedListAdapter.ViewHolder> {

    List<Option> selectedOptionsList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.selected_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(selectedOptionsList.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedOptionsList.size();
    }

    public void setSelectedOptionsList(List<Option> selectedOptionsList) {
        this.selectedOptionsList = selectedOptionsList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        DeletableItemView selectedItem;

        public ViewHolder(View itemView) {
            super(itemView);
            selectedItem = (DeletableItemView) itemView.findViewById(R.id.selected_item);
        }

        public void bind(Option option) {
            selectedItem.setItemName(option.getName());
        }
    }
}
