package com.tokopedia.discovery.newdynamicfilter.adapter.viewholder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.ExpandableItemSelectedListAdapter;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterExpandableItemViewHolder extends DynamicFilterViewHolder {

    LinearLayout titleContainer;
    TextView title;
    RecyclerView recyclerView;
    ExpandableItemSelectedListAdapter adapter;
    DynamicFilterView filterView;

    public DynamicFilterExpandableItemViewHolder(View itemView, DynamicFilterView filterView) {
        super(itemView);
        this.filterView = filterView;
        titleContainer = (LinearLayout) itemView.findViewById(R.id.title_container);
        title = (TextView) itemView.findViewById(R.id.expandable_item_title);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.expandable_item_selected_list);
        adapter = new ExpandableItemSelectedListAdapter(filterView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(final Filter filter) {
        title.setText(filter.getTitle());
        titleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterView.onExpandableItemClicked(filter);
            }
        });
        adapter.setSelectedOptionsList(getSelectedOptions(filter.getOptions()));
    }

    private List<Option> getSelectedOptions(List<Option> options) {
        List<Option> selectedOptions = new ArrayList<>();
        for (Option option : options) {
            if (Boolean.TRUE.equals(filterView.loadLastCheckedState(option))) {
                selectedOptions.add(option);
            }
        }
        return selectedOptions;
    }
}
