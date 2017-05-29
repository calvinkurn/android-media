package com.tokopedia.seller.topads.keyword.view.adapter.viewholder;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.domain.model.Datum;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordEditDetailActivity;
import com.tokopedia.seller.topads.keyword.view.model.TopAdsKeywordEditDetailViewModel;

/**
 * Created by normansyahputa on 5/19/17.
 */

public class TopAdsKeywordViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleProduct;
    private final TextView statusActive;
    private final TextView pricePromoPerClick;
    private final TextView promoPriceUsed;
    private final TextView groupName;

    public TopAdsKeywordViewHolder(View itemView) {
        super(itemView);
        titleProduct = (TextView) itemView.findViewById(R.id.title_product);
        statusActive = (TextView) itemView.findViewById(R.id.status_active);
        pricePromoPerClick = (TextView) itemView.findViewById(R.id.price_promo_per_click);
        promoPriceUsed = (TextView) itemView.findViewById(R.id.promo_price_used);
        groupName = (TextView) itemView.findViewById(R.id.group_name);

    }

    private String getString(@StringRes int stringRes) {
        return itemView.getContext().getString(stringRes);
    }

    private String getString(@StringRes int stringRes, String value) {
        return itemView.getContext().getString(stringRes, value);
    }


    private double getPrice(String price){
        String valueString = CurrencyFormatHelper
                .removeCurrencyPrefix(price);
        valueString = CurrencyFormatHelper.RemoveNonNumeric(valueString);
        if (TextUtils.isEmpty(valueString)) {
            return 0;
        }
        return Double.parseDouble(valueString);
    }

    public void bindDataAds(final Datum datum) {
        titleProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopAdsKeywordEditDetailViewModel model = new TopAdsKeywordEditDetailViewModel();

                model.setKeywordTag(datum.getKeywordTag());
                model.setKeywordId(datum.getKeywordId());
//                model.setKeywordTypeId(Integer.parseInt(datum.getKeywordTypeId()));
                model.setGroupId((datum.getGroupId()));
                model.setPriceBid(getPrice(datum.getKeywordPriceBidFmt()));
                model.setToggle(datum.getKeywordStatusToogle());

                TopAdsKeywordEditDetailActivity.start(titleProduct.getContext(), model);
            }
        });
        titleProduct.setText(datum.getKeywordTag());
        statusActive.setText(datum.getKeywordStatusDesc());
        pricePromoPerClick.setText(getString(R.string.top_ads_per_click_detail, datum.getKeywordPriceBidFmt()));
        promoPriceUsed.setText(datum.getStatTotalSpent());
        groupName.setText(datum.getGroupName());
    }
}
