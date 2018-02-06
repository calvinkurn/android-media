package com.tokopedia.tokocash.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tokocash.activation.presentation.fragment.ActivateTokoCashFragment;
import com.tokopedia.tokocash.activation.presentation.fragment.RequestOTPWalletFragment;
import com.tokopedia.tokocash.activation.presentation.fragment.SuccessActivateFragment;
import com.tokopedia.tokocash.qrpayment.domain.GetBalanceTokoCashUseCase;
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

    GetBalanceTokoCashUseCase getBalanceTokoCashUseCase();

    void inject(CustomScannerTokoCashActivity customScannerTokoCashActivity);

    void inject(NominalQrPaymentActivity nominalQrPaymentActivity);

    void inject(SuccessPaymentQRActivity successPaymentQRActivity);

    void inject(ActivateTokoCashFragment activateTokoCashFragment);

    void inject(RequestOTPWalletFragment requestOTPWalletFragment);

    void inject(SuccessActivateFragment successActivateFragment);
}
