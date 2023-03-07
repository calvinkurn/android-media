package com.tokopedia.sellerapp.domain.mapper

import com.tokopedia.sellerapp.data.datasource.local.entity.SummaryEntity
import com.tokopedia.sellerapp.domain.model.SummaryModel
import com.tokopedia.sellerapp.presentation.model.TITLE_NEW_ORDER
import com.tokopedia.sellerapp.presentation.model.TITLE_READY_TO_SHIP
import com.tokopedia.sellerapp.util.MenuHelper
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_NEW_ORDER
import com.tokopedia.sellerapp.util.MenuHelper.DATAKEY_READY_TO_SHIP

object SummaryDomainMapper {

    fun List<SummaryEntity>.mapToListDomainModel() : List<SummaryModel> {
        return map {
            SummaryModel(
                title = MenuHelper.getTitleByDataKey(it.dataKey),
                dataKey = it.dataKey,
                description = it.description,
                counter = it.value
            )
        }
    }

    fun List<SummaryEntity>.mapToEachDomainModel(dataKey: String) : SummaryModel {
        val summary = singleOrNull { it.dataKey == dataKey }
        return summary?.let {
            SummaryModel(
                title = MenuHelper.getTitleByDataKey(dataKey),
                dataKey = it.dataKey,
                description = it.description,
                counter = it.value
            )
        } ?: SummaryModel()
    }
}