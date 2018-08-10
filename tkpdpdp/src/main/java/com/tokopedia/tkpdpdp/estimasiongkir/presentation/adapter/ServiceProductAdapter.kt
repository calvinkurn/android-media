package com.tokopedia.tkpdpdp.estimasiongkir.presentation.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.tkpdpdp.R
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.ShippingServiceModel
import kotlinx.android.synthetic.main.item_service_product.view.*

class ServiceProductAdapter : RecyclerView.Adapter<ServiceProductAdapter.ServiceProductViewHolder>() {
    private val products: MutableList<ShippingServiceModel.Product> = mutableListOf()

    fun replaceProducts(products: List<ShippingServiceModel.Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceProductViewHolder {
        return ServiceProductViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_service_product, parent, false))
    }

    override fun onBindViewHolder(holder: ServiceProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    inner class ServiceProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(product: ShippingServiceModel.Product) {
            val title = itemView.context.getString(R.string.shipping_receiver_text,
                    product.name, product.etd)
            val spannableString = SpannableString(title)

            spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(itemView.context,
                    R.color.font_black_disabled_38)),
                    product.name.length + 1, title.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

            itemView.label_view.setTitle(spannableString)

            itemView.label_view.setContent(product.fmtPrice)
        }
    }
}
