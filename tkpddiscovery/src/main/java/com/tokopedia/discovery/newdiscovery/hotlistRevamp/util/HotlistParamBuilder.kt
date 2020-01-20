package com.tokopedia.discovery.newdiscovery.hotlistRevamp.util

import com.tokopedia.discovery.categoryrevamp.data.filter.DAFilterQueryType
import com.tokopedia.discovery.categoryrevamp.utils.ParamMapToUrl
import com.tokopedia.usecase.RequestParams

class HotlistParamBuilder {

    companion object {
        private const val PARAM_PRODUCT_KEY = "productKey"
        private const val START = "start"
        private const val DEVICE = "device"
        private const val SOURCE = "source"
        private const val UNIQUE_ID = "unique_id"
        private const val ROWS = "rows"
        private const val KEY_SAFE_SEARCH = "safe_search"
        private const val KEY_QUERY = "q"
        private const val KEY_PAGE = "page"
        private const val KEY_EP = "ep"
        private const val KEY_ITEM = "item"
        private const val KEY_F_SHOP = "fshop"
        private const val KEY_SRC = "src"
        private const val FILTER = "filter"
        private const val KEY_TEMPLATE_ID = "template_id"


        private const val KEY_PRODUCT_PARAMS = "product_params"
        private const val KEY_TOP_PARAMS = "top_params"
        private const val KEY_PARAMS = "params"
        private const val KEY_SRC_HOTLIST = "hotlist"

        private const val DEVICE_TYPE = "android"
        private const val ITEMS_PER_PAGE = 10
        private const val CPM_ADS_PER_PAGE = 1
        private const val TOP_ADS_PER_PAGE = "2"
        private const val CPM_TEMPLATE_ID = "3"

        val hotlistParamBuilder: HotlistParamBuilder by lazy { HotlistParamBuilder() }
    }

    enum class HotListType(val value: String) {
        CURATED("Curated"),
        URL("Url"),
        KEYWORD("Keyword")
    }

    enum class SourceType(val value: String) {
        DIRECTORY("directory"),
        QUICK_FILTER("quick_filter")
    }

    enum class EpType(val value: String) {
        PRODUCT("product"),
        HEADLINE("headline")
    }

    fun getHotlistDetailParams(productKey: String): RequestParams {
        val requestParam = RequestParams.create()
        requestParam.putString(PARAM_PRODUCT_KEY, productKey)
        return requestParam
    }

    fun generateProductListParamsWithTopAds(strFilterAttribute: String,
                                            start: Int,
                                            uniqueId: String,
                                            selectedSort: HashMap<String, String>,
                                            selectedFilter: HashMap<String, String>): RequestParams {
        val param = RequestParams.create()
        param.putString(KEY_PRODUCT_PARAMS, prepareProductListParams(strFilterAttribute,
                start * ITEMS_PER_PAGE,
                ITEMS_PER_PAGE,
                uniqueId,
                createParametersForQuery(selectedSort),
                createParametersForQuery(selectedFilter))
        )
        param.putString(KEY_TOP_PARAMS, preparetopAdsParams(strFilterAttribute,
                start,
                createParametersForQuery(selectedSort),
                createParametersForQuery(selectedFilter)))
        return param
    }

    fun generateProductListParamsWithOutTopAds(strFilterAttribute: String,
                                               start: Int,
                                               uniqueId: String,
                                               selectedSort: HashMap<String, String>,
                                               selectedFilter: HashMap<String, String>): RequestParams {
        val param = RequestParams.create()
        param.putString(KEY_PARAMS, prepareProductListParams(strFilterAttribute,
                start * ITEMS_PER_PAGE,
                ITEMS_PER_PAGE,
                uniqueId,
                createParametersForQuery(selectedSort),
                createParametersForQuery(selectedFilter))
        )
        return param

    }

    fun generateDynamicFilterParams(hotlistId: String): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = hotlistId
        paramMap.putObject(FILTER, daFilterQueryType)
        paramMap.putString(KEY_QUERY, "")
        paramMap.putString(SOURCE, SourceType.DIRECTORY.value)
        return paramMap
    }


    fun generateQuickFilterParams(hotlistId: String): RequestParams {
        val quickFilterParam = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = hotlistId
        quickFilterParam.putObject(FILTER, daFilterQueryType)
        quickFilterParam.putString(SOURCE, SourceType.QUICK_FILTER.value)
        return quickFilterParam
    }

    fun generateCpmTopAdsParams(queryItem: String): RequestParams {
        val cpmParams = RequestParams()
        val param = RequestParams.create()
        cpmParams.putString(DEVICE, DEVICE_TYPE)
        cpmParams.putString(KEY_SRC, KEY_SRC_HOTLIST)
        cpmParams.putString(KEY_ITEM, CPM_ADS_PER_PAGE.toString())
        cpmParams.putString(KEY_EP, EpType.HEADLINE.value)
        cpmParams.putString(KEY_TEMPLATE_ID, CPM_TEMPLATE_ID)
        cpmParams.putString(KEY_PAGE, CPM_ADS_PER_PAGE.toString())
        cpmParams.putString(KEY_QUERY, queryItem)
        param.putString(KEY_PARAMS, createParametersForQuery(cpmParams.parameters))
        return param
    }

    private fun prepareProductListParams(strFilterAttribute: String, start: Int, rows: Int, unique_id: String, sortParam: String, filterParam: String): String {
        var param: String
        param = "$KEY_SAFE_SEARCH=false"
        if (filterParam.isNotEmpty()) {
            param = "$param&$filterParam"
        }
        if (sortParam.isNotEmpty()) {
            param = "$param&$sortParam"
        }
        param = "$param&$strFilterAttribute&device=$DEVICE_TYPE&$START=$start&$ROWS=$rows&$UNIQUE_ID=$unique_id"

        return param
    }

    private fun preparetopAdsParams(strFilterAttribute: String, page: Int, sortParam: String, filterParam: String): String {
        var param: String
        param = "$KEY_SAFE_SEARCH=false"
        if (filterParam.isNotEmpty()) {
            param = "$param&$filterParam"
        }
        if (sortParam.isNotEmpty()) {
            param = "$param&$sortParam"
        }
        param = "$param&$strFilterAttribute&device=$DEVICE_TYPE&page=$page&item=$TOP_ADS_PER_PAGE&ep=${EpType.PRODUCT.value}&$KEY_F_SHOP=1&src=${SourceType.DIRECTORY.value}"
        return param
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }

}