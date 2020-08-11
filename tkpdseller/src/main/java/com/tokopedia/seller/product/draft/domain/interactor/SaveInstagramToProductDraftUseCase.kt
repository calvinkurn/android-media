package com.tokopedia.seller.product.draft.domain.interactor

import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.product.manage.common.draft.data.model.detail.PictureInputModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class SaveInstagramToProductDraftUseCase @Inject constructor(private val addEditProductDraftRepository: AddEditProductDraftRepository) : UseCase<List<Long>>()  {

    companion object {
        private const val UPLOAD_PRODUCT_INPUT_MODEL_LIST = "UPLOAD_PRODUCT_INPUT_MODEL_LIST"
        fun generateUploadProductParam(localPathList: ArrayList<String>, instagramDescList: ArrayList<String?>): RequestParams? {
            val productDraftList = ArrayList<ProductDraft>()
            repeat(localPathList.size) {
                val localPath = localPathList[it]
                val productDraft = ProductDraft().apply {
                    detailInputModel.imageUrlOrPathList = listOf(localPath)
                    descriptionInputModel.productDescription = instagramDescList[it] ?: ""
                    val pictureInputModelList: MutableList<PictureInputModel> = ArrayList()
                    val pictureInputModel = PictureInputModel()
                    pictureInputModel.filePath = localPath
                    pictureInputModelList.add(pictureInputModel)
                    detailInputModel.pictureList = pictureInputModelList
                }
                productDraftList.add(productDraft)
            }
            val params = RequestParams.create()
            params.putObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST, productDraftList)
            return params
        }
    }

    override fun createObservable(params: RequestParams): Observable<List<Long>> {
        val productDraftList : ArrayList<ProductDraft> = params.getObject(UPLOAD_PRODUCT_INPUT_MODEL_LIST) as ArrayList<ProductDraft>
        return Observable.from(productDraftList)
                .flatMap {
                   Observable.just(addEditProductDraftRepository.insertDraft(it, false))
                }.toList()
    }
}