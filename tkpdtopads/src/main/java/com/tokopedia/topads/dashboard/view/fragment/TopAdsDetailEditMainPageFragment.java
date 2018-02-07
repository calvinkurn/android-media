package com.tokopedia.topads.dashboard.view.fragment;

import android.text.TextUtils;
import android.view.View;

import com.tokopedia.topads.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailPresenter;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public abstract class TopAdsDetailEditMainPageFragment<V extends Ad> extends TopAdsDetailFragment<TopAdsDetailPresenter, V> {

    private LabelView cost;
    private LabelView schedule;

    @Override
    protected void initView(View view) {
        super.initView(view);
        cost = view.findViewById(R.id.cost);
        schedule = view.findViewById(R.id.schedule);
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        cost.setEnabled(false);
        schedule.setEnabled(false);
    }

    @Override
    public void onAdLoaded(V ad) {
        super.onAdLoaded(ad);
        cost.setEnabled(true);
        schedule.setEnabled(true);
        cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCostClicked();
            }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onScheduleClicked();
            }
        });
    }

    protected abstract void onScheduleClicked();

    protected abstract void onCostClicked();

    @Override
    protected void updateMainView(V ad) {
        cost.setContent(ad.getPriceBidFmt());
        if (TextUtils.isEmpty(ad.getEndTime())) {
            schedule.setContent(ad.getEndDate());
        } else {
            schedule.setContent(getString(R.string.top_ads_range_date_text, ad.getEndDate(), ad.getEndTime()));
        }
    }
}
