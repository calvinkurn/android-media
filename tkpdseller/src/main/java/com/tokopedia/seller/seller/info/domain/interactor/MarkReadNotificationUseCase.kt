package com.tokopedia.seller.seller.info.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.seller.seller.info.constant.*
import com.tokopedia.seller.seller.info.data.model.NotificationUpdateActionResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class MarkReadNotificationUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                      @Named(MARK_SELLER_INFO_READ_NOTIFICATION) private val rawQuery: String)
    : UseCase<NotificationUpdateActionResponse>() {

    companion object {
        const val PARAM_NOTIF_ID = "notifId"
        @JvmStatic
        fun createRequestParams(notificationId: String): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_NOTIF_ID, notificationId)
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<NotificationUpdateActionResponse> {
        val graphqlRequest = GraphqlRequest(rawQuery, NotificationUpdateActionResponse::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setSessionIncluded(true).build()
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        return graphqlUseCase.createObservable(requestParams).map {
            val data: NotificationUpdateActionResponse =
                    it.getData(NotificationUpdateActionResponse::class.java) ?: throw RuntimeException()
            data
        }
    }
}