package com.tokopedia.discovery.newdiscovery.search.model

import com.tkpd.library.utils.URLParser
import com.tokopedia.discovery.newdiscovery.analytics.SearchConstant
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import java.io.Serializable

class SearchParameterModel(deepLinkUri: String = "") : Serializable {

    private val urlParser = URLParser(deepLinkUri)
    private val searchParameterHashMap = urlParser.paramKeyValueMap

//    fun getSearchParameterHashMap() : Map<String, String> {
//        return searchParameterHashMap
//    }

    fun contains(key: String) : Boolean {
        return searchParameterHashMap.contains(key)
    }

    fun get(key: String) : String? {
        return searchParameterHashMap[key]
    }

    fun getBoolean(key: String) : Boolean {
        val booleanAsString = get(key) ?: return false

        return booleanAsString.toBoolean()
    }

    fun getSearchQuery() : String? {
        return when {
            contains(SearchApiConst.Q) -> get(SearchApiConst.Q)
            contains(SearchApiConst.KEYWORD) -> get(SearchApiConst.KEYWORD)
            else -> ""
        }
    }
}