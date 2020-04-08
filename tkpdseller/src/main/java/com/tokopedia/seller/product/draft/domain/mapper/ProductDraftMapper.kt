package com.tokopedia.seller.product.draft.domain.mapper

import android.text.TextUtils
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.product.manage.item.main.draft.data.model.ProductDraftViewModel
import javax.inject.Inject

class ProductDraftMapper @Inject constructor() {
    fun mapDomainDataModelToViewModel(drafts: List<ProductDraft>) = drafts.map { draft ->
        val pictures = draft.detailInputModel.pictureList
        val primaryImageUrl = if (pictures.isNotEmpty()) {
            val picture = pictures[0]
            picture.urlOriginal
        } else {
            ""
        }
        val productName = draft.detailInputModel.productName
        val completionPercent = draft.completionPercent

        ProductDraftViewModel(
                draft.productId,
                primaryImageUrl,
                productName,
                completionPercent,
                !TextUtils.isEmpty(draft.productId.toString()))
    }
}