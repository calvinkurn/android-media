package com.tokopedia.tokocash.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tokocash.activation.presentation.presetation.ActivateTokoCashFragment;
import com.tokopedia.tokocash.activation.presentation.presetation.RequestOTPWalletFragment;
import com.tokopedia.tokocash.activation.presentation.presetation.SuccessActivateFragment;
import com.tokopedia.tokocash.historytokocash.presentation.activity.HistoryTokoCashActivity;
import com.tokopedia.tokocash.qrpayment.presentation.activity.CustomScannerTokoCashActivity;
import com.tokopedia.tokocash.qrpayment.presentation.activity.NominalQrPaymentActivity;
import com.tokopedia.tokocash.qrpayment.presentation.activity.SuccessPaymentQRActivity;

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

    void inject(SuccessPaymentQRActivity successPaymentQRActivity);

    void inject(ActivateTokoCashFragment activateTokoCashFragment);

    void inject(RequestOTPWalletFragment requestOTPWalletFragment);

    void inject(SuccessActivateFragment successActivateFragment);
}
