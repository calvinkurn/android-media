package com.tokopedia.seller.seller.info.view.adapter;

import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.base.list.seller.view.adapter.BaseViewHolder;
import com.tokopedia.seller.R;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;
import com.tokopedia.seller.seller.info.view.model.SellerInfoSectionModel;
import com.tokopedia.seller.seller.info.view.util.SellerInfoDateUtil;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoViewHolder extends BaseViewHolder<SellerInfoModel> {

    private final ImageView imageSellerInfo;
    private final TextView textDateDescription;
    private final TextView textTitle;
    private final View sellerInfoContainer;
    private final TextView textDateDesctionSection;

    private int lightGreenColor, whiteColor;

    public SellerInfoViewHolder(View itemView) {
        super(itemView);
        imageSellerInfo = itemView.findViewById(R.id.image_seller_info);
        textDateDescription = itemView.findViewById(R.id.text_date_description_date);
        textTitle = itemView.findViewById(R.id.text_title);
        sellerInfoContainer = itemView.findViewById(R.id.item_seller_info_container);
        textDateDesctionSection = itemView.findViewById(R.id.text_date_description_section);

        lightGreenColor = ResourcesCompat.getColor(itemView.getResources(), R.color.light_green, null);
        whiteColor = ResourcesCompat.getColor(itemView.getResources(), R.color.white, null);
    }

    @Override
    public void bindObject(SellerInfoModel sellerInfoModel) {
        if(sellerInfoModel instanceof SellerInfoSectionModel)
            return;

        String s = SellerInfoDateUtil.fromUnixTimeGetHourEtc(sellerInfoModel.getCreateTimeUnix());
        textDateDescription.setText(itemView
                .getResources()
                .getString(
                        R.string.seller_info_title_description_format,
                        s));

        textDateDesctionSection.setText(sellerInfoModel.getSection().getName());

        textTitle.setText(sellerInfoModel.getTitle());

        ImageHandler.loadImageWithTarget(imageSellerInfo.getContext(), sellerInfoModel.getInfoThumbnailUrl(), new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(imageSellerInfo.getResources(), resource);
                float radius = imageSellerInfo.getResources().getDimension(R.dimen.seller_info_radius_image_view);
                dr.setCornerRadius(radius);
                imageSellerInfo.setImageDrawable(dr);
            }
        });

        if(sellerInfoModel.isRead()){
            sellerInfoContainer.setBackgroundColor(whiteColor);
        }else{
            sellerInfoContainer.setBackgroundColor(lightGreenColor);
        }
    }
}
