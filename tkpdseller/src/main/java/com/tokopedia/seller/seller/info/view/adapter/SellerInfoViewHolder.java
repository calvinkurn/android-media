package com.tokopedia.seller.seller.info.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;
import com.tokopedia.seller.seller.info.view.util.SellerInfoDateUtil;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoViewHolder extends BaseViewHolder<SellerInfoModel> {

    private final ImageView imageSellerInfo;
    private final TextView textDateDescription;
    private final TextView textTitle;

    private SellerInfoDateUtil sellerInfoDateUtil;

    @Override
    public void bindObject(SellerInfoModel sellerInfoModel) {
        String s = sellerInfoDateUtil.fromUnixTime(sellerInfoModel.getCreateTimeUnix());
        textDateDescription.setText(s);

        textTitle.setText(sellerInfoModel.getTitle());

        ImageHandler.LoadImage(imageSellerInfo, sellerInfoModel.getInfoThumbnailUrl());
    }

    public void setSellerInfoDateUtil(SellerInfoDateUtil sellerInfoDateUtil) {
        this.sellerInfoDateUtil = sellerInfoDateUtil;
    }

    public SellerInfoViewHolder(View itemView) {
        super(itemView);

        imageSellerInfo = itemView.findViewById(R.id.image_seller_info);
        textDateDescription = itemView.findViewById(R.id.text_date_description);
        textTitle = itemView.findViewById(R.id.text_title);
    }
}
