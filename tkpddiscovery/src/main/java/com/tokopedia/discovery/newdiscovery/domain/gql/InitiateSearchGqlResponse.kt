package com.tokopedia.discovery.newdiscovery.domain.gql

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InitiateSearchGqlResponse(
    @SerializedName("searchProduct")
    @Expose
    val searchProduct : SearchProduct? = SearchProduct()
) {
    data class SearchProduct(
        @SerializedName("redirection")
        @Expose
        val redirection : Redirection? = Redirection(),

        @SerializedName("catalogs")
        @Expose
        val catalogs : List<Catalog>? = listOf()
    ) {
        data class Redirection(
            @SerializedName("redirect_applink")
            @Expose
            val redirectApplink: String? = ""
        )

        data class Catalog(
            @SerializedName("id")
            @Expose
            val id: Int? = 0
        )
    }
}