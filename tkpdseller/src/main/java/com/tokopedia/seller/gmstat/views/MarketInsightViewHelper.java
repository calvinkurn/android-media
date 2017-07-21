package com.tokopedia.seller.gmstat.views;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.Router;
import com.tokopedia.seller.gmstat.views.widget.TitleCardView;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.product.view.activity.ProductAddActivity;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.seller.gmstat.utils.GMStatConstant.NUMBER_TIMES_FORMAT;

/**
 * Created by normansyahputa on 11/23/16.
 */

public class MarketInsightViewHelper {

    public static final String DEFAULT_CATEGORY = "kaos";
    public static final int MAX_KEYWORD_SHOWN = 3;

    private RecyclerView marketInsightRecyclerView;
    private TextView tvMarketInsightFooter;
    private TitleCardView view;
    private boolean isGoldMerchant;
    private MarketInsightAdapter marketInsightAdapter;

    public MarketInsightViewHelper(TitleCardView view, boolean isGoldMerchant) {
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

    public void moveToGMSubscribe() {
        if (!isGoldMerchant) {
            Router.goToGMSubscribe(view.getContext());
        }
    }

    private void initView(final View view) {

        marketInsightRecyclerView = (RecyclerView) view.findViewById(R.id.market_insight_recyclerview);
        tvMarketInsightFooter = (TextView) view.findViewById(R.id.market_insight_footer);

        marketInsightRecyclerView.setLayoutManager(new LinearLayoutManager(
                this.view.getContext(), LinearLayoutManager.VERTICAL, false));
        marketInsightAdapter = new MarketInsightAdapter(new ArrayList<GetKeyword.SearchKeyword>());
        marketInsightRecyclerView.setAdapter(marketInsightAdapter);
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
                            R.string.these_keywords_are_based_on_category_x, categoryName)));
            tvMarketInsightFooter.setVisibility(View.VISIBLE);
        }
    }

    public void bindData(List<GetKeyword> getKeywords) {
        view.setLoadingState(false);
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
    }

    public void showLoading(){
        view.setLoadingState(true);
    }
    public void hideLoading(){
        view.setLoadingState(false);
    }

    public void displayNonGoldMerchant() {
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
        view.setEmptyState(true);
        // content will be overlayed behind the empty state
        view.getContentView().setVisibility(View.VISIBLE);

        displayDummyContentKeyword();
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

    public void displayEmptyState() {
        view.setEmptyViewRes(R.layout.widget_market_insight_empty_no_data);
        view.getEmptyView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProductMarketInsight();
                    }
                });
        view.setEmptyState(true);
    }

    public void bindNoShopCategory() {
        if (isGoldMerchant)
            displayEmptyState();
        else {
            displayNonGoldMerchant();
        }
    }

    public static class MarketInsightAdapter extends RecyclerView.Adapter<MarketInsightViewHolder> {

        List<GetKeyword.SearchKeyword> searchKeywords;

        public MarketInsightAdapter(List<GetKeyword.SearchKeyword> searchKeywords) {
            setSearchKeywords(searchKeywords);

        }

        public void setSearchKeywords(List<GetKeyword.SearchKeyword> searchKeywords) {
            this.searchKeywords = searchKeywords;
        }

        @Override
        public MarketInsightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.market_insight_item_layout, parent, false);
            return new MarketInsightViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(MarketInsightViewHolder holder, int position) {
            holder.bindData(searchKeywords.get(position), searchKeywords);
        }

        @Override
        public int getItemCount() {
            return searchKeywords.size() >= MAX_KEYWORD_SHOWN ? MAX_KEYWORD_SHOWN : searchKeywords.size();
        }
    }

    public static class MarketInsightViewHolder extends RecyclerView.ViewHolder {

        final String TAG = "MarketInsight";

        TextView marketInsightKeyword;

        TextView marketInsightNumber;

        ImageView zoomIcon;

        RoundCornerProgressBar marketInsightProgress;

        public MarketInsightViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        void initView(View itemView) {

            marketInsightKeyword = (TextView) itemView.findViewById(R.id.market_insight_keyword);

            marketInsightNumber = (TextView) itemView.findViewById(R.id.market_insight_number);

            zoomIcon = (ImageView) itemView.findViewById(R.id.zoom_icon);

            marketInsightProgress = (RoundCornerProgressBar) itemView.findViewById(R.id.market_insight_progress);
        }

        public void bindData(GetKeyword.SearchKeyword searchKeyword, List<GetKeyword.SearchKeyword> list) {
            double total = list.get(0).getFrequency();

            double v = searchKeyword.getFrequency() / total;
            double percentage = Math.floor((v * 100) + 0.5);
            Log.d(TAG, "total " + total + " percentage " + percentage + " frequency " + searchKeyword.getFrequency());

            marketInsightProgress.setProgress((float) percentage);

            marketInsightNumber.setText(String.format(NUMBER_TIMES_FORMAT, String.valueOf(searchKeyword.getFrequency())));

            marketInsightKeyword.setText(searchKeyword.getKeyword());
        }
    }
}
