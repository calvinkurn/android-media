package com.tokopedia.profilecompletion.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract
import com.tokopedia.session.R

/**
 * Created by stevenfredian on 7/3/17.
 */
class ProfileCompletionGenderFragment : BaseDaggerFragment() {

    private var profileCompletionFragment: ProfileCompletionFragment? = null
    private var proceed: View? = null
    private var radioGroup: RadioGroup? = null
    private var avaWoman: View? = null
    private var avaMan: View? = null
    private var skip: View? = null
    private var progress: View? = null
    private var presenter: ProfileCompletionContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(R.layout.fragment_profile_completion_gender, container, false)
        initView(parentView)
        setViewListener()
        initialVar()
        return parentView
    }

    private val fragmentLayout: Int = R.layout.fragment_profile_completion_gender

    private fun initView(view: View) {
        avaMan = view.findViewById(R.id.ava_man)
        avaWoman = view.findViewById(R.id.ava_woman)
        radioGroup = view.findViewById(R.id.radioGroup)
        proceed = profileCompletionFragment?.view?.findViewById(R.id.proceed)
        skip = profileCompletionFragment?.view?.findViewById(R.id.skip)
        progress = profileCompletionFragment?.view?.findViewById(R.id.progress)
        profileCompletionFragment?.canProceed(false)
    }

    private fun setViewListener() {
        radioGroup?.setOnCheckedChangeListener { radioGroup, i ->
            profileCompletionFragment?.canProceed(true)
        }
        proceed?.setOnClickListener {
            val selected = radioGroup?.findViewById<View>(radioGroup?.checkedRadioButtonId?: 0)
            var idx = radioGroup?.indexOfChild(selected)
            if (selected === avaMan) {
                idx = EditUserProfileUseCase.MALE
            } else if (selected === avaWoman) {
                idx = EditUserProfileUseCase.FEMALE
            }
            idx?.let { presenter?.editUserInfo(it) }
        }
        skip?.setOnClickListener { presenter?.skipView(TAG) }
    }

    private fun initialVar() {
        presenter = profileCompletionFragment?.presenter
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    companion object {
        const val TAG = "gender"

        fun createInstance(view: ProfileCompletionFragment?): ProfileCompletionGenderFragment {
            val fragment = ProfileCompletionGenderFragment()
            fragment.profileCompletionFragment = view
            return fragment
        }
    }
}