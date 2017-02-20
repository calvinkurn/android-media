package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.domain.model.data.Cell;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticKlikFragment extends TopAdsStatisticFragment {

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsStatisticKlikFragment();
        return fragment;
    }

    @Override
    public float getValueData(Cell cell) {
        return (float)cell.getClickSum();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.title_top_ads_statistic_graph_click);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return cell.getClickSumFmt();
    }
}
