package com.tokopedia.tkpdpdp.estimasiongkir.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesEstimationModel
import com.tokopedia.tkpdpdp.estimasiongkir.domain.interactor.GetRateEstimationUseCase
import com.tokopedia.tkpdpdp.estimasiongkir.listener.RatesEstimationDetailView
import rx.Subscriber
import javax.inject.Inject

class RatesEstimationDetailPresenter @Inject
constructor(private val useCase: GetRateEstimationUseCase) : BaseDaggerPresenter<RatesEstimationDetailView>() {

    override fun detachView() {
        useCase.unsubscribe()
        super.detachView()
    }

    fun getCostEstimation(rawQuery: String, productWeight: Float, shopDomain: String = "") {
        useCase.execute(GetRateEstimationUseCase.createRequestParams(rawQuery, productWeight, shopDomain),
                object : Subscriber<RatesEstimationModel>() {
                    override fun onCompleted() {}

                    override fun onError(throwable: Throwable) {
                        throwable.printStackTrace()
                        view?.onErrorLoadRateEstimaion(throwable)
                    }

                    override fun onNext(ratesEstimationModel: RatesEstimationModel) {
                        view?.onSuccesLoadRateEstimaion(ratesEstimationModel)
                    }
                })
    }
}
