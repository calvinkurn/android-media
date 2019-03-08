package com.tokopedia.tkpdpdp.estimasiongkir.domain.interactor

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesEstimationModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import rx.Observable

class GetRateEstimationUseCase(private val graphqlUseCase: GraphqlUseCase) : UseCase<RatesEstimationModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<RatesEstimationModel> {
        val query = requestParams.getString(PARAM_QUERY, "")
        requestParams.clearValue(PARAM_QUERY)

        val graphqlRequest = GraphqlRequest(query, RatesEstimationModel.Response::class.java, requestParams.parameters, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(null).map {
            graphqlResponse -> graphqlResponse.getData<RatesEstimationModel.Response>(RatesEstimationModel.Response::class.java).data.ratesEstimation[0]
        }
    }

    companion object {
        private const val PARAM_QUERY = "query"
        private const val PARAM_PRODUCT_WEIGHT = "weight"
        private const val PARAM_SHOP_DOMAIN = "domain"

        @JvmStatic
        fun createRequestParams(query: String, productWeight: Float, shopDomain: String): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_QUERY, query)
                putObject(PARAM_PRODUCT_WEIGHT, productWeight)
                putString(PARAM_SHOP_DOMAIN, shopDomain)
            }
        }
    }
}
