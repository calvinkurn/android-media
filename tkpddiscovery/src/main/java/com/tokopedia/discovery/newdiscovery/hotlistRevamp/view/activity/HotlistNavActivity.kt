package com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tkpd.library.utils.legacy.MethodChecker
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalogrevamp.ui.customview.SearchNavigationView
import com.tokopedia.discovery.categoryrevamp.view.fragments.BaseCategorySectionFragment
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CategoryNavigationListener
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.analytics.HotlistNavAnalytics.Companion.hotlistNavAnalytics
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.fragment.HotlistNavFragment
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener
import com.tokopedia.filter.widget.BottomSheetFilterView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.activity_hotlist_nav.*
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*


class HotlistNavActivity : BaseActivity(),
        CategoryNavigationListener,
        BottomSheetListener,
        SearchNavigationView.SearchNavClickListener,
        BaseCategorySectionFragment.SortAppliedListener {


    private var bottomSheetFilterView: BottomSheetFilterView? = null
    private var searchNavContainer: SearchNavigationView? = null
    private lateinit var hotListFragment: Fragment
    private lateinit var alias: String
    private lateinit var hotlistFragment: BaseCategorySectionFragment
    private var visibleFragmentListener: CategoryNavigationListener.VisibleClickListener? = null
    private var navigationListenerList: ArrayList<CategoryNavigationListener.ClickListener> = ArrayList()


    private val STATE_GRID = 1
    private val STATE_LIST = 2
    private val STATE_BIG = 3
    private val ORDER_BY = "ob"

    enum class ViewStateType(val value: String) {
        GRID("grid"),
        LIST("list"),
        BIG("big")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotlist_nav)
        bottomSheetFilterView = findViewById(R.id.bottomSheetFilter)
        searchNavContainer = findViewById(R.id.search_nav_container)
        prepareView()
        attachFragment()
        initBottomSheetListener()
        initButtons()

    }


    companion object {

        private val EXTRA_HOTLIST_PARAM_URL = "HOTLIST_URL"
        private val EXTRA_HOTLIST_PARAM_QUERY = "EXTRA_HOTLIST_PARAM_QUERY"
        private val EXTRA_HOTLIST_PARAM_ALIAS = "HOTLIST_ALIAS"
        private val EXTRA_HOTLIST_PARAM_TRACKER = "EXTRA_HOTLIST_PARAM_TRACKER"

        @JvmStatic
        fun createInstanceUsingAlias(context: Context,
                                     alias: String,
                                     trackerAttribution: String): Intent {
            val intent = Intent(context, HotlistNavActivity::class.java)
            val extras = Bundle()
            extras.putString(EXTRA_HOTLIST_PARAM_ALIAS, alias)
            try {
                extras.putString(EXTRA_HOTLIST_PARAM_TRACKER, URLDecoder.decode(trackerAttribution, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                extras.putString(EXTRA_HOTLIST_PARAM_TRACKER, trackerAttribution.replace("%20".toRegex(), " "))
            }

            intent.putExtras(extras)
            return intent
        }


        @JvmStatic
        fun isHotlistNavEnabled(context: Context): Boolean {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            return remoteConfig.getBoolean(RemoteConfigKey.APP_HOTLIST_NAV_ENABLE, true)
        }
    }

    private fun initButtons() {
        searchNavContainer?.setSearchNavListener(this)
        img_display_button.tag = STATE_GRID

        img_display_button.setOnClickListener {

            for (navListener in navigationListenerList) {
                navListener.onChangeGridClick()
            }

            when (img_display_button.tag) {

                STATE_GRID -> {
                    hotlistNavAnalytics.eventDisplayButtonClicked(alias,
                            isUserLoggedIn(),
                            ViewStateType.GRID.value)
                    img_display_button.tag = STATE_LIST
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_list_display))
                }

                STATE_LIST -> {
                    hotlistNavAnalytics.eventDisplayButtonClicked(alias,
                            isUserLoggedIn(),
                            ViewStateType.LIST.value)
                    img_display_button.tag = STATE_BIG
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_big_display))
                }
                STATE_BIG -> {
                    hotlistNavAnalytics.eventDisplayButtonClicked(alias,
                            isUserLoggedIn(),
                            ViewStateType.BIG.value)
                    img_display_button.tag = STATE_GRID
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_grid_display))
                }
            }
        }

        img_share_button.setOnClickListener {
            visibleFragmentListener?.onShareButtonClick()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val userSession = UserSession(this)
        return userSession.isLoggedIn
    }


    private fun initBottomSheetListener() {
        bottomSheetFilterView?.setCallback(object : BottomSheetFilterView.Callback {
            override fun onApplyFilter(filterParameter: Map<String, String>) {
                applyFilter(filterParameter)
            }

            override fun onShow() {
                hideBottomNavigation()
            }

            override fun onHide() {
                showBottomNavigation()
                sendBottomSheetHideEvent()
            }

            override fun getActivity(): AppCompatActivity {
                return this@HotlistNavActivity
            }
        })
    }

    private fun sendBottomSheetHideEvent() {
        hotlistFragment.onBottomSheetHide()
    }

    private fun showBottomNavigation() {
        searchNavContainer?.show()
    }

    private fun prepareView() {
        bottomSheetFilterView?.initFilterBottomSheet(FilterTrackingData(
                FilterEventTracking.Event.CLICK_CATEGORY,
                FilterEventTracking.Category.FILTER_CATEGORY,
                "",
                FilterEventTracking.Category.PREFIX_CATEGORY_PAGE))
    }

    private fun initToolbar(alias: String) {
        action_up_btn.setOnClickListener {
            onBackPressed()
        }
        et_search.text = alias.replace("-", " ")

        layout_search.setOnClickListener {
            moveToAutoCompleteActivity(alias.replace("-", " "))
        }

        image_button_close.setOnClickListener {
            moveToAutoCompleteActivity("")
        }
    }

    private fun moveToAutoCompleteActivity(departMentName: String) {
        RouteManager.route(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + departMentName)
    }

    private fun attachFragment() {
        var trackerAttribution = ""
        var searchQuery = ""
        intent.extras?.let {
            if (it.containsKey(EXTRA_HOTLIST_PARAM_URL)) {
                val url = it.getString(EXTRA_HOTLIST_PARAM_URL, "")
                alias = if (url.isEmpty()) {
                    it.getString(EXTRA_HOTLIST_PARAM_ALIAS, "")
                } else {
                    generateAliasUsingURL(url)
                }
                searchQuery = it.getString(EXTRA_HOTLIST_PARAM_QUERY, "")
                trackerAttribution = it.getString(EXTRA_HOTLIST_PARAM_TRACKER, "")
            } else {
                alias = intent.data?.path?.replace("/", "") ?: ""
            }
        } ?: run {
            alias = intent.data?.path?.replace("/", "") ?: ""
        }
        val Fragment = HotlistNavFragment.createInstanceUsingAlias(alias, trackerAttribution)
        hotlistFragment = Fragment as BaseCategorySectionFragment
        supportFragmentManager.beginTransaction().add(R.id.parent_view,
                Fragment).commit()
        hotlistFragment.setSortListener(this)
        initToolbar(alias)

    }


    private fun generateAliasUsingURL(url: String): String {
        val uri = Uri.parse(url)
        return if (uri.pathSegments.size > 1)
            uri.pathSegments[1]
        else ""
    }


    private fun applyFilter(filterParameter: Map<String, String>) {

        val presentFilterList = hotlistFragment.getSelectedFilter()
        if (presentFilterList.size < filterParameter.size) {
            for (i in filterParameter.entries) {
                if (!presentFilterList.containsKey(i.key)) {
                    hotlistNavAnalytics.eventFilterApplied(alias,
                            isUserLoggedIn(),
                            i.key,
                            i.value)
                }
            }
        }
        if (filterParameter.isNotEmpty() && (filterParameter.size > 1 || !filterParameter.containsKey(ORDER_BY))) {
            searchNavContainer?.onFilterSelected(true)
        } else {
            searchNavContainer?.onFilterSelected(false)
        }
        hotlistFragment.applyFilterToSearchParameter(filterParameter)
        hotlistFragment.setSelectedFilter(HashMap(filterParameter))
        hotlistFragment.clearDataFilterSort()
        hotlistFragment.reloadData()
    }

    override fun setupSearchNavigation(clickListener: CategoryNavigationListener.ClickListener) {
        navigationListenerList.add(clickListener)
    }

    override fun setUpVisibleFragmentListener(visibleClickListener: CategoryNavigationListener.VisibleClickListener) {
        visibleFragmentListener = visibleClickListener
    }

    override fun hideBottomNavigation() {
        searchNavContainer?.hide()
    }


    override fun loadFilterItems(filters: ArrayList<Filter>?, searchParameter: MutableMap<String, String>?) {
        bottomSheetFilterView?.loadFilterItems(filters, searchParameter)
    }

    override fun setFilterResultCount(formattedResultCount: String?) {
        bottomSheetFilterView?.setFilterResultCount(formattedResultCount)
    }

    override fun launchFilterBottomSheet() {
        bottomSheetFilterView?.launchFilterBottomSheet()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleDefaultActivityResult(requestCode, resultCode, data)
    }

    private fun handleDefaultActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        bottomSheetFilterView?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        bottomSheetFilterView?.let {
            if (!it.onBackPressed()) {
                finish()
            }
        }
    }

    override fun onSortButtonClicked() {
        visibleFragmentListener?.onSortClick()
        hotlistNavAnalytics.eventSortClicked(alias,
                isUserLoggedIn(),
                "")
    }

    override fun onFilterButtonClicked() {
        visibleFragmentListener?.onFilterClick()

        hotlistNavAnalytics.eventFilterClicked(alias,
                isUserLoggedIn())
    }


    override fun onSortApplied(showTick: Boolean) {
        searchNavContainer?.onSortSelected(showTick)
    }
}
