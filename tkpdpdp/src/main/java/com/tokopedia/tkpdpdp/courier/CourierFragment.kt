package com.tokopedia.tkpdpdp.courier

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.tkpdpdp.constant.CacheKey.CACHE_KEY_NAME
import com.tokopedia.tkpdpdp.constant.CacheKey.STATE_ORIENTATION_CHANGED
import com.tokopedia.tkpdpdp.tracking.ProductPageTracking
import java.util.*

class CourierFragment : BaseListFragment<CourierViewData, CourierTypeFactoryImpl>() {

    private lateinit var list: List<CourierViewData>
    private lateinit var localCacheHandler: LocalCacheHandler
    private lateinit var productId: String

    companion object {

        private const val ARGS_LIST: String = "ARGS_LIST"
        private const val ARGS_PRODUCT_ID: String = "ARGS_PRODUCT_ID"

        fun newInstance(productId: String, list: ArrayList<CourierViewData>): Fragment {
            val courierFragment = CourierFragment()
            val bundle = Bundle()
            bundle.putString(ARGS_PRODUCT_ID, productId)
            bundle.putParcelableArrayList(ARGS_LIST, list)
            courierFragment.arguments = bundle
            return courierFragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments!!.getString(ARGS_PRODUCT_ID)
        list = arguments!!.getParcelableArrayList(ARGS_LIST)
        localCacheHandler = LocalCacheHandler(activity, CACHE_KEY_NAME)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.resources?.configuration?.let { setUpByConfiguration(it) }
    }

    override fun loadData(page: Int) {
        renderList(list, false)
    }

    override fun getAdapterTypeFactory(): CourierTypeFactoryImpl {
        return CourierTypeFactoryImpl()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setUpByConfiguration(newConfig)
    }

    private fun setUpByConfiguration(configuration: Configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (localCacheHandler.getBoolean(STATE_ORIENTATION_CHANGED, true).not()) {
                ProductPageTracking.eventPdpOrientationChanged(activity, productId)
                localCacheHandler.putBoolean(STATE_ORIENTATION_CHANGED, java.lang.Boolean.TRUE)
                localCacheHandler.applyEditor()
            }
        }
    }

    override fun onItemClicked(courierViewData: CourierViewData) {

    }

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

}
