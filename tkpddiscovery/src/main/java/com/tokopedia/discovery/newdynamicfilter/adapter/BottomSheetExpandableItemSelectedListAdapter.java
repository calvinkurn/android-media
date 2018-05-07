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
import com.tokopedia.core.widgets.FilterView;
import com.tokopedia.design.color.ColorSampleView;
import com.tokopedia.design.item.DeletableItemView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.view.BottomSheetDynamicFilterView;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetExpandableItemSelectedListAdapter extends
        RecyclerView.Adapter<BottomSheetExpandableItemSelectedListAdapter.ViewHolder> {

    private List<Option> selectedOptionsList = new ArrayList<>();
    private BottomSheetDynamicFilterView filterView;
    private String filterTitle;

    public BottomSheetExpandableItemSelectedListAdapter(BottomSheetDynamicFilterView filterView, String filterTitle) {
        this.filterView = filterView;
        this.filterTitle = filterTitle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.bottom_sheet_selected_filter_item, parent, false);
        return new ViewHolder(view, filterView, this, filterTitle);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemText;
        private ColorSampleView colorIcon;
        private View itemContainer;
        private BottomSheetDynamicFilterView filterView;
        private BottomSheetExpandableItemSelectedListAdapter adapter;
        private String filterTitle;
        private View ratingIcon;

        public ViewHolder(View itemView,
                          BottomSheetDynamicFilterView filterView,
                          BottomSheetExpandableItemSelectedListAdapter adapter,
                          String filterTitle) {
            super(itemView);
            itemText = itemView.findViewById(R.id.filter_item_text);
            ratingIcon = itemView.findViewById(R.id.rating_icon);
            colorIcon = itemView.findViewById(R.id.color_icon);
            itemContainer = itemView.findViewById(R.id.filter_item_container);
            this.filterView = filterView;
            this.adapter = adapter;
            this.filterTitle = filterTitle;
        }

        public void bind(final Option option, final int position) {
            if (Option.KEY_RATING.equals(option.getKey())) {
                ratingIcon.setVisibility(View.VISIBLE);
            } else {
                ratingIcon.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(option.getHexColor())) {
                colorIcon.setVisibility(View.VISIBLE);
                colorIcon.setColor(Color.parseColor(option.getHexColor()));
            } else {
                colorIcon.setVisibility(View.GONE);
            }

            itemText.setText(option.getName());

            if (option.isCategoryOption()) {
                bindCategoryOption(option, position);
            } else {
                bindGeneralOption(option, position);
            }
        }

        private void bindCategoryOption(final Option option, final int position) {
            if (filterView.isSelectedCategory(option)) {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_selected);
            } else {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_neutral);
            }
            itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (filterView.isSelectedCategory(option)) {
                        filterView.removeSelectedOption(option, filterTitle);
                    } else {
                        filterView.selectCategory(option, filterTitle);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }

        private void bindGeneralOption(final Option option, final int position) {
            if (Boolean.parseBoolean(option.getInputState())) {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_selected);
            } else {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_neutral);
            }
            itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newCheckedState = !Boolean.parseBoolean(option.getInputState());
                    filterView.saveCheckedState(option, newCheckedState, filterTitle);
                    option.setInputState(Boolean.toString(newCheckedState));
                    adapter.notifyItemChanged(position);
                }
            });
        }
    }
}
