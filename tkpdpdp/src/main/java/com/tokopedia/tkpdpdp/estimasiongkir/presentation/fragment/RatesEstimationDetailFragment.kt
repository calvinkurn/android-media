package com.tokopedia.tkpdpdp.estimasiongkir.presentation.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.tkpdpdp.R
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesModel
import com.tokopedia.tkpdpdp.estimasiongkir.constant.RatesEstimationConstant
import com.tokopedia.tkpdpdp.estimasiongkir.di.RatesEstimationComponent
import com.tokopedia.tkpdpdp.estimasiongkir.listener.RatesEstimationDetailView
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.presenter.RatesEstimationDetailPresenter
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.adapter.RatesEstimationServiceAdapter
import kotlinx.android.synthetic.main.fragment_rates_estimation_detail.*
import kotlinx.android.synthetic.main.partial_header_rate_estimation.*

import javax.inject.Inject

class RatesEstimationDetailFragment : BaseDaggerFragment(), RatesEstimationDetailView {

    @Inject
    lateinit var presenter: RatesEstimationDetailPresenter
    @Inject
    lateinit var userSession: UserSession

    private var productId: String = ""
    private var productWeightFmt: String = ""

    private val adapter = RatesEstimationServiceAdapter()

    override fun initInjector() {
        getComponent(RatesEstimationComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun getScreenName(): String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rates_estimation_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            productId = it.getString(RatesEstimationConstant.PARAM_PRODUCT_ID, "")
            productWeightFmt = it.getString(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, "")
        }

        recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(DividerItemDecoration(activity))

        shipping_weight.text = getString(R.string.rate_est_detail_weight_fmt, productWeightFmt)

        getCostEstimation()
    }

    private fun getCostEstimation() {
        setViewState(VIEW_LOADING)
        presenter.getCostEstimation(GraphqlHelper.loadRawString(resources, R.raw.gql_pdp_estimasi_ongkir), productId)
    }

    private fun setViewState(viewLoading: Int) {
        when (viewLoading) {
            VIEW_LOADING -> {
                loading_state_view.visibility = View.VISIBLE
                app_bar_layout.visibility = View.INVISIBLE
                recycler_view.visibility = View.INVISIBLE
            }
            VIEW_CONTENT -> {
                loading_state_view.visibility = View.GONE
                app_bar_layout.visibility = View.VISIBLE
                recycler_view.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onErrorLoadRateEstimaion(throwable: Throwable) {

    }

    override fun onSuccesLoadRateEstimaion(ratesModel: RatesModel) {
        shipping_destination.text = getString(R.string.rate_est_detail_dest_fmt, ratesModel.texts.textDestination)
        val title = getString(R.string.shipping_receiver_text, userSession!!.name, "Alamat Kantor")
        val spannableString = SpannableString(title)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, userSession.name.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(activity!!, R.color.font_black_disabled_38)),
                userSession.name.length + 1, title.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        shipping_receiver_name.text = spannableString
        shipping_receiver_address.text = String.format("%s\n%s", "0817 1234 5678",
                "Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2, Tokopedia Lt. 2, Jakarta")
        adapter.updateShippingServices(ratesModel.attributes)
        setViewState(VIEW_CONTENT)
    }

    companion object {
        private val VIEW_CONTENT = 1
        private val VIEW_LOADING = 2

        fun createInstance(productId: String, productWeightFmt: String) = RatesEstimationDetailFragment().apply {
            arguments = Bundle().apply {
                putString(RatesEstimationConstant.PARAM_PRODUCT_ID, productId)
                putString(RatesEstimationConstant.PARAM_PRODUCT_WEIGHT, productWeightFmt)
            }
        }
    }
}
