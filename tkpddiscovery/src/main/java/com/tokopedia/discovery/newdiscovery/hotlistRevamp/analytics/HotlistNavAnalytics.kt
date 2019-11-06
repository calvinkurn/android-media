package com.tokopedia.discovery.newdiscovery.hotlistRevamp.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.filter.common.data.Option
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

class HotlistNavAnalytics {

    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_LOGIN_TYPE = "loginType"
        private const val KEY_POSITION = "position"
        private const val KEY_ATTRIBUTION = "attribution"
        private const val KEY_ECOMMERCE = "ecommerce"
        private const val KEY_CURRENCY_CODE = "currencyCode"
        private const val KEY_IMPRESSIONS = "impressions"
        private const val KEY_CLICK = "click"
        private const val KEY_ACTION_FIELD = "actionField"
        private const val KEY_PRODUCTS = "products"
        private const val KEY_NAME = "name"
        private const val KEY_ID = "id"
        private const val KEY_PRICE = "price"
        private const val KEY_BRAND = ""
        private const val KEY_LIST = "list"
        private const val KEY_CATEGORY = "category"
        private const val KEY_VARIANT = "variant"
        private const val KEY_EVENT_VIEW_HOTLIST_IRIS = "viewHotlistIris"
        private const val KEY_CLICK_HOTLIST = "clickHotlist"
        private const val CURRENCY_VALUE = "IDR"

        val hotlistNavAnalytics: HotlistNavAnalytics by lazy { HotlistNavAnalytics() }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    // 4

    fun eventCpmTopAdsImpression(isUserLoggedIn: Boolean,
                                 pagePath: String) {

        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_EVENT_VIEW_HOTLIST_IRIS,
                KEY_EVENT_CATEGORY, "hotlist page - $pagePath",
                KEY_EVENT_ACTION, "topads headline impression",
                KEY_EVENT_LABEL, "",
                KEY_LOGIN_TYPE, getLoginType(isUserLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)

    }
    // 5 user click cpm top ad product

    fun eventCpmTopAdsProductClick(isUserLoggedIn: Boolean,
                                   pagePath: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $pagePath",
                KEY_EVENT_ACTION, "topads headline product",
                KEY_EVENT_LABEL, "",
                KEY_LOGIN_TYPE, getLoginType(isUserLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 6 user click cpm topads shop

    fun eventCpmTopAdsShopClick(isUserLoggedIn: Boolean,
                                pagePath: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $pagePath",
                KEY_EVENT_ACTION, "topads headline shop",
                KEY_EVENT_LABEL, "",
                KEY_LOGIN_TYPE, getLoginType(isUserLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 7 user click on social share

    fun eventShareClicked(isUserLoggedIn: Boolean,
                          pagePath: String, share_icon: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $pagePath",
                KEY_EVENT_ACTION, "click social share",
                KEY_EVENT_LABEL, "",
                KEY_LOGIN_TYPE, getLoginType(isUserLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //8 , 9 , 10  skipped for now

    // 11

    fun eventProductClicked(hotlistName: String,
                            applink: String,
                            isLoggedIn: Boolean,
                            hotlistType: String,
                            position: Int,
                            isTopAds: Boolean,
                            name: String,
                            id: String,
                            price: String,
                            path: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, "productClick",
                KEY_EVENT_CATEGORY, "hotlist page",
                KEY_EVENT_ACTION, "click product list",
                KEY_EVENT_LABEL, "keyword: $hotlistName - applink: $applink",
                KEY_LOGIN_TYPE, getLoginType(isLoggedIn),
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_CLICK, DataLayer.mapOf(
                KEY_ACTION_FIELD, "list:/hot/" + hotlistName + " - " + getLoginType(isLoggedIn) + " - " + hotlistType + " - " + position + " - " + getIsTopAdsString(isTopAds),
                KEY_PRODUCTS, DataLayer.listOf(DataLayer.mapOf(
                KEY_NAME, name,
                KEY_ID, id,
                KEY_PRICE, price,
                KEY_BRAND, "",
                KEY_CATEGORY, path,
                KEY_VARIANT, "",
                KEY_POSITION, position,
                KEY_ATTRIBUTION, "")))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 12

    fun eventProductImpression(hotlistName: String,
                               isLoggedIn: Boolean,
                               hotlistType: String,
                               position: Int,
                               isTopAds: Boolean,
                               name: String,
                               id: String,
                               price: String,
                               path: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, "productView",
                KEY_EVENT_CATEGORY, "hotlist page",
                KEY_EVENT_ACTION, "product list impression",
                KEY_EVENT_LABEL, hotlistName,
                KEY_LOGIN_TYPE, getLoginType(isLoggedIn),
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_CURRENCY_CODE, CURRENCY_VALUE,
                KEY_IMPRESSIONS, DataLayer.listOf(DataLayer.mapOf(
                KEY_NAME, name,
                KEY_ID, id,
                KEY_PRICE, price,
                KEY_BRAND, "",
                KEY_CATEGORY, path,
                KEY_VARIANT, "",
                KEY_LIST, "actionField: list:/hot/" + hotlistName + " - " + getLoginType(isLoggedIn) + " - " + hotlistType + " - " + position + " - " + getIsTopAdsString(isTopAds),
                KEY_POSITION, position,
                KEY_ATTRIBUTION, "")))
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


    // 13

    fun eventWishistClicked(hotlistName: String,
                            hotlistType: String,
                            isLoggedIn: Boolean,
                            isTopAds: Boolean,
                            product_id: String,
                            isWishlisted: Boolean) {
        val tracker = getTracker()
        val eventAction: String = if (isWishlisted) {
            "add wishlist"
        } else {
            "remove wishlist"
        }
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $hotlistName",
                KEY_EVENT_ACTION, eventAction + " - " + hotlistType + " - " + getLoginType(isLoggedIn),
                KEY_EVENT_LABEL, product_id + "-" + if (isTopAds) "topads" else "general",
                KEY_LOGIN_TYPE, getLoginType(isLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


    // 14


    fun eventQuickFilterClicked(hotlistName: String,
                                isLoggedIn: Boolean,
                                option: Option,
                                filterValue: Boolean) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $hotlistName",
                KEY_EVENT_ACTION, "click quick filter",
                KEY_EVENT_LABEL, "${option.name}-${option.value}" + "-" + filterValue.toString(),
                KEY_LOGIN_TYPE, getLoginType(isLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 15


    fun eventSortClicked(hotlistName: String,
                         isLoggedIn: Boolean,
                         sortName: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $hotlistName",
                KEY_EVENT_ACTION, "click sort",
                KEY_EVENT_LABEL, sortName,
                KEY_LOGIN_TYPE, getLoginType(isLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 16


    fun eventFilterClicked(hotlistName: String,
                           isLoggedIn: Boolean) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $hotlistName",
                KEY_EVENT_ACTION, "click filter",
                KEY_EVENT_LABEL, "",
                KEY_LOGIN_TYPE, getLoginType(isLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 17 click apply sort

    fun eventSortApplied(hotlistName: String,
                         isLoggedIn: Boolean,
                         sortName: String,
                         sortValue: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $hotlistName - sort hotlist",
                KEY_EVENT_ACTION, "click apply sort",
                KEY_EVENT_LABEL, "$sortName - $sortValue",
                KEY_LOGIN_TYPE, getLoginType(isLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 18

    fun eventFilterApplied(hotlistName: String,
                           isLoggedIn: Boolean,
                           filterName: String,
                           filterValue: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $hotlistName - filter hotlist",
                KEY_EVENT_ACTION, "click apply filter",
                KEY_EVENT_LABEL, "$filterName-$filterValue",
                KEY_LOGIN_TYPE, getLoginType(isLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 19

    fun eventDisplayButtonClicked(hotlistName: String,
                                  isLoggedIn: Boolean,
                                  displayName: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_CLICK_HOTLIST,
                KEY_EVENT_CATEGORY, "hotlist page - $hotlistName",
                KEY_EVENT_ACTION, "click display",
                KEY_EVENT_LABEL, displayName,
                KEY_LOGIN_TYPE, getLoginType(isLoggedIn)
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


    private fun getLoginType(isUserLoggedIn: Boolean): String {
        return if (isUserLoggedIn) {
            "Login"
        } else {
            "Non-Login"
        }
    }

    private fun getIsTopAdsString(isTopAds: Boolean): String {
        return if (isTopAds) {
            "topads"
        } else {
            "non topads"
        }
    }
}