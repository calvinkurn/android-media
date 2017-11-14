package com.tokopedia.topads.dashboard.view.fragment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.topads.R;
import com.tokopedia.design.label.DateLabelView;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.topads.TopAdsModuleRouter;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailViewPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsDetailStatisticFragment<T extends TopAdsDetailViewPresenter, V extends Ad> extends TopAdsDetailViewFragment<T, V> {

    private DateLabelView dateLabelView;

    protected LabelView maxBid;
    protected LabelView avgCost;

    protected LabelView start;
    protected LabelView end;
    protected LabelView dailyBudget;

    private LabelView sent;
    private LabelView impr;
    private LabelView click;
    private LabelView ctr;
    protected LabelView favorite;

    @Override
    protected void initView(View view) {
        super.initView(view);
        initDateLabelView(view);
        maxBid = (LabelView) view.findViewById(R.id.max_bid);
        avgCost = (LabelView) view.findViewById(R.id.avg_cost);
        start = (LabelView) view.findViewById(R.id.start);
        end = (LabelView) view.findViewById(R.id.end);
        dailyBudget = (LabelView) view.findViewById(R.id.daily_budget);
        sent = (LabelView) view.findViewById(R.id.sent);
        impr = (LabelView) view.findViewById(R.id.impr);
        click = (LabelView) view.findViewById(R.id.click);
        ctr = (LabelView) view.findViewById(R.id.ctr);
        favorite = (LabelView) view.findViewById(R.id.favorite);
    }

    protected TopAdsComponent getTopAdsComponent(){
        return TopAdsComponentUtils.getTopAdsComponent(getActivity());
    }

    protected void initDateLabelView(View view) {
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    @Override
    protected void loadData() {
        super.loadData();
        updateDateLabelViewText();
    }

    protected void updateDateLabelViewText() {
        dateLabelView.setDate(datePickerPresenter.getStartDate(), datePickerPresenter.getEndDate());
    }

    @Override
    protected void loadAdDetail(V ad) {
        super.loadAdDetail(ad);
        updateCostView(ad);
        updateDailyBudgetView(ad);
        updateStatisticView(ad);
    }

    protected void updateCostView(V ad) {
        maxBid.setContent(getString(R.string.top_ads_bid_format_text, ad.getPriceBidFmt(), ad.getLabelPerClick()));
        avgCost.setContent(ad.getStatAvgClick());
    }

    protected void updateDailyBudgetView(V ad) {
        start.setContent(ad.getStartDate() + " - " + ad.getStartTime());
        if (TextUtils.isEmpty(ad.getEndTime())) {
            end.setContent(ad.getEndDate());
        } else {
            end.setContent(getString(R.string.top_ads_range_date_text, ad.getEndDate(), ad.getEndTime()));
        }
        if(TextUtils.isEmpty(ad.getPriceDailySpentFmt())) {
            dailyBudget.setContent(ad.getPriceDailyFmt());
        }else{
            dailyBudget.setContent(getString(R.string.topads_format_daily_budget, ad.getPriceDailySpentFmt(), ad.getPriceDailyFmt()));
        }
    }

    protected void updateStatisticView(V ad) {
        sent.setContent(ad.getStatTotalSpent());
        impr.setContent(ad.getStatTotalImpression());
        click.setContent(ad.getStatTotalClick());
        ctr.setContent(ad.getStatTotalCtr());
        favorite.setContent(ad.getStatTotalConversion());
    }
}