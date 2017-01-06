package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;

import com.tokopedia.seller.topads.model.data.Cell;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticCtrFragment extends TopAdsStatisticFragment {

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsStatisticCtrFragment();
        return fragment;
    }

    @Override
    public String getValueData(Cell cell) {
        return String.valueOf(cell.getCtrPercentage());
    }
}
