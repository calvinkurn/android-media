package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.RatingHelper;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.color.ColorSampleView;
import com.tokopedia.design.item.DeletableItemView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetExpandableItemSelectedListAdapter extends
        RecyclerView.Adapter<BottomSheetExpandableItemSelectedListAdapter.ViewHolder> {

    private List<Option> selectedOptionsList = new ArrayList<>();
    private DynamicFilterView filterView;

    public BottomSheetExpandableItemSelectedListAdapter(DynamicFilterView filterView) {
        this.filterView = filterView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.bottom_sheet_selected_filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(selectedOptionsList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return selectedOptionsList.size();
    }

    public void setSelectedOptionsList(List<Option> selectedOptionsList) {
        this.selectedOptionsList = selectedOptionsList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemText;
        private ColorSampleView colorIcon;
        private View itemContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.filter_item_text);
            colorIcon = itemView.findViewById(R.id.color_icon);
            itemContainer = itemView.findViewById(R.id.filter_item_container);
        }

        public void bind(final Option option, final int position) {
            if (!TextUtils.isEmpty(option.getHexColor())) {
                colorIcon.setVisibility(View.VISIBLE);
                colorIcon.setColor(Color.parseColor(option.getHexColor()));
            } else {
                colorIcon.setVisibility(View.GONE);
            }

            if (Boolean.parseBoolean(option.getInputState())) {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_selected);
            } else {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_neutral);
            }
            itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newCheckedState = !Boolean.parseBoolean(option.getInputState());
                    filterView.saveCheckedState(option, newCheckedState);
                    option.setInputState(Boolean.toString(newCheckedState));
                    notifyItemChanged(position);
                }
            });
            itemText.setText(option.getName());
        }
    }
}
