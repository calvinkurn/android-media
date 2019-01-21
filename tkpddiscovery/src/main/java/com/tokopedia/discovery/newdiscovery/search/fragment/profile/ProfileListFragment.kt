package com.tokopedia.discovery.newdiscovery.search.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.search.SearchNavigationListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.ProfileListAdapter
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.ProfileListListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileListViewModel

import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel
import com.tokopedia.profile.di.DaggerProfileListComponent
import javax.inject.Inject

class ProfileListFragment : BaseListFragment<ProfileViewModel, ProfileListAdapter>(),
        ProfileContract.View,
        ProfileListListener,
        FollowActionListener{

    @Inject
    lateinit var presenter: ProfileContract.Presenter

    lateinit var searchNavigationListener: SearchNavigationListener

    var query : String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (userVisibleHint) {
            searchNavigationListener.hideBottomNavigation()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && view != null) {
            searchNavigationListener.hideBottomNavigation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            loadDataFromSavedState(savedInstanceState)
        } else {
            loadDataFromArguments()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_list, container, false)
    }

    override fun onSuccessGetProfileListData(profileListViewModel : ProfileListViewModel) {
        renderList(profileListViewModel.profileModelList, false)
        updateScrollListenerState(profileListViewModel.isHasNextPage)
        activity!!.invalidateOptionsMenu()
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return true
    }

    override fun getAdapterTypeFactory(): ProfileListAdapter {
        return ProfileListAdapter(this)
    }

    override fun onItemClicked(t: ProfileViewModel?) {
    }

    override fun getScreenName(): String {
        return "name"
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
    }

    override fun onSuccessToggleFollow(adapterPosition: Int, isEnabled: Boolean) {
        if (adapter.data.get(adapterPosition) is ProfileViewModel) {
            (adapter.data.get(adapterPosition) as ProfileViewModel).followed = isEnabled
            adapter.notifyItemChanged(adapterPosition)
        }
    }

    override fun onErrorToggleFollow(adapterPosition: Int, errorMessage: String) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun attachNavigationListener(searchNavigationListener: SearchNavigationListener) {
        this.searchNavigationListener = searchNavigationListener
    }

    override fun onFavoriteButtonClicked(adapterPosition: Int, profileModel: ProfileViewModel) {
        //this will call api
        //testing purpose
        onSuccessToggleFollow(adapterPosition, !profileModel.followed)
    }

    companion object {
        private val EXTRA_QUERY = "EXTRA_QUERY"

        fun newInstance(query: String, searchhNavigationListener: SearchNavigationListener): ProfileListFragment {
            val args = Bundle()
            args.putString(EXTRA_QUERY, query)
            val profileListFragment = ProfileListFragment()
            profileListFragment.arguments = args
            profileListFragment.attachNavigationListener(searchhNavigationListener)
            return profileListFragment
        }
    }

    private fun loadDataFromArguments() {
        query = arguments!!.getString(EXTRA_QUERY)
    }

    private fun loadDataFromSavedState(savedInstanceState: Bundle) {
        query = savedInstanceState.getString(EXTRA_QUERY)
    }
}
