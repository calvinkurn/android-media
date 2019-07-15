package com.tokopedia.seller.seller.info.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.seller.seller.info.data.model.ResponseSellerInfoModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class SellerCenterUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                              @Named("SELLER_CENTER_RAW") private val rawQuery: String)
    : UseCase<ResponseSellerInfoModel>() {

    companion object {
        const val PAGE_KEY = "page"
        const val TYPE_ID_KEY = "typeId"
        const val TAG_ID_KEY = "tagId"
        const val LAST_NOTIF_ID_KEY = "lastNotifId"
        const val TYPE_ID_PARAM_SELLER = 2
        const val TAG_ID_PARAM_NO_FILTER = 0

        fun createRequestParams(page: Int, lastNotifId: String): RequestParams {
            return RequestParams.create().apply {
                putInt(PAGE_KEY, page)
                putString(LAST_NOTIF_ID_KEY, lastNotifId)
                putInt(TYPE_ID_KEY, TYPE_ID_PARAM_SELLER)
                putInt(TAG_ID_KEY, TAG_ID_PARAM_NO_FILTER)
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ResponseSellerInfoModel> {
        val graphqlRequest = GraphqlRequest(rawQuery, ResponseSellerInfoModel::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: ResponseSellerInfoModel? = it.getData(ResponseSellerInfoModel::class.java)
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: listOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.first().message)
            } else if (data.notifData.list.isEmpty()) {
                throw MessageErrorException("list is empty or wrong pojo")
            }

            data
        }
    }
}