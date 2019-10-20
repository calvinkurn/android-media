package com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.core.gcm.GCMHandler
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
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmItem
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.hotListDetail.HotListDetailResponse
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.di.DaggerHotlistNavComponent
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.di.HotlistNavComponent
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.util.HotlistParamBuilder.Companion.hotlistParamBuilder
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.adapters.CpmAdsAdapter
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.interfaces.CpmTopAdsListener
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.viewmodel.HotlistNavViewModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.android.synthetic.main.fragment_hotlist_nav.*
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

    lateinit var hotlistnavViewModel: HotlistNavViewModel

    lateinit var component: HotlistNavComponent
    private lateinit var userSession: UserSession
    private lateinit var gcmHandler: GCMHandler
    private lateinit var quickFilterAdapter: QuickFilterAdapter
    private lateinit var cpmAdsAdapter: CpmAdsAdapter


    var productNavListAdapter: ProductNavListAdapter? = null
    var productList: ArrayList<Visitable<ProductTypeFactory>> = ArrayList()
    var CpmList: ArrayList<CpmItem> = ArrayList()
    private lateinit var productTypeFactory: ProductTypeFactory
    var quickFilterList = ArrayList<Filter>()


    private var hotListId: String = ""

    var pageCount = 0
    var isPagingAllowed: Boolean = true

    private val REQUEST_ACTIVITY_SORT_PRODUCT = 102
    private val REQUEST_ACTIVITY_FILTER_PRODUCT = 103


    var hotListAlias = ""

    companion object {
        private val EXTRA_ALIAS = "extra_alias"
        private val EXTRA_TRACKER_ATTRIBUTION = "EXTRA_TRACKER_ATTRIBUTION"
        private val EXTRA_QUERY_HOTLIST = "EXTRA_QUERY_HOTLIST"

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
        // Inflate the layout for this fragment
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
        hotlistnavViewModel.mHotListDetailResponse.observe(this, Observer {

            when (it) {
                is Success -> {
                    updateData(it.data)
                }

                is Fail -> {

                }
            }

        })


        hotlistnavViewModel.mProductList.observe(this, Observer {

            when (it) {
                is Success -> {
                    if (productNavListAdapter?.isShimmerRunning() == true) {
                        productNavListAdapter?.removeShimmer()
                    }

                    if (it.data.isNotEmpty()) {
                        // showNoDataScreen(false)
                        productList.addAll(it.data as ArrayList<Visitable<ProductTypeFactory>>)
                        productNavListAdapter?.removeLoading()
                        product_recyclerview.adapter?.notifyDataSetChanged()
                        isPagingAllowed = true
                    } else {
                        if (productList.isEmpty()) {
                            //   showNoDataScreen(true)
                        }
                    }
                    hideRefreshLayout()
                    reloadFilter(hotlistParamBuilder.generateDynamicFilterParams(hotListId))
                }

                is Fail -> {
                    productNavListAdapter?.removeLoading()
                    hideRefreshLayout()
                    if (productList.isEmpty()) {
                        // showNoDataScreen(true)
                    }
                    isPagingAllowed = true
                }

            }
        })

        hotlistnavViewModel.mProductCount.observe(this, Observer {
            it?.let {
                setTotalSearchResultCount(it)
                if (!TextUtils.isEmpty(it)) {
                    setQuickFilterAdapter(getString(R.string.result_count_template_text, it))
                } else {
                    setQuickFilterAdapter("")
                }
            }
        })



        hotlistnavViewModel.mDynamicFilterModel.observe(this, Observer {

            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                }

                is Fail -> {
                }
            }
        })


        hotlistnavViewModel.mQuickFilterModel.observe(this, Observer {

            when (it) {
                is Success -> {
                    LoadQuickFilters(it.data as ArrayList)
                }

                is Fail -> {
                }
            }

        })

        hotlistnavViewModel.mCpmTopAdsData.observe(this, Observer {

            when (it) {
                is Success -> {
                    LoadCPM(it.data as ArrayList<CpmItem>)

                }

                is Fail -> {
                    Log.d("dfgchjbkl", "rhfghjk")

                }

            }


        })

    }


    override fun onSwipeToRefresh() {
        reloadData()
    }

    private fun reloadFilter(param: RequestParams) {
        hotlistnavViewModel.fetchDynamicAttribute(param)
    }

    private fun updateData(data: HotListDetailResponse) {
        ImageLoader.LoadImage(hotlist_banner, data.hotlistDetail?.coverImage)
        hotListId = data.hotlistDetail?.filterAttribute?.sc.toString()
        fetchProducts(0)
        reloadFilter(hotlistParamBuilder.generateDynamicFilterParams(hotListId))
        hotlistnavViewModel.fetchQuickFilters(hotlistParamBuilder.generateQuickFilterParams(hotListId))
        hotlistnavViewModel.fetchCpmData(hotlistParamBuilder.generateCpmTopAdsParams("kaos"))
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


        val param = hotlistParamBuilder.GenerateProductListParams(
                hotListId,
                start,
                getUniqueId(),
                getSelectedSort(),
                getSelectedFilter()
        )
        hotlistnavViewModel.fetchProductListing(param)

    }


    private fun initViewModel() {
        activity?.let {
            hotlistnavViewModel = viewModelProvider.get(HotlistNavViewModel::class.java)
        }

    }

    private fun init() {
        userSession = UserSession(activity)
        gcmHandler = GCMHandler(activity)
        hotlistnavViewModel.getHotlistDetail(hotlistParamBuilder.getHotlistDetailParams(hotListAlias))
        attachScrollListener()
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
        hotlistnavViewModel.fetchQuickFilters(hotlistParamBuilder.generateQuickFilterParams(hotListId))

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
        return hotListId
    }

    override fun getFilterRequestCode(): Int {
        return REQUEST_ACTIVITY_FILTER_PRODUCT
    }

    override fun getSortRequestCode(): Int {
        return REQUEST_ACTIVITY_SORT_PRODUCT
    }

    override fun onSortAppliedEvent(selectedSortName: String, sortValue: Int) {
        reloadData()
    }

    override fun getScreenName(): String {
        return "hotlist"
    }

    override fun initInjector() {
        component = DaggerHotlistNavComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
    }


    // product card listener

    override fun onItemClicked(item: ProductsItem, adapterPosition: Int) {
        val intent = getProductIntent(item.id.toString(), item.categoryID.toString())

        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
            startActivityForResult(intent, 1002)
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
            } else {
                addWishlist(productItem.id.toString(), userSession.userId, position)
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

    override fun onListItemImpressionEvent(item: Visitable<Any>, position: Int) {
    }

    override fun wishListEnabledTracker(wishListTrackerUrl: String) {
    }


    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn)
            AuthHelper.getMD5Hash(userSession.userId)
        else
            AuthHelper.getMD5Hash(gcmHandler.registrationId)
    }


    // QuickFilterListener

    override fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            val filter = getSelectedFilter()
            filter[option.key] = option.value
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
        } else {
            val filter = getSelectedFilter()
            filter.remove(option.key)
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
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

    override fun onCpmClicked(trackerUrl: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // share button clicked

    override fun onShareButtonClicked() {

    }


}
