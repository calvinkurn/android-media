package com.tokopedia.gm;

import android.app.Activity;

import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface GMModuleRouter {

    GMComponent getGMComponent();

    DataDeposit getDataDeposit(String shopId);

    void goToTopAdsDashboard(Activity activity);

}
