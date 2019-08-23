package com.tokopedia.discovery.newdiscovery.base

import android.text.TextUtils
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ListHelper
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

private const val DISCOVERY_URL_SEARCH = 1
private const val DISCOVERY_APPLINK = 2

open class InitiateSearchSubscriber(
    private val initiateSearchListener: InitiateSearchListener
) : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if(graphqlResponse == null) {
            initiateSearchListener.onHandleResponseError()
            return
        }

        val initiateSearchModel : InitiateSearchModel? = graphqlResponse.getData<InitiateSearchModel>(
            InitiateSearchModel::class.java)

        val redirectApplink = getRedirectApplink(initiateSearchModel)

        when (defineRedirectApplink(redirectApplink)) {
            DISCOVERY_URL_SEARCH -> onHandleSearch(initiateSearchModel)
            DISCOVERY_APPLINK -> initiateSearchListener.onHandleApplink(redirectApplink)
            else -> initiateSearchListener.onHandleResponseUnknown()
        }
    }

    private fun getRedirectApplink(initiateSearchModel: InitiateSearchModel?) : String {
        return initiateSearchModel?.searchProduct?.redirection?.redirectApplink ?: ""
    }

    private fun defineRedirectApplink(applink: String): Int {
        return if (TextUtils.isEmpty(applink)) {
            DISCOVERY_URL_SEARCH
        } else {
            DISCOVERY_APPLINK
        }
    }

    private fun onHandleSearch(initiateSearchModel: InitiateSearchModel?) {
        val isHasCatalog = ListHelper.isContainItems(initiateSearchModel?.searchProduct?.catalogs)
        initiateSearchListener.onHandleResponseSearch(isHasCatalog)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        initiateSearchListener.onHandleResponseError()
        e?.printStackTrace()
    }
}