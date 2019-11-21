package com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.fragment


import android.content.Intent
import android.os.Bundle

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.core.share.DefaultShare
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.BaseCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.ProductNavListAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.QuickFilterAdapter
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.view.fragments.BaseCategorySectionFragment
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.QuickFilterListener
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.analytics.HotlistNavAnalytics.Companion.hotlistNavAnalytics
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmItem
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.hotListDetail.HotListDetailResponse
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.hotListDetail.HotlistDetail
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.di.DaggerHotlistNavComponent
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.di.HotlistNavComponent
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.util.HotlistParamBuilder
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.util.HotlistParamBuilder.Companion.hotlistParamBuilder
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.adapters.CpmAdsAdapter
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.interfaces.CpmTopAdsListener
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.viewmodel.HotlistNavViewModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.HOTLIST_SHARE_MSG
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.android.synthetic.main.fragment_hotlist_nav.*
import kotlinx.android.synthetic.main.layout_nav_no_product.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class HotlistNavFragment : BaseCategorySectionFragment(),
        ProductCardListener,
        BaseCategoryAdapter.OnItemChangeView,
        QuickFilterListener,
        WishListActionListener,
        CpmTopAdsListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var removeWishlistActionUseCase: RemoveWishListUseCase
    @Inject
    lateinit var addWishlistActionUseCase: AddWishListUseCase

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private lateinit var hotlistnavViewModel: HotlistNavViewModel
    private lateinit var component: HotlistNavComponent
    private lateinit var userSession: UserSession
    private lateinit var gcmHandler: GCMHandler
    private lateinit var quickFilterAdapter: QuickFilterAdapter
    private lateinit var cpmAdsAdapter: CpmAdsAdapter

    private var productNavListAdapter: ProductNavListAdapter? = null
    private var productList: ArrayList<Visitable<ProductTypeFactory>> = ArrayList()
    private var CpmList: ArrayList<CpmItem> = ArrayList()
    private lateinit var productTypeFactory: ProductTypeFactory
    private var quickFilterList = ArrayList<Filter>()

    private var hotlistDetail: HotlistDetail? = null
    private var hotlistType: String = ""

    private var pageCount = 0
    private var isPagingAllowed: Boolean = true

    private val REQUEST_ACTIVITY_SORT_PRODUCT = 102
    private val REQUEST_ACTIVITY_FILTER_PRODUCT = 103
    private val REQUEST_PRODUCT_ITEM_CLICK = 1002

    private val HOTLIST_APP_LINK_FORMAT = "tokopedia://product/"
    private val HOTLIST_SHARE_URI = "https://www.tokopedia.com/hot/"

    private val HOTLIST_SCREEN_NAME = "hotlist"

    var hotListAlias = ""

    companion object {
        private val EXTRA_ALIAS = "extra_alias"
        private val EXTRA_TRACKER_ATTRIBUTION = "EXTRA_TRACKER_ATTRIBUTION"

        fun createInstanceUsingAlias(alias: String, trackerAttribution: String): Fragment {
            val fragment = HotlistNavFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_ALIAS, alias)
            bundle.putString(EXTRA_TRACKER_ATTRIBUTION, trackerAttribution)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hotlist_nav, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        arguments?.let {
            hotListAlias = it.getString(EXTRA_ALIAS, "")
        }
        setUpVisibleFragmentListener()
        setUpNavigation()
        initViewModel()
        init()
        observeField()
        setUpAdapter()
    }

    private fun observeField() {
        hotlistnavViewModel.mHotListDetailResponse.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    updateData(it.data)
                }

                is Fail -> {
                    showNoDataScreen(true)
                }
            }

        })


        hotlistnavViewModel.mProductList.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    if (productNavListAdapter?.isShimmerRunning() == true) {
                        productNavListAdapter?.removeShimmer()
                    }

                    if (it.data.isNotEmpty()) {
                        showNoDataScreen(false)
                        productList.addAll(it.data as ArrayList<Visitable<ProductTypeFactory>>)
                        productNavListAdapter?.removeLoading()
                        product_recyclerview.adapter?.notifyDataSetChanged()
                        isPagingAllowed = true
                    } else {
                        if (productList.isEmpty()) {
                            showNoDataScreen(true)
                        }
                    }
                    hideRefreshLayout()
                    reloadFilter(hotlistParamBuilder.generateDynamicFilterParams(hotlistDetail?.filterAttribute?.sc.toString()))
                }

                is Fail -> {
                    productNavListAdapter?.removeLoading()
                    hideRefreshLayout()
                    if (productList.isEmpty()) {
                        showNoDataScreen(true)
                    }
                    isPagingAllowed = true
                }

            }
        })

        hotlistnavViewModel.mProductCount.observe(viewLifecycleOwner, Observer {
            it?.let {
                setTotalSearchResultCount(it)
                if (!TextUtils.isEmpty(it)) {
                    setQuickFilterAdapter(getString(R.string.result_count_template_text, it))
                } else {
                    setQuickFilterAdapter("")
                }
            }
        })



        hotlistnavViewModel.mDynamicFilterModel.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                }
            }
        })


        hotlistnavViewModel.mQuickFilterModel.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    LoadQuickFilters(it.data as ArrayList)
                }
            }
        })

        hotlistnavViewModel.mCpmTopAdsData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    if ((it.data as ArrayList<CpmItem>).size > 0) {
                        cpm_recyclerview.show()
                        LoadCPM(it.data as ArrayList<CpmItem>)
                        hotlistNavAnalytics.eventCpmTopAdsImpression(isUserLoggedIn(),
                                hotlistDetail?.shareFilePath ?: "")
                    } else {
                        cpm_recyclerview.hide()
                    }
                }

                is Fail -> {
                    cpm_recyclerview.hide()
                }

            }
        })

    }


    private fun showNoDataScreen(toShow: Boolean) {
        if (toShow) {
            layout_no_data.show()
            txt_no_data_header.text = resources.getText(R.string.category_nav_product_no_data_title)
            txt_no_data_description.text = resources.getText(R.string.category_nav_product_no_data_description)
        } else {
            layout_no_data.hide()
        }
    }


    override fun onSwipeToRefresh() {
        hotlistnavViewModel.getHotlistDetail(hotlistParamBuilder.getHotlistDetailParams(hotListAlias))
    }

    private fun reloadFilter(param: RequestParams) {
        hotlistnavViewModel.fetchDynamicAttribute(param)
    }

    private fun updateData(data: HotListDetailResponse) {
        showBannerShimmer(false)
        ImageLoader.LoadImage(hotlist_banner, data.hotlistDetail?.coverImage)
        hotlistDetail = data.hotlistDetail
        val hotId = hotlistDetail?.filterAttribute?.sc.toString()
        fetchProducts(0)
        reloadFilter(hotlistParamBuilder.generateDynamicFilterParams(hotId))
        hotlistnavViewModel.fetchQuickFilters(hotlistParamBuilder.generateQuickFilterParams(hotId))
        setHotlistType(hotlistDetail?.filterAttribute?.isCurated, hotlistDetail?.url)
        if (hotlistDetail?.isTopads == true) {
            hotlistnavViewModel.fetchCpmData(hotlistParamBuilder.generateCpmTopAdsParams(hotListAlias))
        } else {
            cpm_recyclerview.hide()
        }
    }

    private fun setHotlistType(isCurated: Boolean?, url: String?) {
        hotlistType = if (isCurated == true) {
            HotlistParamBuilder.HotListType.CURATED.value
        } else if (url != null && url.isNotEmpty()) {
            HotlistParamBuilder.HotListType.URL.value
        } else {
            HotlistParamBuilder.HotListType.KEYWORD.value
        }
    }

    private fun setUpAdapter() {
        productTypeFactory = ProductTypeFactoryImpl(this)
        productNavListAdapter = ProductNavListAdapter(productTypeFactory, productList, this)
        product_recyclerview.adapter = productNavListAdapter
        product_recyclerview.layoutManager = getStaggeredGridLayoutManager()
        productNavListAdapter?.addShimmer()

        cpmAdsAdapter = CpmAdsAdapter(CpmList, this)
        cpm_recyclerview.adapter = cpmAdsAdapter
        cpm_recyclerview.layoutManager = LinearLayoutManager(activity,
                RecyclerView.HORIZONTAL, false)
    }

    private fun fetchProducts(start: Int) {

        if (hotlistDetail?.isTopads == true) {
            val param = hotlistParamBuilder.generateProductListParamsWithTopAds(
                    hotlistDetail?.strFilterAttribute ?: "",
                    start,
                    getUniqueId(),
                    getSelectedSort(),
                    getSelectedFilter()
            )
            hotlistnavViewModel.fetchProductListingWithTopAds(param)
        } else {
            val param = hotlistParamBuilder.generateProductListParamsWithOutTopAds(
                    hotlistDetail?.strFilterAttribute ?: "",
                    start,
                    getUniqueId(),
                    getSelectedSort(),
                    getSelectedFilter()
            )
            hotlistnavViewModel.getProductListWithoutTopAds(param)
        }
    }


    private fun initViewModel() {
        activity?.let {
            hotlistnavViewModel = viewModelProvider.get(HotlistNavViewModel::class.java)
            lifecycle.addObserver(hotlistnavViewModel)
        }

    }

    private fun init() {
        showBannerShimmer(true)
        userSession = UserSession(activity)
        gcmHandler = GCMHandler(activity)
        hotlistnavViewModel.getHotlistDetail(hotlistParamBuilder.getHotlistDetailParams(hotListAlias))
        attachScrollListener()
        setQuickFilterAdapter("")
    }

    private fun showBannerShimmer(isVisible: Boolean) {
        if (isVisible) {
            hotlist_banner.hide()
            banner_shimmer.show()
        } else {
            banner_shimmer.hide()
            hotlist_banner.show()
        }
    }

    private fun attachScrollListener() {
        nested_recycler_view.setOnScrollChangeListener { v: NestedScrollView,
                                                         scrollX: Int,
                                                         scrollY: Int,
                                                         oldScrollX: Int,
                                                         oldScrollY: Int ->

            if (v.getChildAt(v.childCount - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                        scrollY > oldScrollY) {
                    if (isPagingAllowed) {
                        incrementpage()
                        fetchProducts(getPage())
                        productNavListAdapter?.addLoading()
                        isPagingAllowed = false
                    }

                }
            }

        }
    }


    private fun setQuickFilterAdapter(productCount: String) {
        quickFilterAdapter = QuickFilterAdapter(quickFilterList, this, productCount)
        quickfilter_recyclerview.adapter = quickFilterAdapter
        quickfilter_recyclerview.layoutManager = LinearLayoutManager(activity,
                RecyclerView.HORIZONTAL, false)
    }


    private fun LoadQuickFilters(list: ArrayList<Filter>) {
        quickFilterList.clear()
        quickFilterList.addAll(list)
        quickfilter_recyclerview.adapter?.notifyDataSetChanged()

    }

    private fun LoadCPM(list: ArrayList<CpmItem>) {
        CpmList.clear()
        CpmList.addAll(list)
        cpm_recyclerview.adapter?.notifyDataSetChanged()
    }


    // BaseCategorySectionFragment methods

    override fun getAdapter(): BaseCategoryAdapter? {
        return productNavListAdapter
    }

    override fun reloadData() {
        if (productNavListAdapter == null) {
            return
        }
        showRefreshLayout()
        productNavListAdapter?.clearData()
        productNavListAdapter?.addShimmer()
        resetPage()
        fetchProducts(getPage())
        hotlistnavViewModel.fetchQuickFilters(hotlistParamBuilder.generateQuickFilterParams(hotlistDetail?.filterAttribute?.sc.toString()))

    }

    private fun getPage(): Int {
        return pageCount
    }

    private fun incrementpage() {
        pageCount += 1
    }

    private fun resetPage() {
        isPagingAllowed = true
        pageCount = 0
    }

    override fun getDepartMentId(): String {
        return hotlistDetail?.filterAttribute?.sc.toString()
    }

    override fun getFilterRequestCode(): Int {
        return REQUEST_ACTIVITY_FILTER_PRODUCT
    }

    override fun getSortRequestCode(): Int {
        return REQUEST_ACTIVITY_SORT_PRODUCT
    }

    override fun onSortAppliedEvent(selectedSortName: String, sortValue: Int) {
        hotlistNavAnalytics.eventSortApplied(hotListAlias,
                isUserLoggedIn(),
                selectedSortName,
                sortValue)
    }

    override fun getScreenName(): String {
        return HOTLIST_SCREEN_NAME
    }

    override fun initInjector() {
        component = DaggerHotlistNavComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
    }


    // product card listener

    override fun onItemClicked(item: ProductsItem, adapterPosition: Int) {
        hotlistNavAnalytics.eventProductClicked(hotListAlias,
                HOTLIST_APP_LINK_FORMAT + item.category,
                isUserLoggedIn(),
                hotlistType,
                adapterPosition,
                item.isTopAds,
                item.name,
                item.id.toString(),
                item.price, item.categoryBreadcrumb + " / " + item.category)

        val intent = getProductIntent(item.id.toString(), item.categoryID.toString())
        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
            startActivityForResult(intent, REQUEST_PRODUCT_ITEM_CLICK)
        }
        if (item.isTopAds) {
            ImpresionTask().execute(item.productClickTrackingUrl)
        }
    }

    private fun getProductIntent(productId: String, warehouseId: String): Intent? {
        if (context == null) {
            return null
        }

        return if (!TextUtils.isEmpty(warehouseId)) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID, productId, warehouseId)
        } else {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        }
    }

    override fun onLongClick(item: ProductsItem, adapterPosition: Int) {
    }

    override fun onWishlistButtonClicked(productItem: ProductsItem, position: Int) {
        if (userSession.isLoggedIn) {
            disableWishlistButton(productItem.id.toString())
            if (productItem.wishlist) {
                removeWishlist(productItem.id.toString(), userSession.userId, position)
                hotlistNavAnalytics.eventWishistClicked(hotListAlias,
                        hotlistType,
                        isUserLoggedIn(),
                        productItem.isTopAds,
                        productItem.id.toString(),
                        false)
            } else {
                addWishlist(productItem.id.toString(), userSession.userId, position)
                hotlistNavAnalytics.eventWishistClicked(hotListAlias,
                        hotlistType,
                        isUserLoggedIn(),
                        productItem.isTopAds,
                        productItem.id.toString(),
                        true)
            }
        } else {
            launchLoginActivity(productItem.id.toString())
        }
    }

    override fun onProductImpressed(item: ProductsItem, adapterPosition: Int) {
    }

    // on item change view

    override fun onChangeList() {
        product_recyclerview.requestLayout()
    }

    override fun onChangeDoubleGrid() {
        product_recyclerview.requestLayout()
    }

    override fun onChangeSingleGrid() {
        product_recyclerview.requestLayout()
    }

    override fun onListItemImpressionEvent(viewedProductList: List<Visitable<Any>>, viewedTopAdsList: List<Visitable<Any>>) {
        hotlistNavAnalytics.eventProductImpression(hotListAlias,
                isUserLoggedIn(),
                hotlistType,
                viewedProductList,
                viewedTopAdsList)
    }

    override fun wishListEnabledTracker(wishListTrackerUrl: String) {
    }


    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn)
            AuthHelper.getMD5Hash(userSession.userId)
        else
            AuthHelper.getMD5Hash(gcmHandler.registrationId)
    }

    private fun isUserLoggedIn(): Boolean {
        return userSession.isLoggedIn
    }


    // QuickFilterListener

    override fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            val filter = getSelectedFilter()
            filter[option.key] = option.value
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()

            hotlistNavAnalytics.eventQuickFilterClicked(hotListAlias,
                    isUserLoggedIn(),
                    option,
                    true)
        } else {
            val filter = getSelectedFilter()
            filter.remove(option.key)
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()

            hotlistNavAnalytics.eventQuickFilterClicked(hotListAlias,
                    isUserLoggedIn(),
                    option,
                    false)
        }
    }

    override fun isQuickFilterSelected(option: Option): Boolean {
        return getSelectedFilter().containsKey(option.key)
    }

    //wishlist action listener

    override fun onErrorAddWishList(errorMessage: String?, productId: String) {
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessAddWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), true)
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.msg_add_wishlist))
    }

    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String) {
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), false)
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.msg_remove_wishlist))
    }


    fun enableWishlistButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId?.toInt() ?: 0, true)
    }

    fun disableWishlistButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId?.toInt() ?: 0, false)
    }

    private fun removeWishlist(productId: String, userId: String, adapterPosition: Int) {
        removeWishlistActionUseCase.createObservable(productId,
                userId, this)
    }

    private fun addWishlist(productId: String, userId: String, adapterPosition: Int) {
        addWishlistActionUseCase.createObservable(productId, userId,
                this)

    }

    private fun launchLoginActivity(productId: String) {
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

    // CPM click listner

    override fun onCpmClicked(trackerUrl: String, item: CpmItem) {
        if (item.is_product) {
            hotlistNavAnalytics.eventCpmTopAdsProductClick(isUserLoggedIn(),
                    hotlistDetail?.shareFilePath ?: "")
        } else {
            hotlistNavAnalytics.eventCpmTopAdsShopClick(isUserLoggedIn(),
                    hotlistDetail?.shareFilePath ?: "")
        }
        RouteManager.route(activity, trackerUrl)
    }

    override fun onCpmImpression(item: CpmItem) {
    }


    // share button clicked

    override fun onShareButtonClicked() {
        hotlistNavAnalytics.eventShareClicked(isUserLoggedIn(),
                hotlistDetail?.shareFilePath ?: "",
                "")


        val remoteConfig = FirebaseRemoteConfigImpl(activity)
        val hotlistShareMsg = remoteConfig.getString(HOTLIST_SHARE_MSG)
        val shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(LinkerData.DISCOVERY_TYPE)
                .setName(getString(R.string.message_share_catalog))
                .setTextContent(hotlistShareMsg)
                .setCustMsg(hotlistShareMsg)
                .setUri(HOTLIST_SHARE_URI + hotlistDetail?.aliasKey)
                .setId(hotlistDetail?.aliasKey)
                .build()

        shareData.type = LinkerData.HOTLIST_TYPE
        DefaultShare(activity, shareData).show()

    }
}
