package com.tokopedia.profilecompletion.data.mapper

import com.tokopedia.core.network.retrofit.response.TkpdResponse
import com.tokopedia.profilecompletion.data.pojo.EditUserInfoData
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 7/3/17.
 */
class EditUserInfoMapper @Inject constructor() : Func1<Response<TkpdResponse>, EditUserInfoDomainModel> {
    override fun call(response: Response<TkpdResponse>): EditUserInfoDomainModel {
        return mappingResponse(response)
    }

    private fun mappingResponse(response: Response<TkpdResponse>): EditUserInfoDomainModel {
        val model = EditUserInfoDomainModel()
        if (response.isSuccessful) {
            if (!response.body()!!.isError) {
                val data = response.body()!!.convertDataObj(EditUserInfoData::class.java)
                model.isSuccess = data.isSuccess == 1
            } else {
                if (response.body()!!.errorMessages == null
                        && response.body()!!.errorMessages.isEmpty()) {
                    model.isSuccess = false
                } else {
                    model.isSuccess = false
                    model.errorMessage = response.body()!!.errorMessageJoined
                }
            }
            model.statusMessage = response.body()!!.statusMessageJoined
        } else {
            model.isSuccess = false
        }
        model.responseCode = response.code()
        return model
    }
}