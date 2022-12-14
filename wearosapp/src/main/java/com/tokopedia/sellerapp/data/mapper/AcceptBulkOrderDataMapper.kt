package com.tokopedia.sellerapp.data.mapper

import com.google.gson.Gson
import com.tokopedia.sellerapp.data.datasource.remote.AcceptBulkOrderModel

object AcceptBulkOrderDataMapper {

    fun String.mapMessageDataToAcceptBulkOrderModel() : AcceptBulkOrderModel {
        return Gson().fromJson(this, AcceptBulkOrderModel::class.java)
    }

}