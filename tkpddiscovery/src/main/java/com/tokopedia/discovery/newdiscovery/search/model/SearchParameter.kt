package com.tokopedia.discovery.newdiscovery.search.model

import android.os.Parcel
import android.os.Parcelable
import com.tkpd.library.utils.URLParser
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import java.io.Serializable

class SearchParameter(private val deepLinkUri: String = "") : Parcelable {

    private var searchParameterHashMap = URLParser(deepLinkUri).paramKeyValueMap

    constructor(parcel: Parcel) : this(parcel.readString() ?: "") {
        parcel.readMap(searchParameterHashMap, String::class.java.classLoader)
    }

    constructor(searchParameter: SearchParameter) : this(searchParameter.deepLinkUri) {
        setSearchParameterHashMap(HashMap(searchParameter.searchParameterHashMap))
    }

    private fun setSearchParameterHashMap(searchParameterHashMap : HashMap<String, String>) {
        this.searchParameterHashMap = searchParameterHashMap
    }

    fun getSearchParameterHashMap() : HashMap<String, String> {
        return searchParameterHashMap
    }

    fun getSearchParameterMap() : Map<String, Any> {
        return searchParameterHashMap as Map<String, Any>
    }

    fun contains(key: String) : Boolean {
        return searchParameterHashMap.contains(key)
    }

    fun set(key: String, value: String) {
        searchParameterHashMap[key] = value
    }

    fun get(key: String) : String {
        return searchParameterHashMap[key] ?: ""
    }

    fun remove(key: String) {
        searchParameterHashMap.remove(key)
    }

    fun getBoolean(key: String) : Boolean {
        return get(key).toBoolean()
    }

    fun getInteger(key: String) : Int {
        return try {
            get(key).toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    fun setSearchQuery(query: String) {
        set(SearchApiConst.Q, query)
    }

    fun getSearchQuery() : String {
        return when {
            contains(SearchApiConst.Q) -> get(SearchApiConst.Q)
            contains(SearchApiConst.KEYWORD) -> get(SearchApiConst.KEYWORD)
            else -> ""
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(deepLinkUri)
        parcel.writeMap(searchParameterHashMap)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchParameter> {
        override fun createFromParcel(parcel: Parcel): SearchParameter {
            return SearchParameter(parcel)
        }

        override fun newArray(size: Int): Array<SearchParameter?> {
            return arrayOfNulls(size)
        }
    }
}

interface SearchParameterOwnerListener : Serializable {
    fun setSearchParameter(searchParameter: SearchParameter)
    fun getSearchParameter() : SearchParameter
}