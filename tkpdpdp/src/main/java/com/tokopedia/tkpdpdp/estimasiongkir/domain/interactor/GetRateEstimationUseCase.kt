package com.tokopedia.tkpdpdp.estimasiongkir.domain.interactor

import android.content.Context

import com.google.gson.Gson
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesEstimationModel
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.io.IOException
import java.io.InputStream

import com.tokopedia.tkpdpdp.R

import rx.Observable

class GetRateEstimationUseCase(private val graphqlUseCase: GraphqlUseCase, // for testing
                               private val context: Context) : UseCase<RatesModel>() {
    private val gson = Gson()

    override fun createObservable(requestParams: RequestParams): Observable<RatesModel> {
        val query = requestParams.getString(PARAM_QUERY, "")
        requestParams.clearValue(PARAM_QUERY)
        val graphqlRequest = GraphqlRequest(query, RatesEstimationModel.Response::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(null).map { graphqlResponse ->
            val (ratesEstimation) = graphqlResponse.getData<RatesEstimationModel.Response>(RatesEstimationModel.Response::class.java)
            ratesEstimation.rates
        }.onErrorReturn { throwable ->
            gson.fromJson(inputStreamToString(context.resources.openRawResource(R.raw.raw_json_est_shipping)),
                    RatesEstimationModel.Response::class.java).ratesEstimation.rates
        }
    }

    // temporary for testing
    private fun inputStreamToString(inputStream: InputStream): String? {
        try {
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            return String(bytes)
        } catch (e: IOException) {
            return null
        }

    }

    companion object {
        private val PARAM_QUERY = "query"
        private val PARAM_PRODUCT_ID = "product_id"
        private val PARAM_USER_ID = "user_id"

        fun createRequestParams(query: String, productId: String, userId: String): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_QUERY, query)
                putString(PARAM_PRODUCT_ID, productId)
                putString(PARAM_USER_ID, userId)
            }
        }
    }
}
