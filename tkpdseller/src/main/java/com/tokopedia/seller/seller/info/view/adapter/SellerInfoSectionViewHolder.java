package com.tokopedia.seller.seller.info.view.adapter;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.seller.info.view.model.SellerInfoSectionModel;
import com.tokopedia.seller.seller.info.view.util.SellerInfoDateUtil;

/**
 * Created by normansyahputa on 12/5/17.
 */

public class SellerInfoSectionViewHolder extends BaseViewHolder<SellerInfoSectionModel> {
    private final TextView dateText;
    private String today, yesterday;

    private SellerInfoDateUtil sellerInfoDateUtil;

    public SellerInfoSectionViewHolder(View itemView) {
        super(itemView);

        dateText = itemView.findViewById(R.id.text_date_section);

        today = itemView.getResources().getString(R.string.item_today);
        yesterday = itemView.getResources().getString(R.string.yesterday);
    }

    public void setSellerInfoDateUtil(SellerInfoDateUtil sellerInfoDateUtil) {
        this.sellerInfoDateUtil = sellerInfoDateUtil;
    }

    @Override
    public void bindObject(SellerInfoSectionModel sellerInfoSectionModel) {
        if(sellerInfoSectionModel.isToday()){
            dateText.setText(today);
        }else if(sellerInfoSectionModel.isYesterday()){
            dateText.setText(yesterday);
        }else {
            dateText.setText(sellerInfoDateUtil.fromUnixTime(sellerInfoSectionModel.getCreateTimeUnix()));
        }
    }
}
