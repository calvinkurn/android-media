package com.tokopedia.transaction.common.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transaction.common.data.ticket.SubmitHelpTicketRequest
import com.tokopedia.transaction.common.data.ticket.SubmitHelpTicketResponse
import com.tokopedia.transaction.common.sharedata.ticket.SubmitTicketResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Named

class SubmitHelpTicketUseCase @Inject constructor(@Named(QUERY_NAME) val queryString: String, val graphqlUseCase: GraphqlUseCase) : UseCase<SubmitTicketResult>() {

    var compositeSubscription = CompositeSubscription()

    override fun createObservable(params: RequestParams): Observable<SubmitTicketResult> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(GraphqlRequest(queryString, SubmitHelpTicketResponse::class.java, mapOf(Pair(PARAM, params.getObject(PARAM) as SubmitHelpTicketRequest))))
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    val submitHelpTicket = it.getData<SubmitHelpTicketResponse>(SubmitHelpTicketResponse::class.java)
                    val submitTicketResult = SubmitTicketResult()
                    if (submitHelpTicket.status == STATUS_OK) {
                        submitTicketResult.status = true
                        if (submitHelpTicket.data.message.isNotEmpty()) {
                            submitTicketResult.message = submitHelpTicket.data.message[0]
                        }
                    } else {
                        submitTicketResult.status = false
                        if (submitHelpTicket.errorMessage.isNotEmpty()) {
                            submitTicketResult.message = submitHelpTicket.errorMessage[0]
                        }
                    }
                    submitTicketResult
                }
    }

    companion object {

        const val STATUS_OK = "OK"

        const val PARAM = "ErrorDetail"

        const val QUERY_NAME = "submit_ticket"
    }
}