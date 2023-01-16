package com.tokopedia.sellerapp.data.repository

import com.tokopedia.sellerapp.data.datasource.local.SummaryRoomDatasource
import com.tokopedia.sellerapp.data.datasource.local.entity.SummaryEntity
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.util.Action
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SummaryRepository @Inject constructor(
    private val clientMessageDatasource: ClientMessageDatasource,
    private val summaryRoomDatasource: SummaryRoomDatasource
): BaseRepository<List<SummaryEntity>> {

    override fun getCachedData(params: Array<String>): Flow<List<SummaryEntity>> {
        return summaryRoomDatasource.getSummaryList()
    }

    override suspend fun sendMessagesToNodes(action: Action) {
        clientMessageDatasource.sendMessagesToNodes(action)
    }

}