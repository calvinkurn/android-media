package com.tokopedia.sellerapp.data.mapper

import com.google.gson.Gson
import com.tokopedia.sellerapp.data.datasource.local.entity.SummaryEntity
import com.tokopedia.sellerapp.data.datasource.remote.SummaryDataModel

object SummaryDataMapper {
    fun mapMessageDataToModel(message: String) : SummaryDataModel {
        return Gson().fromJson(message, SummaryDataModel::class.java)
    }

    fun SummaryDataModel.mapModelToEntity() : List<SummaryEntity> {
        return data.list.map {
            SummaryEntity(
                dataKey = it.dataKey,
                description = it.description,
                value = it.value
            )
        }
    }
}