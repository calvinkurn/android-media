package com.tokopedia.tokocash.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tokocash.historytokocash.presentation.activity.HistoryTokoCashActivity;
import com.tokopedia.tokocash.qrpayment.presentation.activity.CustomScannerTokoCashActivity;
import com.tokopedia.tokocash.qrpayment.presentation.activity.NominalQrPaymentActivity;

import dagger.Component;

/**
 * Created by nabillasabbaha on 12/27/17.
 */
@TokoCashScope
@Component(modules = TokoCashModule.class, dependencies = BaseAppComponent.class)
public interface TokoCashComponent {

    void inject(HistoryTokoCashActivity historyTokoCashActivity);

    void inject(CustomScannerTokoCashActivity customScannerTokoCashActivity);

    void inject(NominalQrPaymentActivity nominalQrPaymentActivity);
}
