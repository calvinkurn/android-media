package com.tokopedia.seller.product.draft.domain.interactor

import com.tokopedia.product.manage.common.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class DeleteSingleDraftProductUseCase @Inject constructor(private val addEditProductDraftRepository: AddEditProductDraftRepository) : UseCase<Boolean>() {

    companion object {
        fun createRequestParams(productId: Long): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, productId)
            return requestParams
        }
    }
    
    override fun createObservable(params: RequestParams): Observable<Boolean> {
        val param = params.getLong(AddEditProductDraftConstant.DRAFT_PRODUCT_ID, Long.MIN_VALUE)
        return Observable.just(addEditProductDraftRepository.deleteDraft(param))
    }
}