package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.Router;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.MarketInsightAdapter;
import com.tokopedia.seller.product.view.activity.ProductAddActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 11/23/16.
 */

public class MarketInsightViewHolder {

    public static final String DEFAULT_CATEGORY = "kaos";
    public static final int MAX_KEYWORD_SHOWN = 3;

    private TextView tvMarketInsightFooter;
    private TitleCardView view;
    private boolean isGoldMerchant;
    private MarketInsightAdapter marketInsightAdapter;

    public MarketInsightViewHolder(TitleCardView view, boolean isGoldMerchant) {
        this.view = view;
        this.isGoldMerchant = isGoldMerchant;
        initView(view);
    }

    public void addProductMarketInsight() {
        Intent intent = new Intent(view.getContext(), ProductAddActivity.class);
        view.getContext().startActivity(intent);

        // analytic below : https://phab.tokopedia.com/T18496
        UnifyTracking.eventClickGMStatMarketInsight();
    }

    private void moveToGMSubscribe() {
        if (!isGoldMerchant) {
            Router.goToGMSubscribe(view.getContext());
        }
    }

    private void initView(final View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tvMarketInsightFooter = (TextView) view.findViewById(R.id.market_insight_footer);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                this.view.getContext(), LinearLayoutManager.VERTICAL, false));
        marketInsightAdapter = new MarketInsightAdapter(new ArrayList<GetKeyword.SearchKeyword>());
        recyclerView.setAdapter(marketInsightAdapter);
    }

    /**
     * set category for footer
     */
    public void bindCategory(String categoryName) {
        if (TextUtils.isEmpty(categoryName)) {
            tvMarketInsightFooter.setVisibility(View.INVISIBLE);
        } else {
            tvMarketInsightFooter.setText(MethodChecker.fromHtml(
                    tvMarketInsightFooter.getContext().getString(
                            R.string.gm_statistic_these_keywords_are_based_on_category_x, categoryName)));
            tvMarketInsightFooter.setVisibility(View.VISIBLE);
        }
    }

    public void bindData(List<GetKeyword> getKeywords) {
        if (!isGoldMerchant) {
            displayNonGoldMerchant();
            return;
        }

        if (getKeywords == null || getKeywords.size() <= 0) {
            displayEmptyState();
            return;
        }

        //[START] check whether all is empty
        int isNotEmpty = 0;
        for (GetKeyword getKeyword : getKeywords) {
            if (getKeyword.getSearchKeyword() == null || getKeyword.getSearchKeyword().isEmpty())
                isNotEmpty++;
        }

        // if all keyword empty
        if (isNotEmpty == getKeywords.size()) {
            displayEmptyState();
            return;
        }

        // remove null or empty
        for (int i = 0; i < getKeywords.size(); i++) {
            if (getKeywords.get(i) == null
                    || getKeywords.get(i).getSearchKeyword() == null
                    || getKeywords.get(i).getSearchKeyword().isEmpty())
                getKeywords.remove(i);
        }

        GetKeyword getKeyword = getKeywords.get(0);

        List<GetKeyword.SearchKeyword> searchKeyword = getKeyword.getSearchKeyword();
        marketInsightAdapter.setSearchKeywords(searchKeyword);
        marketInsightAdapter.notifyDataSetChanged();
        setViewState(LoadingStateView.VIEW_CONTENT);
    }

    public void setViewState(int viewState) {
        view.setViewState(viewState);
    }

    private void displayNonGoldMerchant() {
        view.setEmptyViewRes(R.layout.widget_market_insight_empty_no_gm);
        View emptyView = view.getEmptyView();
        emptyView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToGMSubscribe();
                    }
                }
        );
        // content will be overlayed behind the empty state
        view.getContentView().setVisibility(View.VISIBLE);

        displayDummyContentKeyword();
        view.setViewState(LoadingStateView.VIEW_EMPTY);
    }

    private void displayDummyContentKeyword(){
        // create dummy data as replacement for non gold merchant user.
        List<GetKeyword.SearchKeyword> searchKeyword = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            GetKeyword.SearchKeyword searchKeyword1 = new GetKeyword.SearchKeyword();
            searchKeyword1.setFrequency(1000);
            searchKeyword1.setKeyword(
                    String.format(
                            view.getContext().getString(R.string.market_insight_item_non_gm_text),
                            Integer.toString(i)
                    )
            );
            searchKeyword.add(searchKeyword1);
        }

        bindCategory(DEFAULT_CATEGORY);

        marketInsightAdapter.setSearchKeywords(searchKeyword);
        marketInsightAdapter.notifyDataSetChanged();
    }

    private void displayEmptyState() {
        view.setEmptyViewRes(R.layout.widget_market_insight_empty_no_data);
        view.getEmptyView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProductMarketInsight();
                    }
                });
        view.setViewState(LoadingStateView.VIEW_EMPTY);
    }

    public void bindNoShopCategory() {
        if (isGoldMerchant)
            displayEmptyState();
        else {
            displayNonGoldMerchant();
        }
    }
}