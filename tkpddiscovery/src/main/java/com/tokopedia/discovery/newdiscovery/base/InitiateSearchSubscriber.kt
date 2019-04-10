package com.tokopedia.discovery.newdiscovery.base

import android.text.TextUtils
import com.tokopedia.discovery.newdiscovery.domain.gql.InitiateSearchGqlResponse
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ListHelper
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

private const val DISCOVERY_URL_SEARCH = 1
private const val DISCOVERY_APPLINK = 2

class InitiateSearchSubscriber<D : BaseDiscoveryContract.View>(
    private val discoveryView : D,
    private val searchParameter : SearchParameter,
    private val isForceSearch : Boolean
) : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if(graphqlResponse == null) throw Exception("GraphQL Response is null")

        val gqlResponse : InitiateSearchGqlResponse = graphqlResponse.getData(InitiateSearchGqlResponse::class.java)

        when (defineRedirectApplink(gqlResponse.searchProduct?.redirection?.redirectApplink ?: "")) {
            DISCOVERY_URL_SEARCH -> onHandleSearch(gqlResponse)
            DISCOVERY_APPLINK -> onHandleApplink(gqlResponse.searchProduct?.redirection?.redirectApplink ?: "")
            else -> discoveryView.onHandleResponseUnknown()
        }
    }

    private fun defineRedirectApplink(applink: String): Int {
        return if (TextUtils.isEmpty(applink)) {
            DISCOVERY_URL_SEARCH
        } else {
            DISCOVERY_APPLINK
        }
    }

    private fun onHandleSearch(gqlResponse: InitiateSearchGqlResponse) {
        val model = ProductViewModel()
        model.isHasCatalog = ListHelper.isContainItems(gqlResponse.searchProduct?.catalogs)
        model.searchParameter = searchParameter
        model.isForceSearch = isForceSearch
        discoveryView.onHandleResponseSearch(model)
    }

    private fun onHandleApplink(applink: String) {
        discoveryView.onHandleApplink(applink)
    }

    override fun onCompleted() { }

    override fun onError(e: Throwable?) {
        discoveryView.onHandleResponseError()
        e?.printStackTrace()
    }
}