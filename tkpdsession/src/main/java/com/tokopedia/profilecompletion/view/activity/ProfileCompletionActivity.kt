package com.tokopedia.profilecompletion.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.core.app.BasePresenterActivity
import com.tokopedia.core.base.di.component.AppComponent
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFinishedFragment
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFragment
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract
import com.tokopedia.session.R

/**
 * @author by nisie on 6/19/17.
 */
class ProfileCompletionActivity : BasePresenterActivity<Any?>(), HasComponent<Any?> {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppBarTheme)
        super.onCreate(savedInstanceState)
    }

    override fun setupURIPass(data: Uri) {}
    override fun setupBundlePass(extras: Bundle) {}
    override fun initialPresenter() {}
    override fun getLayoutId(): Int = R.layout.activity_simple_fragment

    override fun initView() {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(MethodChecker.getColor(this, R.color.white)))
        toolbar.setTitleTextColor(MethodChecker.getColor(this, R.color.grey_700))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 10f
        }
        toolbar.setPadding(toolbar.paddingLeft, toolbar.paddingTop, 30, toolbar.paddingBottom)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.ic_action_back)
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, com.tokopedia.core2.R.color.grey_700), PorterDuff.Mode.SRC_ATOP)
            supportActionBar?.setHomeAsUpIndicator(upArrow)
        }

        var fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment == null) {
            fragment = ProfileCompletionFragment.createInstance()
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment, fragment.javaClass.simpleName)
        fragmentTransaction.commit()
    }

    override fun setViewListener() {}
    override fun initVar() {}
    override fun setActionVar() {}

    fun onFinishedForm() {
        val fragment = ProfileCompletionFinishedFragment.createInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment, fragment.javaClass.simpleName)
        fragmentTransaction.commit()
    }

    val profileCompletionContractView: ProfileCompletionContract.View?
        get() = if (supportFragmentManager.findFragmentById(R.id.container) is ProfileCompletionContract.View) {
            supportFragmentManager.findFragmentById(R.id.container) as ProfileCompletionFragment?
        } else throw RuntimeException(ERROR_IMPLEMENT_LISTENER)

    override fun getComponent(): AppComponent {
        return applicationComponent
    }

    companion object {
        private const val ERROR_IMPLEMENT_LISTENER = "Error not implementing " +
                "ProfileCompletionContract.View"
    }

    object DeeplinkIntent{
        @JvmStatic
        @DeepLink(ApplinkConst.PROFILE_COMPLETION)
        fun getCallingTopProfile(context: Context?, bundle: Bundle?): Intent {
            return Intent(context, ProfileCompletionActivity::class.java)
        }
    }
}