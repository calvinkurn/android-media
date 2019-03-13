package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.ExpandableItemSelectedListAdapter;
import com.tokopedia.discovery.newdynamicfilter.controller.FilterController;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterExpandableItemViewHolder extends DynamicFilterViewHolder {

    LinearLayout titleContainer;
    TextView title;
    RecyclerView recyclerView;
    ExpandableItemSelectedListAdapter adapter;
    DynamicFilterView filterView;

    public DynamicFilterExpandableItemViewHolder(View itemView, DynamicFilterView filterView, final FilterController filterController) {
        super(itemView, filterView, filterController);

        titleContainer = (LinearLayout) itemView.findViewById(R.id.title_container);
        title = (TextView) itemView.findViewById(R.id.expandable_item_title);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.expandable_item_selected_list);
    }

    @Override
    public void bind(final Filter filter) {
        adapter = new ExpandableItemSelectedListAdapter(filterView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        title.setText(filter.getTitle());
        titleContainer.setOnClickListener(view -> filterView.onExpandableItemClicked(filter));

        adapter.setSelectedOptionsList(filterView.getSelectedOptions(filter));
    }
}
