package com.tokopedia.topads.dashboard.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.view.listener.OneUseGlobalLayoutListener;
import com.tokopedia.topads.common.view.utils.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.util.ArrayList;
import java.util.List;

public class TopAdsStatisticProductActivity extends TopAdsStatisticActivity {

    private ShowCaseDialog showCaseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getTypeStatistic() {
        return TopAdsNetworkConstant.TYPE_PRODUCT_STAT;
    }

    @Override
    public void updateDataCell(List<Cell> cells) {
        super.updateDataCell(cells);
        startShowCase();
    }

    public void startShowCase() {
        final String showCaseTag = TopAdsStatisticProductActivity.class.getName();
        if (ShowCasePreference.hasShown(this, showCaseTag)) {
            return;
        }
        if (showCaseDialog!= null) {
            return;
        }

        showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar.getHeight() > 0) {
            final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();

            showCaseList.add(
                    new ShowCaseObject(
                            getDateLabelView(),
                            getString(R.string.topads_showcase_statistics_title_1),
                            getString(R.string.topads_showcase_statistics_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE));

            showCaseList.add(new ShowCaseObject(
                    tabLayout,
                    getString(R.string.topads_showcase_statistics_title_2),
                    getString(R.string.topads_showcase_statistics_desc_2),
                    ShowCaseContentPosition.UNDEFINED,
                    R.color.tkpd_main_green));

            showCaseDialog.show(TopAdsStatisticProductActivity.this, showCaseTag, showCaseList);

        } else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new OneUseGlobalLayoutListener(toolbar,
                    new OneUseGlobalLayoutListener.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            startShowCase();
                        }
                    }));
        }
    }


    @Override
    protected void onCostSelected() {
        UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CPC);
    }

    @Override
    protected void onAverageConversionSelected() {
        UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_AVERAGE_CONVERSION);
    }

    @Override
    protected void onConversionSelected() {
        UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CONVERSION);
    }

    @Override
    protected void onCtrSelected() {
        UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CTR);
    }

    @Override
    protected void onClickSelected() {
        UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CLICK);
    }

    @Override
    protected void onImpressionSelected() {
        UnifyTracking.eventTopAdsProductStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_IMPRESSION);
    }
}
