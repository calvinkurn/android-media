package com.tokopedia.seller.gmstat.presenters;

import android.content.res.AssetManager;

/**
 * Created by normansyahputa on 1/2/17.
 */

public interface GMFragmentPresenter {
    void fetchData();

    void fetchData(long sDate, long eDate, int lastSelectionPeriod, int selectionType);

    void onResume();

    void onPause();

    void displayDefaultValue(AssetManager assets);
}
