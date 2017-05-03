package com.tokopedia.discovery.search.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import butterknife.OnClick;

/**
 * @author erry on 14/02/17.
 */

public class DefaultSearchViewHolder extends AbstractViewHolder<DefaultViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.default_search_parent_list_item;
    private static final String TAG = DefaultSearchViewHolder.class.getSimpleName();

    @BindView(R2.id.card_view)
    CardView cardView;
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
    private ItemClickListener itemClickListener;

    public DefaultSearchViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        context = itemView.getContext();
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        itemClickListener = clickListener;
        resultAdapter = new DefaultSearchResultAdapter(context, itemClickListener);
        recyclerView.setAdapter(resultAdapter);
    }

    @OnClick(R2.id.delete)
    void onDeleteAllClicked() {
        itemClickListener.onDeleteAllRecentSearch();
    }

    @Override
    public void bind(DefaultViewModel element) {
        resultAdapter.setSearchTerm(element.getSearchTerm());
        title.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        cardView.setCardElevation(0);
        switch (element.getId()) {
            case "autocomplete":
                layoutParams.setMargins(0, 0, 0, context.getResources().
                        getDimensionPixelSize(R.dimen.search_parent_item_card_margin));
                cardView.setCardElevation(5);
                break;
            case "recent_search":
                title.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                title.setText(context.getString(R.string.title_search_recent));
                layoutParams.setMargins(0, 0, 0, context.getResources().
                        getDimensionPixelSize(R.dimen.search_parent_item_card_margin));
                cardView.setCardElevation(5);
                break;
            case "popular_search":
                title.setVisibility(View.VISIBLE);
                title.setText(context.getString(R.string.title_search_popular));
                layoutParams.setMargins(0, 0, 0, context.getResources().
                        getDimensionPixelSize(R.dimen.search_parent_item_card_margin));
                cardView.setCardElevation(5);
                break;
        }
        cardView.setLayoutParams(layoutParams);
        resultAdapter.setModel(element);
    }
}