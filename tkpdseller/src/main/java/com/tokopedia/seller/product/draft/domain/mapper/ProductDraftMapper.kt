package com.tokopedia.seller.product.draft.domain.mapper

import android.text.TextUtils
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.seller.manageitem.data.db.ProductDraftViewModel
import javax.inject.Inject

class ProductDraftMapper @Inject constructor() {
    fun mapDomainDataModelToViewModel(drafts: List<ProductDraft>) = drafts.map { draft ->
        val primaryImageUrl = draft.detailInputModel.imageUrlOrPathList.firstOrNull().orEmpty()
        val productName = draft.detailInputModel.productName
        val completionPercent = draft.completionPercent

        ProductDraftViewModel(
                draft.draftId,
                primaryImageUrl,
                productName,
                completionPercent,
                !TextUtils.isEmpty(draft.productId.toString()))
    }
}