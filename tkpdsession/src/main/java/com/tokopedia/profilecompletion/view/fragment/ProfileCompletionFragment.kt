package com.tokopedia.profilecompletion.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Pair
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.core.base.di.component.DaggerAppComponent
import com.tokopedia.core.base.di.module.AppModule
import com.tokopedia.core.customView.TextDrawable
import com.tokopedia.profilecompletion.data.pojo.StatusPinData
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionComponent
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenter
import com.tokopedia.profilecompletion.view.util.ProgressBarAnimation
import com.tokopedia.profilecompletion.view.viewmodel.ProfileCompletionViewModel
import com.tokopedia.profilecompletion.viewmodel.PinViewModel
import com.tokopedia.session.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * Created by stevenfredian on 6/22/17.
 */
class ProfileCompletionFragment : BaseDaggerFragment(), ProfileCompletionContract.View {

    var progressBar: ProgressBar? = null
    var viewPager: ViewPager? = null
    var percentText: TextView? = null
    var proceed: TextView? = null
    var progress: View? = null
    var main: View? = null
    var loading: View? = null
    var transaction: FragmentTransaction? = null

    @Inject
    lateinit var presenter: ProfileCompletionPresenter

    @Inject
    lateinit var currentUserSession: UserSession

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModelProvider: ViewModelProvider? = null
    private var pinViewModel: PinViewModel? = null
    private var animation: ProgressBarAnimation? = null

    private var currentData: ProfileCompletionViewModel? = null

    private var filled: String = ""
    private var skip: View? = null
    private var pair: Pair<Int, Int>? = null
    private var retryAction: NetworkErrorHelper.RetryClickedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState?.getParcelable<Parcelable?>(ARGS_DATA) != null) {
            currentData = savedInstanceState.getParcelable(ARGS_DATA)
        }
        viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        pinViewModel = viewModelProvider?.get(PinViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parentView = inflater.inflate(R.layout.fragment_profile_completion, container, false)
        setHasOptionsMenu(true)
        initView(parentView)
        initialVar()
        presenter.attachView(this)
        initObserver()
        return parentView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(Menu.NONE, R.id.action_skip, 0, "")
        val menuItem = menu.findItem(R.id.action_skip) // OR THIS
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = draw
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initObserver() {
        pinViewModel?.getStatusPinResponse?.observe(this, Observer { statusPinDataResult: Result<StatusPinData>? ->
            if (statusPinDataResult is Success<*>) {
                onSuccessGetStatusPin((statusPinDataResult as Success<StatusPinData>).data)
            } else if (statusPinDataResult is Fail) {
                onErrorGetStatusPin(statusPinDataResult.throwable)
            }
        })
    }

    private fun onSuccessGetStatusPin(statusPinData: StatusPinData) {
        loading?.visibility = View.GONE
        if (!statusPinData.isRegistered &&
                currentUserSession.phoneNumber?.isNotEmpty() == true &&
                currentUserSession.isMsisdnVerified) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN_ONBOARDING)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
            startActivityForResult(intent, REQUEST_CODE_PIN)
        } else {
            if (activity != null) {
                activity!!.finish()
            }
        }
    }

    private fun onErrorGetStatusPin(throwable: Throwable) {
        loading?.visibility = View.GONE
        main?.visibility = View.GONE
        NetworkErrorHelper.showEmptyState(activity, view, throwable.message, retryAction)
    }

    private val draw: Drawable
        get() {
            val drawable = TextDrawable(activity)
            drawable.text = resources.getString(R.string.skip_form)
            return drawable
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_skip) {
            skipView(findChildTag())
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView(view: View) {
        progress = view.findViewById(R.id.progress)
        progressBar = view.findViewById(R.id.ProgressBar)
        viewPager = view.findViewById(R.id.viewpager)
        percentText = view.findViewById(R.id.percentText)
        viewPager = view.findViewById(R.id.viewpager)
        proceed = view.findViewById(R.id.proceed)
        skip = view.findViewById(R.id.skip)
        main = view.findViewById(R.id.layout_main)
        loading = view.findViewById(R.id.loading_layout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARGS_DATA, data)
    }

    protected fun initialVar() {
        animation = progressBar?.let { ProgressBarAnimation(it) }
        filled = "filled"
        pair = Pair(R.anim.slide_in_right, R.anim.slide_out_left)
        retryAction = NetworkErrorHelper.RetryClickedListener {
            loading?.visibility = View.VISIBLE
            presenter.userInfo
        }
    }

    override fun onErrorGetUserInfo(string: String?) {
        loading?.visibility = View.GONE
        main?.visibility = View.GONE
        NetworkErrorHelper.showEmptyState(activity, view, string, retryAction)
    }

    private fun updateProgressBar(oldValue: Int, newValue: Int) {
        currentData?.completion = newValue
        animation?.setValue(oldValue, newValue)
        animation?.duration = 500
        progressBar?.startAnimation(animation)
        progressBar?.progress = currentData?.completion?: 0
        percentText?.text = String.format("%s%%", progressBar?.progress.toString())

        val colors = resources.getIntArray(R.array.green_indicator)
        var indexColor = (newValue - 50) / 10
        if (indexColor < 0) {
            indexColor = 0
        }
        val shape = activity?.let {
            ContextCompat.getDrawable(it, R.drawable.horizontal_progressbar)
        } as LayerDrawable?

        val runningBar = (shape?.findDrawableByLayerId(R.id.progress_col) as ScaleDrawable).drawable as GradientDrawable

        runningBar.setColor(colors[indexColor])
        runningBar.mutate()
        progressBar?.progressDrawable = shape
    }

    private fun loadFragment(profileCompletionViewModel: ProfileCompletionViewModel, pair: Pair<Int, Int>?) {
        KeyboardHandler.DropKeyboard(activity, view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            transaction = childFragmentManager.beginTransaction()
        }
        pair?.let { transaction?.setCustomAnimations(pair.first, pair.second) }
        chooseFragment(profileCompletionViewModel)
    }

    private fun chooseFragment(profileCompletionViewModel: ProfileCompletionViewModel) {

        if (checkingIsEmpty(profileCompletionViewModel.gender.toString())) {
            val genderFragment = ProfileCompletionGenderFragment.createInstance(this)
            transaction?.replace(R.id.fragment_container, genderFragment, ProfileCompletionGenderFragment.TAG)?.commit()

        } else if (checkingIsEmpty(profileCompletionViewModel.bday)) {
            val dateFragment = ProfileCompletionDateFragment.createInstance(this)
            transaction?.replace(R.id.fragment_container, dateFragment, ProfileCompletionDateFragment.TAG)?.commit()

        } else if (!profileCompletionViewModel.isPhoneVerified) {
            val verifCompletionFragment = ProfileCompletionPhoneVerificationFragment.createInstance(this)
            transaction?.replace(R.id.fragment_container, verifCompletionFragment, ProfileCompletionPhoneVerificationFragment.TAG)?.commit()

        } else if (profileCompletionViewModel.completion == 100) {
            (activity as ProfileCompletionActivity?)?.onFinishedForm()

        } else {
            loading?.visibility = View.VISIBLE
            pinViewModel?.getStatusPin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PIN) {
            if (activity != null) {
                if (resultCode == Activity.RESULT_OK) {
                    currentData?.completion?.let { updateProgressBar(it, it + 10) }
                    setViewEnabled()
                    currentData?.let { loadFragment(it, pair) }
                }
                activity?.finish()
            }
        }
    }

    private fun checkingIsEmpty(item: String?): Boolean {
        return item == null || item.isEmpty() || item == "0" || item == DEFAULT_EMPTY_BDAY
    }

    override fun getPresenter(): ProfileCompletionContract.Presenter? {
        return presenter
    }

    override fun onSuccessEditProfile(edit: Int) {
        when (edit) {
            EditUserProfileUseCase.EDIT_DOB -> {
                currentData?.bday = filled
                currentData?.completion?.let {
                    updateProgressBar(it, it + 10)
                }
            }
            EditUserProfileUseCase.EDIT_GENDER -> {
                currentData?.gender = 3
                currentData?.completion?.let {
                    updateProgressBar(it, it + 10)
                }
            }
            EditUserProfileUseCase.EDIT_VERIF -> {
                currentData?.isPhoneVerified = true
                presenter.setMsisdnVerifiedToCache(true)
                currentData?.completion?.let {
                    updateProgressBar(it, it + 20) }
            }
        }
        setViewEnabled()
        currentData?.let { loadFragment(it, pair) }
    }

    private fun setViewEnabled() {
        progress?.visibility = View.GONE
        proceed?.visibility = View.VISIBLE
        canProceed(true)
        proceed?.text = getString(R.string.continue_form)
        skip?.isEnabled = true
    }

    override fun disableView() {
        progress?.visibility = View.VISIBLE
        proceed?.visibility = View.GONE
        skip?.isEnabled = false
    }

    override fun canProceed(answer: Boolean) {
        proceed?.isEnabled = answer
        if (answer) {
            proceed?.background?.setColorFilter(MethodChecker.getColor(activity, R.color.medium_green), PorterDuff.Mode.SRC_IN)
            proceed?.setTextColor(MethodChecker.getColor(activity, R.color.white))
        } else {
            proceed?.background?.setColorFilter(MethodChecker.getColor(activity, R.color.grey_300), PorterDuff.Mode.SRC_IN)
            proceed?.setTextColor(MethodChecker.getColor(activity, R.color.grey_500))
        }
    }

    override fun onFailedEditProfile(errorMessage: String?) {
        setViewEnabled()
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    private fun findChildTag(): String? {
        val fragment = childFragmentManager.findFragmentById(R.id.fragment_container)
        return if (fragment != null) {
            fragment.tag
        } else ""
    }

    override val userSession: UserSession?
        get() = currentUserSession

    override fun skipView(tag: String?) {
        setViewEnabled()
        when (tag) {
            ProfileCompletionGenderFragment.TAG -> {
                currentData?.gender = 3
                currentData?.let { loadFragment(it, pair) }
            }
            ProfileCompletionDateFragment.TAG -> {
                currentData?.bday = filled
                currentData?.let { loadFragment(it, pair) }
            }
            ProfileCompletionPhoneVerificationFragment.TAG -> {
                currentData?.isPhoneVerified = true
                currentData?.let { loadFragment(it, pair) }
            }
            else -> {}
        }
    }

    override fun onGetUserInfo(profileCompletionViewModel: ProfileCompletionViewModel?) {
        main?.visibility = View.VISIBLE
        currentData = profileCompletionViewModel
        //        testDummyData();
        currentData?.completion?.let { updateProgressBar(0, it) }
        loading?.visibility = View.GONE
        profileCompletionViewModel?.let { loadFragment(it, Pair(0, 0)) }
    }

    override val data: ProfileCompletionViewModel?
        get() = currentData

    override fun getScreenName(): String = ""

    override fun initInjector() {
        val daggerAppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(context))
                .build() as DaggerAppComponent
        val daggerProfileCompletionComponent = DaggerProfileCompletionComponent.builder()
                .appComponent(daggerAppComponent)
                .build() as DaggerProfileCompletionComponent
        daggerProfileCompletionComponent.inject(this)
    }

    companion object {
        private const val DEFAULT_EMPTY_BDAY = "0001-01-01T00:00:00Z"
        private const val ARGS_DATA = "ARGS_DATA"
        private const val REQUEST_CODE_PIN = 200

        fun createInstance(): ProfileCompletionFragment {
            return ProfileCompletionFragment()
        }
    }
}