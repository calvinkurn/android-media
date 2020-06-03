package com.tokopedia.profilecompletion.data.source

import android.content.Context
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam
import com.tokopedia.network.service.AccountsService
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel
import rx.Observable

/**
 * @author by nisie on 7/3/17.
 */
class CloudEditUserInfoSource(private val context: Context,
                              private val accountsService: AccountsService,
                              private val editUserInfoMapper: EditUserInfoMapper) {
    fun editUserInfo(parameters: TKPDMapParam<String?, Any?>?): Observable<EditUserInfoDomainModel> {
        return accountsService.api
                .editProfile(parameters)
                .map(editUserInfoMapper)
    }
}