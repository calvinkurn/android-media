package com.tokopedia.seller.product.draft.domain.mapper

import android.text.TextUtils
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.product.manage.common.feature.draft.mapper.AddEditProductDraftMapper
import com.tokopedia.seller.manageitem.data.db.ProductDraftViewModel

object ProductDraftMapper {
    fun mapDomainDataModelToViewModel(drafts: List<ProductDraft>) = drafts.map { draft ->
        val primaryImageUrl = draft.detailInputModel.imageUrlOrPathList.firstOrNull().orEmpty()
        val productName = draft.detailInputModel.productName
        val completionPercent = AddEditProductDraftMapper.getCompletionPercent(draft)

        ProductDraftViewModel(
                draft.draftId,
                primaryImageUrl,
                productName,
                completionPercent,
                !TextUtils.isEmpty(draft.productId.toString()))
    }
}