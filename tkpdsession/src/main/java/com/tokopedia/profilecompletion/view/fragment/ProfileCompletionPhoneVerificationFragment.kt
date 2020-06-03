package com.tokopedia.profilecompletion.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract
import com.tokopedia.profilecompletion.view.viewmodel.ProfileCompletionViewModel
import com.tokopedia.session.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.util.CustomPhoneNumberUtil.transform

/**
 * Created by nisie on 2/22/17.
 */
class ProfileCompletionPhoneVerificationFragment(view: ProfileCompletionContract.View?) : PhoneVerificationFragment() {
    private var parentView: ProfileCompletionContract.View? = view
    private var parentPresenter: ProfileCompletionContract.Presenter? = null
    private var verifyButton: TextView? = null
    private var data: ProfileCompletionViewModel? = null
    private var skipFragment: TextView? = null
    private var currentUserSession: UserSession? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (parentView != null) {
            parentPresenter = parentView?.getPresenter()
            currentUserSession = parentView?.userSession
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun findView(view: View) {
        super.findView(view)
        initView()
    }

    override fun setViewListener() {
        super.setViewListener()
        skipButton.visibility = View.GONE
        skipFragment?.setOnClickListener { parentPresenter?.skipView(TAG) }
    }

    private fun initView() {
        if (parentView == null && activity is ProfileCompletionActivity) parentView = (activity as ProfileCompletionActivity?)?.profileCompletionContractView
        data = parentView?.data
        verifyButton = parentView?.getView()?.findViewById(R.id.proceed)
        verifyButton?.text = resources.getString(R.string.continue_form)
        verifyButton?.visibility = View.GONE
        skipFragment = parentView?.getView()?.findViewById(R.id.skip)
        skipFragment?.visibility = View.GONE
        if (data?.phone != null) {
            phoneNumberEditText.text = transform(data?.phone?: "")
        } else {
            SnackbarManager.make(activity,
                    getString(R.string.please_fill_phone_number),
                    Snackbar.LENGTH_LONG)
                    .show()
        }
        KeyboardHandler.DropKeyboard(activity, view)
    }

    override fun onSuccessVerifyPhoneNumber() {
        parentView?.userSession?.setIsMSISDNVerified(true)
        currentUserSession?.phoneNumber = phoneNumber
        parentView?.onSuccessEditProfile(EditUserProfileUseCase.EDIT_VERIF)
        Toast.makeText(activity, MethodChecker.fromHtml(getString(R.string.success_verify_phone_number)), Toast.LENGTH_LONG).show()
    }

    companion object {
        const val TAG = "verif"

        fun createInstance(view: ProfileCompletionContract.View?): ProfileCompletionPhoneVerificationFragment {
            return ProfileCompletionPhoneVerificationFragment(view)
        }
    }
}