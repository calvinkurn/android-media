package com.tokopedia.tkpdpdp.estimasiongkir.presentation.adapter;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.estimasiongkir.ShippingServiceModel;

import java.util.ArrayList;
import java.util.List;

public class ServiceProductAdapter extends RecyclerView.Adapter<ServiceProductAdapter.ServiceProductViewHolder> {
    private List<ShippingServiceModel.Product> products;

    public ServiceProductAdapter() {
        products = new ArrayList<>();
    }

    public void replaceProducts(List<ShippingServiceModel.Product> products){
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceProductViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ServiceProductViewHolder extends RecyclerView.ViewHolder {
        private LabelView labelView;
        public ServiceProductViewHolder(View itemView) {
            super(itemView);
            labelView = itemView.findViewById(R.id.label_view);
        }

        public void bind(ShippingServiceModel.Product product){
            String title = itemView.getContext().getString(R.string.shipping_receiver_text,
                    product.getName(), product.getEtd());
            SpannableString spannableString = new SpannableString(title);

            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(),
                    R.color.font_black_disabled_38)),
                    product.getName().length()+1, title.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            labelView.setTitle(spannableString);

            labelView.setContent(product.getFmtPrice());
        }
    }
}
