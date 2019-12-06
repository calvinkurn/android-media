package com.tokopedia.discovery.newdiscovery.hotlistRevamp.domain.usecases

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.hotListDetail.HotListDetailResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class HotlistDetailUseCase @Inject constructor(private val context: Context) : UseCase<HotListDetailResponse>() {
    override fun createObservable(requestParams: RequestParams?): Observable<HotListDetailResponse> {

        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_hotlist_detail), HotListDetailResponse::class.java, requestParams?.parameters, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(HotListDetailResponse::class.java) as HotListDetailResponse)
        }
    }
}