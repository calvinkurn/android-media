package com.tokopedia.discovery.search.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.R2;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.view.adapter.DefaultSearchResultAdapter;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.search.view.adapter.viewmodel.DefaultViewModel;

import butterknife.BindView;

/**
 * @author erry on 14/02/17.
 */

public class DefaultSearchViewHolder extends AbstractViewHolder<DefaultViewModel> implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.default_search_parent_list_item;
    private static final String TAG = DefaultSearchViewHolder.class.getSimpleName();

    @BindView(R2.id.list)
    RecyclerView recyclerView;
    @BindView(R2.id.container_search)
    LinearLayout titleContainer;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.delete)
    TextView delete;
    private Context context;
    private final DefaultSearchResultAdapter resultAdapter;

    public DefaultSearchViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        context = itemView.getContext();
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        resultAdapter = new DefaultSearchResultAdapter(context, clickListener);
        recyclerView.setAdapter(resultAdapter);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "Delete");
    }

    @Override
    public void bind(DefaultViewModel element) {
        resultAdapter.setSearchTerm(element.getSearchTerm());
        title.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
        switch (element.getId()){
            case autocomplete:
                title.setVisibility(View.GONE);
                break;
            case recent_search:
                delete.setVisibility(View.VISIBLE);
                title.setText(context.getString(R.string.title_search_recent));
                break;
            case hotlist:
                title.setText(context.getString(R.string.title_search_hotlist));
                break;
            case popular_search:
                title.setText(context.getString(R.string.title_search_popular));
                break;
            case shop:
                title.setText(context.getString(R.string.title_search_shop));
                break;
            case in_category:
                title.setText(context.getString(R.string.title_search_category));
                break;
        }
        resultAdapter.setModel(element);
    }
}