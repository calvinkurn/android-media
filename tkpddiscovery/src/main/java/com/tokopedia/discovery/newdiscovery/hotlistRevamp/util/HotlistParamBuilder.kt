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

    enum class HotListType(public val value: String) {
        CURATED("Curated"),
        URL("Url"),
        KEYWORD("Keyword")
    }

    fun getHotlistDetailParams(productKey: String): RequestParams {
        val requestParam = RequestParams.create()
        requestParam.putString(PARAM_PRODUCT_KEY, productKey)
        return requestParam
    }

    fun generateProductListParams(strFilterAttribute: String,
                                  start: Int,
                                  uniqueId: String,
                                  selectedSort: HashMap<String, String>,
                                  selectedFilter: HashMap<String, String>): RequestParams {
        val param = RequestParams.create()
        param.putString("product_params", prepareProductListParams(strFilterAttribute,
                start * 10,
                10,
                uniqueId,
                createParametersForQuery(selectedSort),
                createParametersForQuery(selectedFilter))
        )
        param.putString("top_params", preparetopAdsParams(strFilterAttribute,
                start,
                createParametersForQuery(selectedSort),
                createParametersForQuery(selectedFilter)))
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

    private fun prepareProductListParams(strFilterAttribute: String, start: Int, rows: Int, unique_id: String, sortParam: String, filterParam: String): String {
        var param = "$strFilterAttribute&device=android&start=$start&rows=$rows&unique_id=$unique_id"
        if (filterParam.isNotEmpty()) {
            param = "$param&$filterParam"
        }
        if (sortParam.isNotEmpty()) {
            param = "$param&$sortParam"
        }
        if (param.isNotEmpty()) {
            param = "$param&safe_search=false"
        }
        return param
    }

    private fun preparetopAdsParams(strFilterAttribute: String, page: Int, sortParam: String, filterParam: String): String {
        var param = "$strFilterAttribute&device=android&page=$page&item=2&ep=product&fshop=1&src=directory"
        if (filterParam.isNotEmpty()) {
            param = "$param&$filterParam"
        }
        if (sortParam.isNotEmpty()) {
            param = "$param&$sortParam"
        }
        if (param.isNotEmpty()) {
            param = "$param&safe_search=false"
        }
        return param
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }

}