package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.data.model.data.Cell;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticAvgFragment extends TopAdsStatisticFragment {

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsStatisticAvgFragment();
        return fragment;
    }

    @Override
    public float getValueData(Cell cell) {
        return (float) cell.getCostAvg();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.title_top_ads_statistic_graph_avg);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return cell.getCostAvgFmt();
    }
}
