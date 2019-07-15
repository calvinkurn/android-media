package com.tokopedia.discovery.newdiscovery.domain.usecase

import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class GetDyanamicAutoSelecetedFilterUseCase @Inject constructor(private val getHotListFilterValueUseCase: GetHotListFilterValueUseCase
                                                                , private val getDynamicFilterUseCase: GetDynamicFilterUseCase) : UseCase<DynamicFilterModel>() {

    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFilterModel>? {

        val productAlias = requestParams!!.getString(KEY_PRODUCT_ALIAS, "")

        requestParams.clearValue(KEY_PRODUCT_ALIAS)
        return Observable.zip(
                getHotListFilterValueUseCase.createObservable(getHotListFilterValueUseCase.createRequestParams(productAlias)).subscribeOn(Schedulers.io()),
                getDynamicFilterUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
        ) { t1, t2 ->
            var flag: Boolean = false

            t1?.forEach {
                if (it!!.filterKey.equals(KEY_OFFICIAL_FLAG)) {
                    flag = it.filterKey!!.toBoolean()
                }
            }
            DynamicFilterModel(t2.data, t2.processTime, t2.status, flag)
        }
    }

    companion object {
        const val KEY_PRODUCT_ALIAS = "productKey"
        const val KEY_OFFICIAL_FLAG = "official"
    }

}