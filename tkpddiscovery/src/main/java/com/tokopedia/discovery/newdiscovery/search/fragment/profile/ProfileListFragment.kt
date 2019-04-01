package com.tokopedia.discovery.newdiscovery.search.fragment.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.core.analytics.AppScreen
import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener
import com.tokopedia.discovery.newdiscovery.search.SearchNavigationListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.EmptySearchProfileModel
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.ProfileListTypeFactoryImpl
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.ProfileListListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileListViewModel

import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.TotalSearchCountViewModel
import com.tokopedia.profile.di.DaggerProfileListComponent
import com.tokopedia.user.session.UserSessionInterface
import java.text.DecimalFormat
import javax.inject.Inject

class ProfileListFragment : BaseListFragment<ProfileViewModel, ProfileListTypeFactoryImpl>(),
        ProfileContract.View,
        ProfileListListener,
        FollowActionListener{

    private val PARAM_USER_ID = "{user_id}"

    @Inject
    lateinit var presenter: ProfileContract.Presenter

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    lateinit var searchNavigationListener: SearchNavigationListener

    lateinit var redirectionListener: RedirectionListener

    var totalProfileCount : Int = Integer.MAX_VALUE

    var isHasNextPage : Boolean = isLoadMoreEnabledByDefault

    var query : String = ""

    var nextPage : Int = 1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            onSwipeRefresh()
        }
        if (userVisibleHint && ::searchNavigationListener.isInitialized) {
            searchNavigationListener.hideBottomNavigation()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && view != null && ::searchNavigationListener.isInitialized) {
            searchNavigationListener.hideBottomNavigation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            loadDataFromSavedState(savedInstanceState)
        } else if (arguments != null) {
            loadDataFromSavedState(arguments!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_list, container, false)
    }

    override fun onSuccessGetProfileListData(profileListViewModel : ProfileListViewModel) {
        if (profileListViewModel.getListTrackingObject().isNotEmpty()) {
            SearchTracking.eventUserImpressionProfileResultInTabProfile(
                    context,
                    profileListViewModel.getListTrackingObject(),
                    query
            )
        }

        totalProfileCount = profileListViewModel.totalSearchCount
        renderList(profileListViewModel.profileModelList, profileListViewModel.isHasNextPage)
    }

    override fun renderList(list: List<ProfileViewModel>, hasNextPage: Boolean) {
        hideLoading()
        if (isLoadingInitialData) {
            clearAllData()
        } else {
            adapter.clearAllNonDataElement()
        }
        if (nextPage == 1 && totalProfileCount != 0) {
            adapter.addElement(TotalSearchCountViewModel(createTotalCountText(totalProfileCount)))
        }
        adapter.addElement(list)
        updateScrollListenerState(hasNextPage)

        if (isListEmpty) {
            adapter.addElement(emptyDataViewModel)
        } else {
            isLoadingInitialData = false
        }
    }

    fun createTotalCountText(totalCount : Int) : String {
        val formatter = DecimalFormat("#,###,###")
        return formatter.format(totalCount)
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return true
    }

    override fun getAdapterTypeFactory(): ProfileListTypeFactoryImpl {
        return ProfileListTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: ProfileViewModel?) {
    }

    override fun getScreenName(): String {
        return SCREEN_SEARCH_PAGE_PROFILE_TAB
    }

    override fun initInjector() {
        DaggerProfileListComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).getBaseAppComponent())
                .build()
                .inject(this)
        presenter.attachView(this)
        presenter.attachFollowActionListener(this)
    }

    override fun loadData(page: Int) {
        presenter.requestProfileListData(query, page)
        this.nextPage = page
    }

    override fun onSuccessToggleFollow(adapterPosition: Int, isEnabled: Boolean) {
        if (adapter.data.get(adapterPosition) is ProfileViewModel) {
            (adapter.data.get(adapterPosition) as ProfileViewModel).followed = isEnabled
            adapter.notifyItemChanged(adapterPosition)
        }
    }

    override fun onErrorToggleFollow(adapterPosition: Int, errorMessage: String) {
        NetworkErrorHelper.showSnackbar(activity)
    }

    override fun attachNavigationListener(searchNavigationListener: SearchNavigationListener) {
        this.searchNavigationListener = searchNavigationListener
    }

    override fun attachRedirectionListener(redirectionListener: RedirectionListener) {
        this.redirectionListener = redirectionListener
    }

    override fun onFollowButtonClicked(adapterPosition: Int, profileModel: ProfileViewModel) {
        SearchTracking.eventClickFollowActionProfileResultProfileTab(
                context,
                query,
                !profileModel.followed,
                profileModel.name,
                profileModel.id,
                adapterPosition
        )

        when (userSessionInterface.isLoggedIn) {
            true -> {
                presenter.handleFollowAction(
                        adapterPosition,
                        profileModel.id.toInt(),
                        profileModel.followed)
            }
            false -> {
                launchLoginPage()
            }
        }
    }

    companion object {
        private val EXTRA_QUERY = "EXTRA_QUERY"
        private const val SCREEN_SEARCH_PAGE_PROFILE_TAB = "Search result - Profile tab"

        fun newInstance(query: String,
                        searchhNavigationListener: SearchNavigationListener,
                        redirectionListener: RedirectionListener): ProfileListFragment {
            val args = Bundle()
            args.putString(EXTRA_QUERY, query)
            val profileListFragment = ProfileListFragment()
            profileListFragment.arguments = args
            profileListFragment.attachNavigationListener(searchhNavigationListener)
            profileListFragment.attachRedirectionListener(redirectionListener)
            return profileListFragment
        }
    }

    private fun loadDataFromArguments() {
        query = arguments!!.getString(EXTRA_QUERY) ?: ""
    }

    private fun loadDataFromSavedState(savedInstanceState: Bundle) {
        query = savedInstanceState.getString(EXTRA_QUERY)?:""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_QUERY, query)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        SearchTracking.eventSearchNoResult(activity, query, screenName, mapOf())

        return createProfileEmptySearchModel(
                context!!,
                query,
                getString(R.string.title_profile)
        )
    }

    private fun createProfileEmptySearchModel(context: Context, query: String, sectionTitle: String): EmptySearchProfileModel {
        val emptySearchModel = EmptySearchProfileModel()
        emptySearchModel.imageRes = R.drawable.ic_empty_search
        emptySearchModel.title = getEmptySearchTitle(context, sectionTitle)
        emptySearchModel.content = String.format(context.getString(R.string.empty_search_content_template), query)
        emptySearchModel.buttonText = context.getString(R.string.empty_search_button_text)
        return emptySearchModel
    }

    private fun getEmptySearchTitle(context: Context, sectionTitle: String): String {
        val templateText = context.getString(R.string.msg_empty_search_with_filter_1)
        return String.format(templateText, sectionTitle).toLowerCase()
    }

    override fun onErrorGetProfileListData(e: Throwable) {
        hideLoading()

        if (!adapter.isContainData) {
            NetworkErrorHelper.showEmptyState(activity, view) { loadData(nextPage) }
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity) { loadData(nextPage) }.showRetrySnackbar()
        }
    }

    override fun onEmptyButtonClicked() {
        SearchTracking.eventUserClickNewSearchOnEmptySearch(context, screenName)
        redirectionListener.showSearchInputView()
    }

    override fun onBannerAdsClicked(appLink: String?) {

    }

    override fun onSelectedFilterRemoved(uniqueId: String?) {

    }

    override fun isUserHasLogin(): Boolean {
        return userSessionInterface.isLoggedIn
    }

    override fun getUserId(): String {
        return userSessionInterface.userId
    }

    override fun launchLoginPage() {
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

    override fun onHandleProfileClick(profileModel: ProfileViewModel) {
        SearchTracking.eventUserClickProfileResultInTabProfile(
                context,
                listOf(profileModel.getTrackingObject()),
                query
                )

        launchProfilePage(profileModel.id)
    }

    override fun launchProfilePage(userId : String) {
        val applink : String = ApplinkConst.PROFILE.replace(PARAM_USER_ID, userId)

        if(isActivityAnApplinkRouter()) {
            handleItemClickedIfActivityAnApplinkRouter(applink, false)
        }
    }

    private fun isActivityAnApplinkRouter(): Boolean {
        return activity != null && activity!!.applicationContext is ApplinkRouter
    }

    private fun handleItemClickedIfActivityAnApplinkRouter(applink: String, shouldFinishActivity: Boolean) {
        val router = activity!!.applicationContext as ApplinkRouter
        if (router.isSupportApplink(applink)) {
            handleRouterSupportApplink(router, applink, shouldFinishActivity)
        }
    }

    private fun handleRouterSupportApplink(router: ApplinkRouter, applink: String, shouldFinishActivity: Boolean) {
        finishActivityIfRequired(shouldFinishActivity)
        router.goToApplinkActivity(activity, applink)
    }

    private fun finishActivityIfRequired(shouldFinishActivity: Boolean) {
        if (shouldFinishActivity)
            activity?.finish()
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }
}
