package com.tokopedia.discovery.newdiscovery.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.domain.model.filter.FilterValueResponse
import com.tokopedia.discovery.newdiscovery.domain.model.filter.QueriesItem
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject


class GetHotListFilterValueUseCase @Inject constructor(@ApplicationContext private val context: Context
                                                       , private val graphqlUseCase: GraphqlUseCase) : UseCase<List<QueriesItem?>?>() {
    override fun createObservable(requestParams: RequestParams?): Observable<List<QueriesItem?>?> {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.hotlist_detail), FilterValueResponse::class.java, requestParams!!.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {

            ((it.getData(FilterValueResponse::class.java)) as FilterValueResponse).data?.hotlistDetail?.queries

        }
    }


    private val KEY_PRODUCT_KEY = "productKey"

    fun createRequestParams(productKey: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(KEY_PRODUCT_KEY, productKey)
        return requestParams
    }

}