package com.tokopedia.discovery.newdiscovery.hotlistRevamp.domain.usecases

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmItem
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmTopAdsResponse
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.domain.mapper.CpmItemMapper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class CpmAdsUseCase @Inject constructor(private val context: Context) : UseCase<List<CpmItem>>() {
    override fun createObservable(requestParams: RequestParams?): Observable<List<CpmItem>> {
        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_cpm_top_ads), CpmTopAdsResponse::class.java, requestParams?.parameters, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            CpmItemMapper().transform((it.getData(CpmTopAdsResponse::class.java) as CpmTopAdsResponse))
        }
    }
}