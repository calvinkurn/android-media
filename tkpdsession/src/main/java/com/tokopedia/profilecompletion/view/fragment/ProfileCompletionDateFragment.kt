package com.tokopedia.profilecompletion.view.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract
import com.tokopedia.profilecompletion.view.util.Events
import com.tokopedia.profilecompletion.view.util.Properties
import com.tokopedia.session.R
import rx.Observable
import rx.functions.Action1
import java.text.DateFormatSymbols
import java.util.*

/**
 * Created by stevenfredian on 7/3/17.
 */
class ProfileCompletionDateFragment : BaseDaggerFragment() {

    private var month: AutoCompleteTextView? = null
    private var actvContainer: View? = null
    private var year: TextInputEditText? = null
    private var date: TextInputEditText? = null
    private var profileCompletionFragment: ProfileCompletionFragment? = null
    private var proceed: View? = null
    private var skip: View? = null
    private var progress: View? = null
    private var position = 0
    private var presenter: ProfileCompletionContract.Presenter? = null
    private var dateObservable: Observable<String>? = null
    private var yearObservable: Observable<String>? = null
    private var monthObservable: Observable<Int>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(R.layout.fragment_profile_completion_dob, container, false)
        if (profileCompletionFragment != null && profileCompletionFragment?.view != null) {
            initView(parentView)
            setViewListener()
            initialVar()
            setUpFields()
        } else if (activity != null) {
            activity?.finish()
        }
        return parentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    private fun initView(view: View) {
        date = view.findViewById(R.id.date)
        month = view.findViewById(R.id.month)
        year = view.findViewById(R.id.year)
        actvContainer = view.findViewById(R.id.autoCompleteTextViewContainer)
        proceed = this.profileCompletionFragment?.view?.findViewById(R.id.proceed)
        skip = this.profileCompletionFragment?.view?.findViewById(R.id.skip)
        progress = this.profileCompletionFragment?.view?.findViewById(R.id.progress)
        proceed?.isEnabled = false
        this.profileCompletionFragment?.canProceed(false)

        val drawable = MethodChecker.getDrawable(activity, R.drawable.chevron_thin_down)
        drawable.setColorFilter(MethodChecker.getColor(activity, R.color.warm_grey), PorterDuff.Mode.SRC_IN)
        val size = drawable.intrinsicWidth * 0.3
        drawable.setBounds(0, 0, size.toInt(), size.toInt())
        month?.setCompoundDrawables(null, null, drawable, null)
    }

    private fun setViewListener() {
        actvContainer?.setOnClickListener {
            month?.showDropDown()
            KeyboardHandler.DropKeyboard(activity, view)
        }
        month?.setOnClickListener {
            month?.showDropDown()
            KeyboardHandler.DropKeyboard(activity, view)
        }
        month?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, pos, rowId ->
            position = -1
            position = pos + 1
        }
        proceed?.setOnClickListener {
            presenter?.editUserInfo(date?.text.toString(), position, year?.text.toString())
        }
        skip?.setOnClickListener {
            presenter?.skipView(TAG)
        }
        year?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length == 4) {
                    val theYear = charSequence.toString().toInt()
                    if (theYear < YEAR_MIN) {
                        year!!.setText(YEAR_MIN.toString())
                    } else if (theYear > YEAR_MAX) {
                        year!!.setText(YEAR_MAX.toString())
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun initialVar() {
        val monthsIndo = DateFormatSymbols(Locale.getDefault()).months
        val adapter = ArrayAdapter(activity, R.layout.select_dialog_item_material, monthsIndo)
        month?.setAdapter(adapter)
        presenter = profileCompletionFragment?.presenter

        dateObservable = date?.let { Events.text(it) }
        yearObservable = year?.let { Events.text(it) }
        monthObservable = month?.let { Events.select(it) }

        val dateMapper = dateObservable?.map { text -> text.trim { it <= ' ' } != "" }
        val yearMapper = yearObservable?.map { text -> text.trim { it <= ' ' } != "" }
        val monthMapper = monthObservable?.map { integer ->
            position = integer
            integer != 0
        }

        val allField = Observable.combineLatest(dateMapper, yearMapper, monthMapper) {
            date, year, month -> date && month && year
        }.map { aBoolean -> aBoolean }

        val onError = Action1 {
            obj: Throwable -> obj.printStackTrace()
        }

        allField.subscribe(proceed?.let { Properties.enabledFrom(it) }, onError)
        allField.subscribe(Action1 { aBoolean -> profileCompletionFragment?.canProceed(aBoolean) }, onError)
    }

    private fun setUpFields() {}
    override fun getScreenName(): String = ""
    override fun initInjector() {}

    companion object {
        private const val YEAR_MIN = 1937
        private const val YEAR_MAX = 2007
        const val TAG = "date"

        fun createInstance(view: ProfileCompletionFragment?): ProfileCompletionDateFragment {
            val fragment = ProfileCompletionDateFragment()
            fragment.profileCompletionFragment = view
            return fragment
        }
    }
}