package com.tokopedia.sellerapp.data.datasource.local

import com.tokopedia.sellerapp.data.datasource.local.dao.SummaryDao
import com.tokopedia.sellerapp.data.datasource.local.entity.SummaryEntity
import com.tokopedia.sellerapp.data.datasource.remote.SummaryDataModel
import com.tokopedia.sellerapp.data.mapper.SummaryDataMapper.mapModelToEntity
import kotlinx.coroutines.flow.Flow

class SummaryRoomDatasource(
    private val summaryDao: SummaryDao
) {
    fun saveSummary(summaryDataModel: SummaryDataModel) {
        summaryDao.clear()
        summaryDao.insertSummary(summaryDataModel.mapModelToEntity())
    }

    fun getSummaryList() : Flow<List<SummaryEntity>>{
        return summaryDao.getSummaryList()
    }
}