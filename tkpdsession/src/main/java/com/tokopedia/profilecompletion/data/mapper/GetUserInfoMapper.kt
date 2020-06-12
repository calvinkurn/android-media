package com.tokopedia.profilecompletion.data.mapper

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.tokopedia.core.profile.model.GetUserInfoDomainData
import com.tokopedia.core.profile.model.GetUserInfoDomainModel
import com.tokopedia.network.ErrorHandler
import com.tokopedia.network.ErrorMessageException
import com.tokopedia.profilecompletion.data.pojo.GetUserInfoData
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 6/19/17.
 */
class GetUserInfoMapper @Inject constructor() : Func1<Response<String>, GetUserInfoDomainModel> {
    override fun call(response: Response<String>): GetUserInfoDomainModel {
        return mappingResponse(response)
    }

    private fun mappingResponse(response: Response<String>): GetUserInfoDomainModel {
        val model = GetUserInfoDomainModel()
        if (response.isSuccessful) {
            val responseJson = response.body().toString()
            try {
                val json = JSONObject(responseJson)
                if (json.getString(ERROR) != ""
                        && json.getString(ERROR_DESCRIPTION) != "") throw ErrorMessageException(json.getString(ERROR_DESCRIPTION))
            } catch (e: JSONException) {
                val data = GsonBuilder().create().fromJson(responseJson, GetUserInfoData::class.java)
                model.getUserInfoDomainData = mappingToDomainData(data)
                model.isSuccess = true
            }
        } else {
            val messageError = ErrorHandler.getErrorMessage(response)
            if (!TextUtils.isEmpty(messageError)) {
                throw ErrorMessageException(messageError)
            } else {
                throw RuntimeException(response.code().toString())
            }
        }
        return model
    }

    private fun mappingToDomainData(data: GetUserInfoData): GetUserInfoDomainData {
        val domainData = GetUserInfoDomainData()
        domainData.age = data.age
        domainData.bday = data.bday
        domainData.setClientId(data.clientId)
        domainData.completion = data.completion
        domainData.isCreatedPassword = data.isCreatedPassword
        domainData.email = data.email
        domainData.setFirstName(data.firstName)
        domainData.fullName = data.fullName
        domainData.gender = data.gender
        domainData.lang = data.lang
        domainData.name = data.name
        domainData.phone = data.phone
        domainData.setPhoneMasked(data.phoneMasked)
        domainData.isPhoneVerified = data.isPhoneVerified
        domainData.profilePicture = data.profilePicture
        domainData.setRegisterDate(data.registerDate)
        domainData.setRoles(data.roles)
        domainData.status = data.status
        domainData.userId = data.userId
        domainData.setCreatePasswordList(data.createPasswordList)
        return domainData
    }

    companion object {
        private const val ERROR = "error"
        private const val ERROR_DESCRIPTION = "error_description"
    }
}