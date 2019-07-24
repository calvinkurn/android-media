package com.tokopedia.discovery.newdiscovery.domain.gql

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.data.DynamicFilterModel

class SearchFilterProductGqlResponse {

    @SerializedName("search_filter_product")
    @Expose
    private val dynamicFilterModel = DynamicFilterModel()

    fun getDynamicFilterModel(): DynamicFilterModel {
        return dynamicFilterModel
    }
}