package com.tokopedia.tkpdpdp.estimasiongkir.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.tkpdpdp.estimasiongkir.domain.interactor.GetRateEstimationUseCase
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesModel
import com.tokopedia.tkpdpdp.estimasiongkir.listener.RatesEstimationDetailView

import javax.inject.Inject

import rx.Subscriber

class RatesEstimationDetailPresenter @Inject
constructor(private val useCase: GetRateEstimationUseCase, private val userSession: UserSession) : BaseDaggerPresenter<RatesEstimationDetailView>() {

    override fun detachView() {
        useCase.unsubscribe()
        super.detachView()
    }

    fun getCostEstimation(rawQuery: String, productId: String) {
        useCase.execute(GetRateEstimationUseCase.createRequestParams(rawQuery, productId, userSession.userId),
                object : Subscriber<RatesModel>() {
                    override fun onCompleted() {}

                    override fun onError(throwable: Throwable) {
                        throwable.printStackTrace()
                        view?.onErrorLoadRateEstimaion(throwable)
                    }

                    override fun onNext(ratesModel: RatesModel) {
                        view?.onSuccesLoadRateEstimaion(ratesModel)
                    }
                })
    }
}
