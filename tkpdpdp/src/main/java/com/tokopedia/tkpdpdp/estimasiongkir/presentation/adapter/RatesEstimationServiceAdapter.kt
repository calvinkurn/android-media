package com.tokopedia.tkpdpdp.estimasiongkir.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.tokopedia.tkpdpdp.R
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.ShippingServiceModel
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.viewholder.RatesEstimationServiceViewHolder

import java.util.ArrayList

class RatesEstimationServiceAdapter : RecyclerView.Adapter<RatesEstimationServiceViewHolder>() {
    private val shippingServiceModels = ArrayList<ShippingServiceModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesEstimationServiceViewHolder {
        return RatesEstimationServiceViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rates_estimation_service, parent, false))
    }

    override fun onBindViewHolder(holder: RatesEstimationServiceViewHolder, position: Int) {
        holder.bind(shippingServiceModels[position])
    }

    override fun getItemCount() = shippingServiceModels.size

    fun updateShippingServices(shippingServiceModels: List<ShippingServiceModel>) {
        this.shippingServiceModels.clear()
        this.shippingServiceModels.addAll(shippingServiceModels)
        notifyDataSetChanged()
    }
}
