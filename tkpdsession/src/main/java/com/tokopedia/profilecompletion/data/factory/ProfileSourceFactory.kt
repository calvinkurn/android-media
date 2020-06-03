package com.tokopedia.profilecompletion.data.factory

import android.content.Context
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.network.service.AccountsService
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper
import com.tokopedia.profilecompletion.data.source.CloudEditUserInfoSource
import com.tokopedia.profilecompletion.data.source.CloudGetUserInfoSource

/**
 * @author by nisie on 6/19/17.
 */
class ProfileSourceFactory(private val context: Context,
                           private val accountsService: AccountsService,
                           private val getUserInfoMapper: GetUserInfoMapper,
                           private val editUserInfoMapper: EditUserInfoMapper,
                           private val sessionHandler: SessionHandler) {
    fun createCloudGetUserInfoSource(): CloudGetUserInfoSource {
        return CloudGetUserInfoSource(context, accountsService, getUserInfoMapper, sessionHandler)
    }

    fun createCloudEditUserInfoSource(): CloudEditUserInfoSource {
        return CloudEditUserInfoSource(context, accountsService, editUserInfoMapper)
    }
}