package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.design.color.ColorSampleView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetExpandableItemSelectedListAdapter extends
        RecyclerView.Adapter<BottomSheetExpandableItemSelectedListAdapter.ViewHolder> {

    private List<Option> selectedOptionsList = new ArrayList<>();
    private String filterTitle;
    private final DynamicFilterView filterView;

    public BottomSheetExpandableItemSelectedListAdapter(final DynamicFilterView filterView,
                                                        String filterTitle) {
        this.filterView = filterView;
        this.filterTitle = filterTitle;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.bottom_sheet_selected_filter_item, parent, false);
        return new ViewHolder(view, this, filterView, filterTitle);
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
        private BottomSheetExpandableItemSelectedListAdapter adapter;
        private String filterTitle;
        private View ratingIcon;
        private final DynamicFilterView filterView;

        public ViewHolder(View itemView,
                          BottomSheetExpandableItemSelectedListAdapter adapter,
                          final DynamicFilterView filterView,
                          String filterTitle) {
            super(itemView);

            initViewHolderViews(itemView);

            this.adapter = adapter;
            this.filterView = filterView;
            this.filterTitle = filterTitle;
        }

        private void initViewHolderViews(View itemView) {
            itemText = itemView.findViewById(R.id.filter_item_text);
            ratingIcon = itemView.findViewById(R.id.rating_icon);
            colorIcon = itemView.findViewById(R.id.color_icon);
            itemContainer = itemView.findViewById(R.id.filter_item_container);
        }

        public void bind(final Option option, final int position) {
            bindRatingOption(option);

            bindColorOption(option);

            itemText.setText(option.getName());

            if (option.isCategoryOption()) {
                bindCategoryOption(option);
            } else {
                bindGeneralOption(option, position);
            }
        }

        private void bindRatingOption(Option option) {
            if (Option.KEY_RATING.equals(option.getKey())) {
                ratingIcon.setVisibility(View.VISIBLE);
            } else {
                ratingIcon.setVisibility(View.GONE);
            }
        }

        private void bindColorOption(Option option) {
            if (!TextUtils.isEmpty(option.getHexColor())) {
                colorIcon.setVisibility(View.VISIBLE);
                colorIcon.setColor(Color.parseColor(option.getHexColor()));
            } else {
                colorIcon.setVisibility(View.GONE);
            }
        }

        private void bindCategoryOption(final Option option) {
            final boolean isOptionSelected = filterView.getFilterValue(option.getKey()).equals(option.getValue());
            setItemContainerBackgroundResource(isOptionSelected);

            itemContainer.setOnClickListener(view -> {
                boolean newCheckedState = !isOptionSelected;
                String categoryValue = newCheckedState ? option.getValue() : "";
                setAndApplyFilter(option, categoryValue);
                adapter.notifyDataSetChanged();
            });
        }

        private void bindGeneralOption(final Option option, final int position) {
            final boolean isOptionSelected = filterView.getFlagFilterHelperValue(option.getUniqueId());
            setItemContainerBackgroundResource(isOptionSelected);

            itemContainer.setOnClickListener(view -> {
                boolean newCheckedState = !isOptionSelected;
                setAndApplyFlagFilterHelper(option, newCheckedState);
                adapter.notifyItemChanged(position);
            });
        }

        private void setItemContainerBackgroundResource(boolean isSelected) {
            if (isSelected) {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_selected);
            } else {
                itemContainer.setBackgroundResource(R.drawable.quick_filter_item_background_neutral);
            }
        }

        private void setAndApplyFilter(Option option, String value) {
            filterView.setFilterValue(option, value);
            filterView.applyFilter();
        }

        private void setAndApplyFlagFilterHelper(Option option, Boolean value) {
            filterView.setFilterValueExpandableItem(option, value);
            filterView.applyFilter();
        }
    }
}
