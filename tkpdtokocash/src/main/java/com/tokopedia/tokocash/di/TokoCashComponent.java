package com.tokopedia.tokocash.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tokocash.activation.presentation.fragment.ActivateTokoCashFragment;
import com.tokopedia.tokocash.activation.presentation.fragment.RequestOTPWalletFragment;
import com.tokopedia.tokocash.activation.presentation.fragment.SuccessActivateFragment;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.HelpHistoryDetailFragment;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.HistoryTokoCashFragment;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.HomeTokoCashFragment;
import com.tokopedia.tokocash.historytokocash.presentation.fragment.MoveToSaldoFragment;
import com.tokopedia.tokocash.pendingcashback.domain.GetPendingCasbackUseCase;
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

    GetPendingCasbackUseCase getPendingCasbackUseCase();

    void inject(CustomScannerTokoCashActivity customScannerTokoCashActivity);

    void inject(NominalQrPaymentActivity nominalQrPaymentActivity);

    void inject(SuccessPaymentQRActivity successPaymentQRActivity);

    void inject(ActivateTokoCashFragment activateTokoCashFragment);

    void inject(RequestOTPWalletFragment requestOTPWalletFragment);

    void inject(SuccessActivateFragment successActivateFragment);

    void inject(HistoryTokoCashFragment historyTokoCashFragment);

    void inject(HomeTokoCashFragment homeTokoCashFragment);

    void inject(HelpHistoryDetailFragment helpHistoryDetailFragment);

    void inject(MoveToSaldoFragment moveToSaldoFragment);
}
