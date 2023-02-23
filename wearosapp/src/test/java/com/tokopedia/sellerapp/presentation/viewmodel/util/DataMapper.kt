package com.tokopedia.sellerapp.presentation.viewmodel.util

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerapp.domain.model.SummaryModel
import com.tokopedia.sellerapp.presentation.model.MenuItem

object DataMapper {
    fun getUpdatedMenuCounter(homeMenu: List<MenuItem>, listSummary: List<SummaryModel>) : List<MenuItem> {
        return homeMenu.toMutableList().apply {
            listSummary.forEach { summaryModel ->
                val index = indexOfFirst { it.title == summaryModel.title }
                if(index != -1){
                    this[index] = this[index].copy(
                        unreadCount = summaryModel.counter.toIntOrZero(),
                        dataKey = summaryModel.dataKey
                    )
                }
            }
        }
    }
}