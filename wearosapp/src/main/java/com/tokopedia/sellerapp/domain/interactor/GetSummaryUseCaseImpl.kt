package com.tokopedia.sellerapp.domain.interactor

import com.tokopedia.sellerapp.data.repository.SummaryRepository
import com.tokopedia.sellerapp.domain.mapper.SummaryDomainMapper.mapToEachDomainModel
import com.tokopedia.sellerapp.domain.mapper.SummaryDomainMapper.mapToListDomainModel
import com.tokopedia.sellerapp.domain.model.SummaryModel
import com.tokopedia.sellerapp.util.Action
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSummaryUseCaseImpl @Inject constructor(
    private val summaryRepository: SummaryRepository
): GetSummaryUseCase {
    override fun getMenuItemCounter(): Flow<List<SummaryModel>> {
        return summaryRepository.getCachedData().map { entity ->
            entity.mapToListDomainModel()
        }
    }

    override fun getOrderSummary(dataKey: String): Flow<SummaryModel> {
        return summaryRepository.getCachedData().map {
            it.mapToEachDomainModel(dataKey = dataKey)
        }
    }

    override suspend fun fetchOrderSummaryData() {
        summaryRepository.sendMessagesToNodes(Action.GET_SUMMARY)
    }
}