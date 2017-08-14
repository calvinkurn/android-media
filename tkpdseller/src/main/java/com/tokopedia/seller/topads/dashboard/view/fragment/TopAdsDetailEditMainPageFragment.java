package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.text.TextUtils;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsEditCostExistingGroupActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsEditScheduleExistingGroupActivity;
import com.tokopedia.seller.topads.dashboard.view.model.Ad;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailPresenter;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public abstract class TopAdsDetailEditMainPageFragment<V extends Ad> extends TopAdsDetailFragment<TopAdsDetailPresenter, V> {

    private LabelView cost;
    private LabelView schedule;

    @Override
    protected void initView(View view) {
        super.initView(view);
        cost = (LabelView) view.findViewById(R.id.cost);
        schedule = (LabelView) view.findViewById(R.id.schedule);
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();

        cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TopAdsEditCostExistingGroupActivity.createIntent(getActivity(), String.valueOf(ad.getId())));
            }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TopAdsEditScheduleExistingGroupActivity.createIntent(getActivity(), String.valueOf(ad.getId())));
            }
        });
    }

    @Override
    protected void updateMainView(V ad) {
        cost.setTitle(ad.getPriceBidFmt());
        if (TextUtils.isEmpty(ad.getEndTime())) {
            schedule.setContent(ad.getEndDate());
        } else {
            schedule.setContent(getString(R.string.top_ads_range_date_text, ad.getEndDate(), ad.getEndTime()));
        }
    }
}
