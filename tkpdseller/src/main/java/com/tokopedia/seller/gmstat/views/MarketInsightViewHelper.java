package com.tokopedia.seller.gmstat.views;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.core.myproduct.ProductActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.Router;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetShopCategory;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.seller.gmstat.utils.GMStatConstant.MARKET_INSIGHT_FOOTER_FORMAT;
import static com.tokopedia.seller.gmstat.utils.GMStatConstant.NUMBER_TIMES_FORMAT;
import static com.tokopedia.seller.gmstat.views.DataTransactionViewHelper.dpToPx;

/**
 * Created by normansyahputa on 11/23/16.
 */

public class MarketInsightViewHelper {

    LinearLayout marketInsightHeader;

    TextView marketInsightHeader_;

    RecyclerView marketInsightRecyclerView;

    TextView marketInsightFooter_;

    LinearLayout marketInsightGoldMerchant;

    LinearLayout marketInsightNonGoldMerchant;

    LinearLayout marketInsightEmptyState;

    FrameLayout marketInsightContainerTop;

    int emptyColorBackground;

    int transparantColor;

    int breakLineBackground;

    LinearLayout separator2;
    private String prefixFooterMarketInsight;

    public void addProductMarketInsight(){
        ProductActivity.moveToAddProduct(view.getContext());
    }

    public void moveToGMSubscribe(){
        if(!isGoldMerchant)
            Router.goToGMSubscribe(view.getContext());
    }

    private View view;
    private boolean isGoldMerchant;
    private MarketInsightAdapter marketInsightAdapter;

    public MarketInsightViewHelper(View view, boolean isGoldMerchant){
        this.view = view;
        this.isGoldMerchant = isGoldMerchant;
        initView(view);

        view.findViewById(R.id.move_to_gmsubscribe_market_insight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToGMSubscribe();
            }
        });

        if(isGoldMerchant){
            separator2.removeAllViews();
            View view2= new View(view.getContext());
            view2.setLayoutParams(new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) dpToPx(view.getContext(), 16)));
            view2.setBackgroundResource(android.R.color.transparent);
            separator2.addView(view2);
        }else{
            separator2.removeAllViews();
            View view2= new View(view.getContext());
            view2.setLayoutParams(new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) dpToPx(view.getContext(), 16)));
            view2.setBackgroundResource(R.color.breakline_background);
            separator2.addView(view2);
        }

    }

    private void initView(final View view) {

        marketInsightHeader= (LinearLayout) view.findViewById(R.id.market_insight_header);

        marketInsightHeader_= (TextView) view.findViewById(R.id.market_insight_header_);

        marketInsightRecyclerView= (RecyclerView) view.findViewById(R.id.market_insight_recyclerview);

        marketInsightFooter_= (TextView) view.findViewById(R.id.market_insight_footer_);

        marketInsightGoldMerchant= (LinearLayout) view.findViewById(R.id.market_insight_gold_merchant);

        marketInsightNonGoldMerchant= (LinearLayout) view.findViewById(R.id.market_insight_non_gold_merchant);

        marketInsightEmptyState= (LinearLayout) view.findViewById(R.id.market_insight_empty_state);

        marketInsightContainerTop= (FrameLayout) view.findViewById(R.id.market_insight_container_top);

        emptyColorBackground = ResourcesCompat.getColor(view.getResources(), R.color.empty_background, null);

        transparantColor = ResourcesCompat.getColor(view.getResources(), android.R.color.transparent, null);

        breakLineBackground = ResourcesCompat.getColor(view.getResources(), R.color.breakline_background, null);

        separator2= (LinearLayout) view.findViewById(R.id.separator_2_market_insight);

        view.findViewById(R.id.market_insight_empty_state)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProductMarketInsight();
                    }
                });

        view.findViewById(R.id.move_to_gmsubscribe_market_insight).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToGMSubscribe();
                    }
                }
        );

        view.findViewById(R.id.market_insight_container_top).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToGMSubscribe();
                    }
                }
        );

        view.findViewById(R.id.market_insight_non_gold_merchant).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToGMSubscribe();
                    }
                }
        );

        view.findViewById(R.id.market_insight_gmsubscribe_text).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToGMSubscribe();
                    }
                }
        );

        prefixFooterMarketInsight = view.getContext().getString(R.string.prefix_footer_market_insight);
    }

    /**
     * set category for footer
     * @param hadesV1Models non null and non empty dataset
     */
    public void bindDataCategory(List<HadesV1Model> hadesV1Models){
        if(hadesV1Models==null||hadesV1Models.size()<=0)
            return;

        HadesV1Model.Category category = hadesV1Models.get(0).getData().getCategories().get(0);

        String categoryBold = String.format(MARKET_INSIGHT_FOOTER_FORMAT, category.getName());
        marketInsightFooter_.setText(MethodChecker.fromHtml(prefixFooterMarketInsight+categoryBold+" "));
    }

    public void bindData(List<GetKeyword> getKeywords){
        if(isGoldMerchant){
            marketInsightGoldMerchant.setVisibility(View.VISIBLE);
            marketInsightNonGoldMerchant.setVisibility(View.GONE);
            marketInsightEmptyState.setVisibility(View.GONE);
        }else{
            displayNonGoldMerchant();
            return;
        }

        if(getKeywords==null||getKeywords.size()<=0) {
            displayEmptyState();
            return;
        }

        //[START] check whether all is empty
        int isNotEmpty = 0;
        for (GetKeyword getKeyword : getKeywords) {
            if(getKeyword.getSearchKeyword() == null || getKeyword.getSearchKeyword().isEmpty())
                isNotEmpty++;
        }

        // if all keyword empty
        if(isNotEmpty == getKeywords.size()) {
            displayEmptyState();
            return;
        }

        // remove null or empty
        for(int i=0;i<getKeywords.size();i++){
            if(getKeywords.get(i) == null
                    || getKeywords.get(i).getSearchKeyword() == null
                    || getKeywords.get(i).getSearchKeyword().isEmpty())
                getKeywords.remove(i);
        }


        GetKeyword getKeyword = getKeywords.get(0);

        List<GetKeyword.SearchKeyword> searchKeyword = getKeyword.getSearchKeyword();

        marketInsightRecyclerView.setLayoutManager(new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.VERTICAL, false));
        marketInsightAdapter = new MarketInsightAdapter(searchKeyword);
        marketInsightRecyclerView.setAdapter(marketInsightAdapter);
        marketInsightContainerTop.setBackgroundColor(transparantColor);
    }

    public void displayNonGoldMerchant() {
        marketInsightGoldMerchant.setVisibility(View.VISIBLE);
        marketInsightNonGoldMerchant.setVisibility(View.VISIBLE);
        marketInsightEmptyState.setVisibility(View.GONE);


        // create dummy data as replacement for non gold merchant user.
        List<GetKeyword.SearchKeyword> searchKeyword = new ArrayList<>();
        for(int i=0;i<3;i++){
            GetKeyword.SearchKeyword searchKeyword1 = new GetKeyword.SearchKeyword();
            searchKeyword1.setFrequency(1000);
            searchKeyword1.setKeyword("kaos");
            searchKeyword.add(searchKeyword1);
        }

        String categoryBold = String.format(MARKET_INSIGHT_FOOTER_FORMAT, "kaos");
        marketInsightFooter_.setText(MethodChecker.fromHtml(prefixFooterMarketInsight+categoryBold+" "));

        marketInsightRecyclerView.setLayoutManager(new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.VERTICAL, false));
        marketInsightAdapter = new MarketInsightAdapter(searchKeyword);
        marketInsightRecyclerView.setAdapter(marketInsightAdapter);
    }

    public void displayEmptyState() {
        marketInsightContainerTop.setBackgroundColor(emptyColorBackground);
        marketInsightGoldMerchant.setVisibility(View.GONE);
        marketInsightNonGoldMerchant.setVisibility(View.GONE);
        marketInsightEmptyState.setVisibility(View.VISIBLE);
    }

    public void bindData(GetShopCategory getShopCategory) {
        if(getShopCategory == null || getShopCategory.getShopCategory() == null || getShopCategory.getShopCategory().isEmpty()){
            if(isGoldMerchant)
                displayEmptyState();
            else{
                displayNonGoldMerchant();
            }
        }
    }

    public static class MarketInsightAdapter extends RecyclerView.Adapter<MarketInsightViewHolder>{

        private double total;
        List<GetKeyword.SearchKeyword> searchKeywords;

        public MarketInsightAdapter(List<GetKeyword.SearchKeyword> searchKeywords){
            this.searchKeywords = searchKeywords;

            total = 0;
            for (GetKeyword.SearchKeyword searchKeyword : searchKeywords) {
                total += searchKeyword.getFrequency();
            }

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
            return searchKeywords.size()>=3?3:searchKeywords.size();
        }
    }

    public static class MarketInsightViewHolder extends RecyclerView.ViewHolder{

        final String TAG = "MarketInsight";

        TextView marketInsightKeyword;

        TextView marketInsightNumber;

        ImageView zoomIcon;

        RoundCornerProgressBar marketInsightProgress;

        void initView(View itemView){

            marketInsightKeyword= (TextView) itemView.findViewById(R.id.market_insight_keyword);

            marketInsightNumber= (TextView) itemView.findViewById(R.id.market_insight_number);

            zoomIcon= (ImageView) itemView.findViewById(R.id.zoom_icon);

            marketInsightProgress= (RoundCornerProgressBar) itemView.findViewById(R.id.market_insight_progress);
        }

        public MarketInsightViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void bindData(GetKeyword.SearchKeyword searchKeyword, List<GetKeyword.SearchKeyword> list){
            double total = list.get(0).getFrequency();

            double v = searchKeyword.getFrequency() / total;
            double percentage = Math.floor((v*100)+0.5);
            Log.d(TAG, "total "+total+" percentage "+percentage+" frequency "+searchKeyword.getFrequency());

            marketInsightProgress.setProgress((float) percentage);

            marketInsightNumber.setText(String.format(NUMBER_TIMES_FORMAT, String.valueOf(searchKeyword.getFrequency())));

            marketInsightKeyword.setText(searchKeyword.getKeyword());
        }
    }
}
