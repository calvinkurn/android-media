package com.tokopedia.discovery.newdiscovery.domain.usecase

import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetDynamicAutoSelectedFilterUseCase @Inject constructor(private val getHotListFilterValueUseCase: GetHotListFilterValueUseCase
                                                              , private val getDynamicFilterUseCase: GetDynamicFilterUseCase) : UseCase<DynamicFilterModel>() {

    companion object {
        const val KEY_PRODUCT_ALIAS = "productKey"
        const val KEY_OFFICIAL_FLAG = "official"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFilterModel>? {

        val productAlias = requestParams!!.getString(KEY_PRODUCT_ALIAS, "")

        requestParams.clearValue(KEY_PRODUCT_ALIAS)
        return Observable.zip(
                getHotListFilterValueUseCase.createObservable(getHotListFilterValueUseCase.createRequestParams(productAlias)).subscribeOn(Schedulers.io()),
                getDynamicFilterUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
        ) { queriesItemList, dynamicFilterModel ->
            var flag: Boolean = false

            queriesItemList?.forEach {
                if (it!!.filterKey.equals(KEY_OFFICIAL_FLAG)) {
                    flag = it.filterValue!!.toBoolean()
                }
            }
            DynamicFilterModel(dynamicFilterModel.data, dynamicFilterModel.processTime, dynamicFilterModel.status, flag)
        }
    }

}