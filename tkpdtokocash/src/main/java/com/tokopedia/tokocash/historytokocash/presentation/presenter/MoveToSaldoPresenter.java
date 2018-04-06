package com.tokopedia.tokocash.historytokocash.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.historytokocash.domain.MoveToSaldoUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.contract.MoveToSaldoContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.ParamsActionHistory;
import com.tokopedia.tokocash.historytokocash.presentation.model.WithdrawSaldo;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 2/26/18.
 */

public class MoveToSaldoPresenter extends BaseDaggerPresenter<MoveToSaldoContract.View>
        implements MoveToSaldoContract.Presenter {

    private MoveToSaldoUseCase moveToSaldoUseCase;

    @Inject
    public MoveToSaldoPresenter(MoveToSaldoUseCase moveToSaldoUseCase) {
        this.moveToSaldoUseCase = moveToSaldoUseCase;
    }

    @Override
    public void processMoveToSaldo(String url, ParamsActionHistory paramsActionHistory) {
        getView().showProgressLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(MoveToSaldoUseCase.REFUND_ID, paramsActionHistory.getRefundId());
        requestParams.putString(MoveToSaldoUseCase.REFUND_TYPE, paramsActionHistory.getRefundType());
        moveToSaldoUseCase.setUrl(url);
        moveToSaldoUseCase.execute(requestParams, new Subscriber<WithdrawSaldo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().wrappingDataFailed();
                getView().hideProgressLoading();
            }

            @Override
            public void onNext(WithdrawSaldo withdrawSaldo) {
                getView().wrappingDataSuccess(withdrawSaldo.getAmount());
                getView().hideProgressLoading();
            }
        });
    }

    @Override
    public void destroyView() {
        if (moveToSaldoUseCase != null) moveToSaldoUseCase.unsubscribe();
    }
}
