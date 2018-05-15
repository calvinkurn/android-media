package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.BottomSheetExpandableItemSelectedListAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.ExpandableItemSelectedListAdapter;
import com.tokopedia.discovery.newdynamicfilter.view.BottomSheetDynamicFilterView;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

public class BottomSheetExpandableItemViewHolder extends DynamicFilterViewHolder {

    LinearLayout titleContainer;
    TextView title;
    View seeAllButton;
    RecyclerView recyclerView;
    BottomSheetExpandableItemSelectedListAdapter adapter;
    BottomSheetDynamicFilterView filterView;

    public BottomSheetExpandableItemViewHolder(View itemView, BottomSheetDynamicFilterView filterView) {
        super(itemView);
        this.filterView = filterView;
        titleContainer = (LinearLayout) itemView.findViewById(R.id.title_container);
        seeAllButton = itemView.findViewById(R.id.see_all_button);
        title = (TextView) itemView.findViewById(R.id.expandable_item_title);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.expandable_item_selected_list);
    }

    @Override
    public void bind(final Filter filter) {
        adapter = new BottomSheetExpandableItemSelectedListAdapter(filterView, filter.getTitle());
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        title.setText(filter.getTitle());
        if (hasCustomOptions(filter)) {
            titleContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterView.onExpandableItemClicked(filter);
                }
            });
            seeAllButton.setVisibility(View.VISIBLE);
        } else {
            titleContainer.setOnClickListener(null);
            seeAllButton.setVisibility(View.GONE);
        }

        adapter.setSelectedOptionsList(filterView.getSelectedOptions(filter));
    }

    private boolean hasCustomOptions(Filter filter) {
        for (Option option : filter.getOptions()) {
            if (!option.isPopular()) {
                return true;
            }
        }
        return false;
    }
}
