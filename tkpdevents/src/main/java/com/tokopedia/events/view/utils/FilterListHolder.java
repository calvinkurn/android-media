package com.tokopedia.events.view.utils;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.domain.model.searchdomainmodel.FilterDomainModel;
import com.tokopedia.events.view.adapter.FiltersAdapter;
import com.tokopedia.events.view.presenter.EventSearchPresenter;

import butterknife.BindView;

/**
 * Created by pranaymohapatra on 16/01/18.
 */

public class FilterListHolder {

    @BindView(R2.id.tv_filter_label)
    TextView tvFilterLabel;
    @BindView(R2.id.rv_filter_list)
    RecyclerView rvFilterList;

    public void setRecyclerView(FilterDomainModel data, EventSearchPresenter presenter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(rvFilterList.getContext(),
                LinearLayoutManager.VERTICAL, false);
        FiltersAdapter adapter = new FiltersAdapter(data, rvFilterList.getContext(), presenter);
        rvFilterList.setLayoutManager(layoutManager);
        rvFilterList.setAdapter(adapter);
        rvFilterList.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvFilterList.getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(rvFilterList.getContext().getResources().getDrawable(R.drawable.recycler_view_divider));
        rvFilterList.addItemDecoration(dividerItemDecoration);

    }

    public void setTvFilterLabel(String text) {
        tvFilterLabel.setText(text);
    }

}
