package com.tokopedia.profilecompletion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.pojo.StatusPinData
import com.tokopedia.profilecompletion.data.pojo.StatusPinPojo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-09-12.
 * ade.hadian@tokopedia.com
 */

class PinViewModel @Inject constructor(
        private val getStatusPinUseCase: GraphqlUseCase<StatusPinPojo>,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher){

    private val mutableGetStatusPinResponse = MutableLiveData<Result<StatusPinData>>()
    val getStatusPinResponse: LiveData<Result<StatusPinData>>
        get() = mutableGetStatusPinResponse

    fun getStatusPin(){
        rawQueries[ProfileCompletionQueriesConstant.QUERY_STATUS_PIN]?.let { query ->
            getStatusPinUseCase.setTypeClass(StatusPinPojo::class.java)
            getStatusPinUseCase.setGraphqlQuery(query)
            getStatusPinUseCase.execute(
                    onSuccessGetStatusPin(),
                    onErrorGetStatusPin()
            )
        }
    }

    private fun onErrorGetStatusPin(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableGetStatusPinResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetStatusPin(): (StatusPinPojo) -> Unit {
        return {
            when {
                it.data.errorMessage.isEmpty() -> mutableGetStatusPinResponse.value = Success(it.data)
                it.data.errorMessage.isNotEmpty() ->
                    mutableGetStatusPinResponse.value = Fail(MessageErrorException(it.data.errorMessage))
                else -> mutableGetStatusPinResponse.value = Fail(RuntimeException())
            }
        }
    }
}