package com.tokopedia.seller.gmsubscribe.view.recyclerview;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.gmsubscribe.view.viewmodel.GmProductViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public class GmProductViewHolder extends RecyclerView.ViewHolder {

    private final Context mContext;

    @BindView(R2.id.title_package)
    TextView titlePackage;

    @BindView(R2.id.promo_package)
    TextView promoPackage;

    @BindView(R2.id.description_package)
    TextView descriptionPackage;

    @BindView(R2.id.description_free_days)
    TextView descriptionFreeDays;

    @BindView(R2.id.price_package)
    TextView pricePackage;

    @BindView(R2.id.last_price_package)
    TextView lastPricePakcage;

    @BindView(R2.id.icon_check)
    ImageView iconCheck;

    @BindView(R2.id.layout_view)
    RelativeLayout layoutView;

    public GmProductViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
    }

    public void renderData(GmProductViewModel gmProductViewModel, boolean isSelected) {
        titlePackage.setText(gmProductViewModel.getName());
        descriptionPackage.setText(gmProductViewModel.getNotes());
        promoPackage.setVisibility(
                gmProductViewModel.isBestDeal() ? View.VISIBLE : View.GONE);
        pricePackage.setText(gmProductViewModel.getPrice());
        if (!gmProductViewModel.getFreeDays().isEmpty()) {
            descriptionFreeDays.setVisibility(View.VISIBLE);
            descriptionFreeDays.setText(gmProductViewModel.getFreeDays());
        }
        if (!gmProductViewModel.getLastPrice().isEmpty()) {
            setViewForLastPrice(gmProductViewModel.getLastPrice());
        }
        if (isSelected) {
            setSelected();
        } else {
            setUnselected();
        }
    }

    private void setViewForLastPrice(String lastPrice) {
        pricePackage.setPaintFlags(pricePackage.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        pricePackage.setTextColor(mContext.getResources().getColor(R.color.primary_text_default_material_light));
        pricePackage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        lastPricePakcage.setText(lastPrice);
        lastPricePakcage.setVisibility(View.VISIBLE);
    }

    public void setSelected() {
        iconCheck.setVisibility(View.VISIBLE);
        try {
            layoutView.setBackgroundResource(R.drawable.background_gmsubscribe_product_item_selected);
        } catch (NoSuchMethodError e) {
            layoutView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.background_gmsubscribe_product_item_selected));
        }
    }

    public void setUnselected() {
        iconCheck.setVisibility(View.GONE);
        try {
            layoutView.setBackgroundResource(R.drawable.background_gmsubscribe_product_item_unselected);
        } catch (NoSuchMethodError e) {
            layoutView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.background_gmsubscribe_product_item_unselected));
        }
    }


}
