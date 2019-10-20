package com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.tkpd.library.utils.legacy.MethodChecker
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.view.fragments.BaseCategorySectionFragment
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CategoryNavigationListener
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.fragment.HotlistNavFragment
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.analytics.FilterEventTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetListener
import com.tokopedia.filter.widget.BottomSheetFilterView
import kotlinx.android.synthetic.main.activity_hotlist_nav.*
import java.util.*

class HotlistNavActivity : BaseActivity(), CategoryNavigationListener, BottomSheetListener {

    private val EXTRA_HOTLIST_PARAM_URL = "HOTLIST_URL"
    private val EXTRA_HOTLIST_PARAM_QUERY = "EXTRA_HOTLIST_PARAM_QUERY"
    private val EXTRA_HOTLIST_PARAM_ALIAS = "HOTLIST_ALIAS"
    private val EXTRA_HOTLIST_PARAM_TRACKER = "EXTRA_HOTLIST_PARAM_TRACKER"

    private var bottomSheetFilterView: BottomSheetFilterView? = null
    private var searchNavContainer: View? = null
    private lateinit var hotListFragment: Fragment
    private lateinit var alias: String
    private lateinit var hotlistFragment: BaseCategorySectionFragment
    private var visibleFragmentListener: CategoryNavigationListener.VisibleClickListener? = null
    private var navigationListenerList: ArrayList<CategoryNavigationListener.ClickListener> = ArrayList()


    private val STATE_GRID = 1
    private val STATE_LIST = 2
    private val STATE_BIG = 3


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

    private fun initButtons() {

        icon_sort.setOnClickListener {
            visibleFragmentListener?.onSortClick()

        }
        button_sort.setOnClickListener {
            visibleFragmentListener?.onSortClick()
        }

        icon_filter.setOnClickListener {
            visibleFragmentListener?.onFilterClick()
        }

        button_filter.setOnClickListener {
            visibleFragmentListener?.onFilterClick()
        }


        img_display_button.tag = STATE_GRID

        img_display_button.setOnClickListener {

            for (navListener in navigationListenerList) {
                navListener.onChangeGridClick()
            }

            when (img_display_button.tag) {

                STATE_GRID -> {
                    img_display_button.tag = STATE_LIST
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_list_display))
                }

                STATE_LIST -> {
                    img_display_button.tag = STATE_BIG
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_big_display))
                }
                STATE_BIG -> {
                    img_display_button.tag = STATE_GRID
                    img_display_button.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_grid_display))
                }
            }
        }

        img_share_button.setOnClickListener {
            visibleFragmentListener?.onShareButtonClick()
        }
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
        searchNavContainer?.visibility = View.VISIBLE
    }

    private fun prepareView() {
        bottomSheetFilterView?.initFilterBottomSheet(FilterTrackingData(
                FilterEventTracking.Event.CLICK_CATEGORY,
                FilterEventTracking.Category.FILTER_CATEGORY,
                getCategoryId(),
                FilterEventTracking.Category.PREFIX_CATEGORY_PAGE))
    }

    private fun getCategoryId(): String? {
        return ""
    }

    private fun initToolbar(alias: String) {
        action_up_btn.setOnClickListener {
            onBackPressed()
        }
        et_search.text = alias

        layout_search.setOnClickListener {
            moveToAutoCompleteActivity(alias)
        }

        image_button_close.setOnClickListener {
            moveToAutoCompleteActivity("")
        }
    }

    private fun moveToAutoCompleteActivity(departMentName: String) {
        RouteManager.route(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + departMentName)
    }

    private fun attachFragment() {
        intent.extras?.let {
            val url = it.getString(EXTRA_HOTLIST_PARAM_URL, "")
            alias = it.getString(EXTRA_HOTLIST_PARAM_ALIAS, "")
            val searchQuery = it.getString(EXTRA_HOTLIST_PARAM_QUERY, "")
            val trackerAttribution = it.getString(EXTRA_HOTLIST_PARAM_TRACKER, "")

            val Fragment = HotlistNavFragment.createInstanceUsingAlias(alias, trackerAttribution)
            hotlistFragment = Fragment as BaseCategorySectionFragment
            supportFragmentManager.beginTransaction().add(R.id.parent_view,
                    Fragment).commit()

            initToolbar(alias)
        }
    }

    private fun applyFilter(filterParameter: Map<String, String>) {

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
        searchNavContainer?.visibility = View.GONE
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
}
