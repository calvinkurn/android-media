package com.tokopedia.transaction.orders.orderlist.view.activity

import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.os.Bundle
import android.text.TextUtils
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.webview.download.BaseDownloadAppLinkActivity

class WebViewOrderListActivity : BaseDownloadAppLinkActivity() {

    object DeepLinkIntents {
        private const val INSURANCE_EXTENSIONS = "[pdf]"


        private fun getDownlodableExtensions(): String {
            return INSURANCE_EXTENSIONS;
        }
        @DeepLink(ApplinkConst.ORDER_LIST_WEBVIEW)
        @JvmStatic
        fun getOrderListIntent(context: Context, extras: Bundle): Intent {

            var webUrl = extras.getString(
                    KEY_APP_LINK_QUERY_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
            )

            if (TextUtils.isEmpty(webUrl)) {
                webUrl = TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
            }

            return newIntent(context, webUrl, true, getDownlodableExtensions())
        }

    }
}
