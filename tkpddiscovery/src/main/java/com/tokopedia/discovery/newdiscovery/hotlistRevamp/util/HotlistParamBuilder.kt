package com.tokopedia.discovery.newdiscovery.hotlistRevamp.util

import com.tokopedia.discovery.categoryrevamp.data.filter.DAFilterQueryType
import com.tokopedia.discovery.categoryrevamp.utils.ParamMapToUrl
import com.tokopedia.usecase.RequestParams

class HotlistParamBuilder {

    companion object {
        val hotlistParamBuilder: HotlistParamBuilder by lazy { HotlistParamBuilder() }
    }

    val PARAM_PRODUCT_KEY = "productKey"
    val KEY_PARAMS = "params"
    val START = "start"
    val SC = "sc"
    val DEVICE = "device"
    val SOURCE = "source"
    val UNIQUE_ID = "unique_id"
    val ROWS = "rows"
    val KEY_SAFE_SEARCH = "safe_search"
    val OB = "ob"
    val Q = "q"
    val KEY_PAGE = "page"
    val KEY_EP = "ep"
    val KEY_ITEM = "item"
    val KEY_F_SHOP = "fshop"
    val KEY_DEPT_ID = "dep_id"
    val KEY_SRC = "src"
    val QUERY = "query"
    val FILTER = "filter"
    val KEY_TEMPLATE_ID = "template_id"

    fun getHotlistDetailParams(productKey: String): RequestParams {
        val requestParam = RequestParams.create()
        requestParam.putString(PARAM_PRODUCT_KEY, productKey)
        return requestParam
    }

    fun GenerateProductListParams(hotId: String,
                                  start: Int,
                                  uniqueId: String,
                                  selectedSort: HashMap<String, String>,
                                  selectedFilter: HashMap<String, String>): RequestParams {
        val param = RequestParams.create()


        val searchProductRequestParams = RequestParams.create()
        searchProductRequestParams.putString(START, (start * 10).toString())
        searchProductRequestParams.putString(SC, hotId)
        searchProductRequestParams.putString(DEVICE, "android")
        searchProductRequestParams.putString(UNIQUE_ID, uniqueId)
        searchProductRequestParams.putString(KEY_SAFE_SEARCH, "false")
        searchProductRequestParams.putString(ROWS, "10")
        searchProductRequestParams.putString(SOURCE, "search_product")
        searchProductRequestParams.putAllString(selectedSort)
        searchProductRequestParams.putAllString(selectedFilter)
        param.putString("product_params", createParametersForQuery(searchProductRequestParams.parameters))


        val topAdsRequestParam = RequestParams.create()
        topAdsRequestParam.putString(KEY_SAFE_SEARCH, "false")
        topAdsRequestParam.putString(DEVICE, "android")
        topAdsRequestParam.putString(KEY_SRC, "directory")
        topAdsRequestParam.putString(KEY_PAGE, start.toString())
        topAdsRequestParam.putString(KEY_EP, "product")
        topAdsRequestParam.putString(KEY_ITEM, "2")
        topAdsRequestParam.putString(KEY_F_SHOP, "1")
        topAdsRequestParam.putString(KEY_DEPT_ID, hotId)

        topAdsRequestParam.putAllString(selectedSort)

        param.putString("top_params", createParametersForQuery(topAdsRequestParam.parameters))
        return param

    }

    fun generateDynamicFilterParams(hotlistId: String): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = hotlistId
        paramMap.putString(SOURCE, "search_product")
        paramMap.putObject(FILTER, daFilterQueryType)
        paramMap.putString(Q, "")
        paramMap.putString(SOURCE, "directory")
        return paramMap
    }


    fun generateQuickFilterParams(hotlistId: String): RequestParams {
        val quickFilterParam = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = hotlistId
        quickFilterParam.putObject(FILTER, daFilterQueryType)
        quickFilterParam.putString(SOURCE, "quick_filter")
        return quickFilterParam
    }

    fun generateCpmTopAdsParams(queryItem: String): RequestParams {
        val cpmParams = RequestParams()
        val param = RequestParams.create()
        cpmParams.putString(DEVICE, "android")
        cpmParams.putString(KEY_SRC, "hotlist")
        cpmParams.putString(KEY_ITEM, "1")
        cpmParams.putString(KEY_EP, "headline")
        cpmParams.putString(KEY_TEMPLATE_ID, "3")
        cpmParams.putString(KEY_PAGE, "1")
        cpmParams.putString(Q, queryItem)
        param.putString(KEY_PARAMS, createParametersForQuery(cpmParams.parameters))
        return param
    }

    fun prepareFirstProductListParams(strFilterAttribute: String, start: Int, rows: Int): String {
        return strFilterAttribute + "device=android&start=$start&rows=$rows"
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }

}