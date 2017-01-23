package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.data.Cell;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticImprFragment extends TopAdsStatisticFragment {

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsStatisticImprFragment();
        return fragment;
    }

    @Override
    public float getValueData(Cell cell) {
        return (float)cell.getImpressionSum();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.title_graph_impr_statistic_topads);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return cell.getImpressionSumFmt();
    }
}
