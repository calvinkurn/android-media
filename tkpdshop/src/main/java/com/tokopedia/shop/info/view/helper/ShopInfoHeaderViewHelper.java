package com.tokopedia.shop.info.view.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.R;

/**
 * Created by normansyahputa on 2/12/18.
 */

public class ShopInfoHeaderViewHelper {

    public static final int SHOP_OFFICIAL_VALUE = 1;

    private final View view;
    private RatingBar ratingBarShopInfo;
    private TextView productQualityValue;
    private ImageView speedImageView;
    private TextView speedValueDesd;
    private ImageView shopbgImageView;
    private ShopReputationView shopReputationView;
    private ImageView shopIconImageView;
    private ImageView shopStatusImageView;
    private TextView shopName;
    private TextView shopInfoLocation;
    private ShopInfo shopInfo;

    public ShopInfoHeaderViewHelper(View view){
        this.view = view;

        initView();
    }

    private void initView(){
        ratingBarShopInfo = view.findViewById(R.id.rating_bar_shop_info);

        productQualityValue = view.findViewById(R.id.product_quality_value);

        speedImageView = view.findViewById(R.id.speed_imageview);

        speedValueDesd = view.findViewById(R.id.speed_value_desc);

        shopbgImageView = view.findViewById(R.id.shop_background_imageview);

        shopReputationView = view.findViewById(R.id.shop_reputation_view);

        shopIconImageView = view.findViewById(R.id.shop_icon_imageview);

        shopStatusImageView = view.findViewById(R.id.gold_merchant_status_imageview);

        shopName = view.findViewById(R.id.shop_name);

        shopInfoLocation = view.findViewById(R.id.shop_info_location);
    }

    public void renderData(ShopInfo shopInfo){
        this.shopInfo = shopInfo;

        ImageHandler.LoadImage(shopbgImageView, shopInfo.getInfo().getShopCover());

        shopName.setText(MethodChecker.fromHtml(shopInfo.getInfo().getShopName()).toString());

        ImageHandler.loadImageCircle2(view.getContext(), shopIconImageView, shopInfo.getInfo().getShopAvatar());

        if (!shopInfo.getInfo().getShopCover().isEmpty()) {
            shopStatusImageView.setVisibility(View.VISIBLE);
            if (shopInfo.getInfo().getShopIsOfficial().trim().equals("1")) {
//                shopStatusImageView.setImageResource(R.drawable.ic_badge_official);
            } else {
//                shopStatusImageView.setImageResource(R.drawable.ic_shop_gold);
            }
        } else {
//            holder.infoShop.setBackgroundResource(0);
        }

        if (shopInfo.getInfo().getShopIsOfficial().trim().equals(Integer.toString(SHOP_OFFICIAL_VALUE))) {
//            shopInfoLocation.setText(getResources().getString(R.string.authorized));
        } else {
            shopInfoLocation.setText(shopInfo.getInfo().getShopLocation());
        }

        int set = (int) shopInfo.getStats().getShopBadgeLevel().getSet();
        int level = (int) shopInfo.getStats().getShopBadgeLevel().getLevel();
        shopReputationView.setValue(set,level, shopInfo.getStats().getShopReputationScore());

        productQualityValue.setText(shopInfo.getRatings().getQuality().getAverage());
        long ratingStar = shopInfo.getRatings().getQuality().getRatingStar();
        ratingBarShopInfo.setMax(5);
        ratingBarShopInfo.setRating(ratingStar);

        // kecepatan where ???
    }
}
