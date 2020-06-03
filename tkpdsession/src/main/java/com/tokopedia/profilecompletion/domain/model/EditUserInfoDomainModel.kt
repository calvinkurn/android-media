package com.tokopedia.profilecompletion.domain.model

/**
 * @author by nisie on 7/3/17.
 */
data class EditUserInfoDomainModel (
    var isSuccess: Boolean = false,
    var errorMessage: String = "",
    var statusMessage: String = "",
    var responseCode: Int = 0
)