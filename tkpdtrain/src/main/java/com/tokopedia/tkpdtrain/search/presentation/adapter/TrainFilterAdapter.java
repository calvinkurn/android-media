package com.tokopedia.tkpdtrain.search.presentation.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.tkpdtrain.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 3/23/18.
 */

public class TrainFilterAdapter extends RecyclerView.Adapter {

    private List<String> listObjectFilter;
    private ActionListener listener;

    public TrainFilterAdapter() {
        this.listObjectFilter = new ArrayList<>();
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_train_filter, null);
        return new ItemViewFilter(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewFilter itemViewFilter = (ItemViewFilter) holder;
        itemViewFilter.itemName.setText(listObjectFilter.get(position));
        itemViewFilter.checkBoxFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    listener.onCheckChanged(listObjectFilter.get(position));
                }
            }
        });
        itemViewFilter.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCheckChanged(listObjectFilter.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listObjectFilter.size();
    }

    static class ItemViewFilter extends RecyclerView.ViewHolder {

        private TextView itemName;
        private AppCompatCheckBox checkBoxFilter;
        private RelativeLayout itemContainer;

        public ItemViewFilter(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_title);
            checkBoxFilter = itemView.findViewById(R.id.checkbox);
            itemContainer = itemView.findViewById(R.id.item_container);
        }
    }

    public void addList(ArrayList<String> listObjectFilter) {
        this.listObjectFilter.clear();
        this.listObjectFilter = listObjectFilter;
        notifyDataSetChanged();
    }

    public interface ActionListener {
        void onCheckChanged(String itemSelected);
    }
}
