package com.tokopedia.shop.info.view.helper;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.shop.R;
import com.tokopedia.shop.info.view.activity.ShopInfoActivity;

import java.util.Random;

/**
 * Created by normansyahputa on 2/12/18.
 */

public class ShopInfoHeaderViewHelper {

    public static final int SHOP_OFFICIAL_VALUE = 1;

    private final View view;
    private UserSession userSession;
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
    private LinearLayout containerClickInfo;

    private Button buttonManageShop;
    private Button buttonAddProduct;
    private Button buttonChatSeller;
    private Button buttonShopPage;
    private Button button;

    public ShopInfoHeaderViewHelper(View view, UserSession userSession){
        this.view = view;
        this.userSession = userSession;

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

        containerClickInfo = view.findViewById(R.id.container_click_info);

        buttonManageShop = view.findViewById(R.id.button_manage_shop);

        buttonAddProduct = view.findViewById(R.id.button_add_product);

        buttonChatSeller = view.findViewById(R.id.button_chat_seller);

        buttonShopPage = view.findViewById(R.id.button_shop_page);

        button = view.findViewById(R.id.button);
    }

    public void renderData(final ShopInfo shopInfo){
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

        final String shopId = shopInfo.getInfo().getShopId();

        containerClickInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ShopInfoActivity.createIntent(view.getContext(), shopId);
                view.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.reputation_click_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Reputation Click", Toast.LENGTH_LONG).show();
            }
        });
        view.findViewById(R.id.product_quality_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Product Quality Click", Toast.LENGTH_LONG).show();
            }
        });
        view.findViewById(R.id.speed_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Speed Click", Toast.LENGTH_LONG).show();

                button.setVisibility(new Random().nextBoolean() ? View.VISIBLE : View.GONE);
            }
        });

        buttonManageShop.setVisibility(View.GONE);
        buttonAddProduct.setVisibility(View.GONE);
        buttonChatSeller.setVisibility(View.GONE);
        buttonShopPage.setVisibility(View.GONE);

        if(userSession.getShopId().equals(shopInfo.getInfo().getShopId())){
            buttonManageShop.setVisibility(View.VISIBLE);
            buttonAddProduct.setVisibility(View.VISIBLE);
        }else{
            buttonChatSeller.setVisibility(View.VISIBLE);
            buttonShopPage.setVisibility(View.VISIBLE);
        }
    }

    public void renderData(SpeedReputation speedReputation){
        speedValueDesd.setText(speedReputation.getData().getSpeed().getRecent1Month().getSpeedLevelDescription());
    }
}
