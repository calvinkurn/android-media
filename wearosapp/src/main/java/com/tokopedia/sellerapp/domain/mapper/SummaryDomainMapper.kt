package com.tokopedia.sellerapp.domain.mapper

import com.tokopedia.sellerapp.data.datasource.local.entity.SummaryEntity
import com.tokopedia.sellerapp.domain.model.SummaryModel
import com.tokopedia.sellerapp.presentation.model.TITLE_NEW_ORDER
import com.tokopedia.sellerapp.presentation.model.TITLE_READY_TO_DELIVER

object SummaryDomainMapper {
    const val DATAKEY_UNREAD_CHAT = "unreadChat"
    const val DATAKEY_NEW_ORDER = "newOrder"
    const val DATAKEY_READY_TO_SHIP = "readyToShipOrder"

    fun List<SummaryEntity>.mapToListDomainModel() : List<SummaryModel> {
        return map {
            SummaryModel(
                title = it.getTitleByDataKey(),
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
                title = it.getTitleByDataKey(),
                dataKey = it.dataKey,
                description = it.description,
                counter = it.value
            )
        } ?: SummaryModel()
    }

    private fun SummaryEntity.getTitleByDataKey() : String {
        return when(dataKey) {
            DATAKEY_NEW_ORDER -> TITLE_NEW_ORDER
            DATAKEY_READY_TO_SHIP -> TITLE_READY_TO_DELIVER
            else -> ""
        }
    }
}