package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import android.content.res.AssetManager;
import android.os.Bundle;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.listener.GMStatisticDashboardView;

/**
 * Created by normansyahputa on 1/2/17.
 */

public abstract class GMDashboardPresenter extends BaseDaggerPresenter<GMStatisticDashboardView> {

    public abstract void fetchData();

    public abstract void fetchData(long sDate, long eDate, int lastSelectionPeriod, int selectionType);

    public abstract void onResume();

    public abstract void onPause();

    public abstract void displayDefaultValue(AssetManager assets);

    public abstract void saveState(Bundle savedInstanceState);

    public abstract void restoreState(Bundle savedInstanceState);

    public abstract void setFetchData(boolean fetchData);

    public abstract void setFirstTime(boolean firstTime);

    public abstract void initInstance();
}
