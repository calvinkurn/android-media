package com.tokopedia.seller.purchase.detail.fragment

import android.app.Fragment
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.seller.R
import com.tokopedia.seller.purchase.detail.activity.OrderDetailActivity
import com.tokopedia.seller.purchase.utils.OrderDetailConstant.PARAM_CONFIRM_PICKUP_ORDER_ID
import com.tokopedia.seller.purchase.utils.OrderDetailConstant.PARAM_CONFIRM_PICKUP_ORIGIN_ADDRESS
import kotlinx.android.synthetic.main.fragment_confirm_request_pickup.*

/**
 * Created by fwidjaja on 2019-10-18.
 */
class ConfirmRequestPickupFragment: Fragment() {
    private var orderId = ""
    private var originAddress = ""
    private lateinit var actionListener: ActionListener

    interface ActionListener {
        fun onClickRequestPickupBtn(orderId: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): ConfirmRequestPickupFragment {
            return ConfirmRequestPickupFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_CONFIRM_PICKUP_ORDER_ID, bundle.getString(PARAM_CONFIRM_PICKUP_ORDER_ID))
                    putString(PARAM_CONFIRM_PICKUP_ORIGIN_ADDRESS, bundle.getString(PARAM_CONFIRM_PICKUP_ORIGIN_ADDRESS))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            orderId = arguments?.getString(PARAM_CONFIRM_PICKUP_ORDER_ID).toString()
            originAddress = arguments?.getString(PARAM_CONFIRM_PICKUP_ORIGIN_ADDRESS).toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as OrderDetailActivity).setActionBarTitle(getString(R.string.request_pickup_title))
        return inflater.inflate(R.layout.fragment_confirm_request_pickup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupListeners()
    }

    private fun setupLayout() {
        shop_address?.text = originAddress
        card_address?.let {
            it.setCardBackgroundColor(ContextCompat.getColor(it.context, R.color.grey_200))
        }
    }

    private fun setupListeners() {
        btn_req_pickup?.setOnClickListener {
            actionListener.onClickRequestPickupBtn(orderId)
        }
    }

    fun setListeners(activity: OrderDetailActivity) {
        this.actionListener = activity
    }
}